package com.curator.api.paper.pojo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 试卷中的试题 数据传输对象
 *
 * @author Jun
 * @date 2021/5/12
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PaperQuestionDTO implements Serializable {

    /**
     * 考生报考信息ID
     */
    private String examInfoId;

    /**
     * 试卷ID
     */
    private String testPaperId;

    /**
     * 试题ID
     */
    private String questionId;

    /**
     * 试题类型(1-单选，2-多选，3-判断，4-填空)
     */
    private Integer questionType;

    /**
     * 试题序号
     */
    private Integer questionSort;

    /**
     * 用户试题答案集合
     */
    private List<String> userAnswerList;

    /**
     * 试题答案集合
     */
    private List<PaperQuestionAnswer> questionAnswerList;

     public static class PaperQuestionAnswer {

        /**
         * 试题 答案内容
         */
        private String questionAnswerContent;

        public String getQuestionAnswerContent() {
            return questionAnswerContent;
        }

        public void setQuestionAnswerContent(String questionAnswerContent) {
            this.questionAnswerContent = questionAnswerContent;
        }
    }
}
