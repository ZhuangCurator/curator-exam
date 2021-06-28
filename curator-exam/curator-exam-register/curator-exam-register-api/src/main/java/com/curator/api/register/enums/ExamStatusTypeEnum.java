package com.curator.api.register.enums;

/**
 * 考试状态类型枚举
 *
 * @author Jun
 * @date 2021/4/16
 */
public enum ExamStatusTypeEnum {

    /**
     * 未开始考试 考试状态(0:未开始考试,1:已结束考试,2:缺考)
     *
     */
    UN_STARTED(0,"未开始考试"),

    /**
     * 已结束考试
     */
    ALREADY_OVER(1,"已结束考试"),

    /**
     * 缺考
     */
    MISSED_EXAM(2,"缺考");

    private int status;
    private String desc;


    ExamStatusTypeEnum(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static String getDesc(int status){
        for(ExamStatusTypeEnum obj : ExamStatusTypeEnum.values()){
            if(obj.getStatus() == (status)){
                return obj.desc;
            }
        }
        return "";
    }
}
