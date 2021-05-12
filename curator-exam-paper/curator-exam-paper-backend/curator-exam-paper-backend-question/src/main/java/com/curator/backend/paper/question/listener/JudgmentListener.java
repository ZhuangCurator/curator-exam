package com.curator.backend.paper.question.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.curator.api.paper.enums.QuestionDifficultyEnum;
import com.curator.api.paper.enums.QuestionTypeEnum;
import com.curator.backend.paper.question.entity.vo.info.QuestionAnswerInfo;
import com.curator.backend.paper.question.entity.vo.info.QuestionExcelInfo;
import com.curator.backend.paper.question.entity.vo.info.QuestionInfo;
import com.curator.common.util.Help;
import com.curator.common.util.JsonUtil;
import com.curator.common.util.RedissonUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 判断题监听器
 *
 * @author Jun
 * @date 2021-05-08
 */
@Slf4j
public class JudgmentListener extends AnalysisEventListener<QuestionExcelInfo> {

    String createAccountId;
    List<QuestionInfo> questionInfoList = new ArrayList<>();
    List<QuestionExcelInfo> excelInfoList = new ArrayList<>();

    public JudgmentListener(String createAccountId) {
        this.createAccountId = createAccountId;
    }

    @Override
    public void invoke(QuestionExcelInfo excelInfo, AnalysisContext context) {
        log.info("解析到一条判断题数据:{}", JsonUtil.obj2String(excelInfo));
        QuestionInfo questionInfo = new QuestionInfo();
        try {
            questionInfo.setQuestionStem(excelInfo.getQuestionStem());
            questionInfo.setQuestionAnalysis(excelInfo.getAnalysis());
            questionInfo.setQuestionPoint(excelInfo.getPoint());
            questionInfo.setOrdered(0);
            questionInfo.setQuestionType(QuestionTypeEnum.JUDGMENT.getStatus());
            // 试题难度
            if (Help.isNotEmpty(excelInfo.getDifficulty())) {
                Integer difficulty = QuestionDifficultyEnum.getStatus(excelInfo.getDifficulty().trim());
                if (Help.isEmpty(difficulty)) {
                    excelInfo.setReason("试题难度请填写初级或中级或高级!");
                    excelInfoList.add(excelInfo);
                    return;
                }
                questionInfo.setQuestionDifficulty(difficulty);
            } else {
                excelInfo.setReason("试题难度不能为空!");
                excelInfoList.add(excelInfo);
                return;
            }
            // 试题答案内容
            List<QuestionAnswerInfo> questionAnswerInfoList = new ArrayList<>();
            AtomicInteger order = new AtomicInteger(0);
            // 答案
            String answer = excelInfo.getAnswer().trim();
            if (Help.isEmpty(answer)) {
                excelInfo.setReason("答案不能为空!");
                excelInfoList.add(excelInfo);
                return;
            } else {
                answer = answer.toUpperCase();
                String regex = "[AB]";
                if (!answer.matches(regex)) {
                    excelInfo.setReason("单选题答案请填写AB!");
                    excelInfoList.add(excelInfo);
                    return;
                }
            }
            // 选项A
            if (Help.isNotEmpty(excelInfo.getOptionsA())) {
                QuestionAnswerInfo info = new QuestionAnswerInfo();
                info.setContent(excelInfo.getOptionsA());
                info.setQuestionAnswerOrder(order.incrementAndGet());
                String option = "A";
                if (answer.equals(option)) {
                    info.setRighted(1);
                } else {
                    info.setRighted(0);
                }
                questionAnswerInfoList.add(info);
            } else {
                excelInfo.setReason("选项A不能为空!");
                excelInfoList.add(excelInfo);
                return;
            }
            // 选项B
            if (Help.isNotEmpty(excelInfo.getOptionsB())) {
                QuestionAnswerInfo info = new QuestionAnswerInfo();
                info.setContent(excelInfo.getOptionsB());
                String option = "B";
                if (answer.equals(option)) {
                    info.setRighted(1);
                } else {
                    info.setRighted(0);
                }
                questionAnswerInfoList.add(info);
            } else {
                excelInfo.setReason("选项B不能为空!");
                excelInfoList.add(excelInfo);
                return;
            }
            questionInfo.setQuestionAnswerInfoList(questionAnswerInfoList);
            questionInfoList.add(questionInfo);
        } catch (Exception e) {
            excelInfo.setReason("请查看分值是否有误!");
            excelInfoList.add(excelInfo);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (Help.isNotEmpty(questionInfoList)) {
            RedissonUtil.setCacheList("QUESTION:IMPORT:OK:" + createAccountId, questionInfoList);
            RedissonUtil.expire("QUESTION:IMPORT:OK:" + createAccountId, 5, TimeUnit.MINUTES);
        }
        if (Help.isNotEmpty(excelInfoList)) {
            RedissonUtil.setCacheList("QUESTION:IMPORT:WRONG:" + createAccountId, excelInfoList);
            RedissonUtil.expire("QUESTION:IMPORT:WRONG:" + createAccountId, 5, TimeUnit.MINUTES);
        }
    }
}
