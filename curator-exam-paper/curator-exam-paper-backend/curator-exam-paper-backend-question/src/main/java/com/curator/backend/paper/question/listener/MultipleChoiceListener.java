package com.curator.backend.paper.question.listener;

import cn.hutool.core.util.ReUtil;
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 多选题监听器
 *
 * @author Jun
 * @date 2021-05-08
 */
@Slf4j
public class MultipleChoiceListener extends AnalysisEventListener<QuestionExcelInfo> {

    String createAccountId;
    List<QuestionInfo> questionInfoList = new ArrayList<>();
    List<QuestionExcelInfo> excelInfoList = new ArrayList<>();

    public MultipleChoiceListener(String createAccountId) {
        this.createAccountId = createAccountId;
    }

    @Override
    public void invoke(QuestionExcelInfo excelInfo, AnalysisContext context) {
        log.info("解析到一条多项选择题数据:{}", JsonUtil.obj2String(excelInfo));
        QuestionInfo questionInfo = new QuestionInfo();
        try {
            questionInfo.setQuestionStem(excelInfo.getQuestionStem());
            questionInfo.setQuestionAnalysis(excelInfo.getAnalysis());
            questionInfo.setQuestionPoint(excelInfo.getPoint());
            questionInfo.setOrdered(0);
            questionInfo.setQuestionType(QuestionTypeEnum.MULTIPLE_CHOICE.getStatus());
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
            List<String> answerList = new ArrayList<>();
            Field[] fields = excelInfo.getClass().getDeclaredFields();
            for (Field field : fields) {
                // 获取原来的访问控制权限
                boolean accessFlag = field.isAccessible();
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                String fieldName = field.getName();
                if (ReUtil.isMatch("answer[A-Za-z]+", fieldName)) {
                    try {
                        // 答案
                        String fieldValue = String.valueOf(field.get(excelInfo)).trim();
                        if (Help.isNotEmpty(fieldValue)) {
                            answerList.add(fieldValue.toUpperCase());
                        }
                    } catch (IllegalAccessException e) {
                        excelInfo.setReason("多选题答案保存出错!");
                        excelInfoList.add(excelInfo);
                        return;
                    }
                }
                field.setAccessible(accessFlag);
            }
            for (Field field : fields) {
                // 获取原来的访问控制权限
                boolean accessFlag = field.isAccessible();
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                String fieldName = field.getName();
                if (ReUtil.isMatch("options[A-Z]?", fieldName)) {
                    try {
                        // 选项内容
                        String fieldValue = String.valueOf(field.get(excelInfo));
                        if (Help.isNotEmpty(fieldValue)) {
                            QuestionAnswerInfo answerInfo = new QuestionAnswerInfo();
                            answerInfo.setContent(fieldValue);
                            // 该选项默认为错误
                            answerInfo.setRighted(0);
                            answerList.forEach(answer -> {
                                if (fieldName.contains(answer)) {
                                    // 如果该选项名包含 大写的答案内容，则为正确答案
                                    answerInfo.setRighted(1);
                                }
                            });
                            questionAnswerInfoList.add(answerInfo);
                        }
                    } catch (IllegalAccessException e) {
                        excelInfo.setReason("多选题选项保存出错!");
                        excelInfoList.add(excelInfo);
                        return;
                    }
                }
                field.setAccessible(accessFlag);
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
