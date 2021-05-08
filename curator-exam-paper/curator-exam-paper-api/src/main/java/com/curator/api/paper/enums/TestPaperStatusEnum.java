package com.curator.api.paper.enums;

/**
 * 考生试卷状态枚举
 *
 * @author Jun
 * @date 2021/4/16
 */
public enum TestPaperStatusEnum {

    /**
     * 试卷未开考
     *
     */
    UN_STARTED(1,"试卷未开考"),

    /**
     * 考试进行中
     *
     */
    PROCESSING(2,"考试进行中"),

    /**
     * 考试需新卷重考
     *
     */
    NEW_PAPER_RETAKE(3,"考试需新卷重考"),

    /**
     * 考试需原卷重考
     *
     */
    OLD_PAPER_RETAKE(4,"考试需原卷重考"),

    /**
     * 试卷已废弃
     *
     */
    DISCARD(5,"试卷已废弃"),

    /**
     * 考试已结束
     */
    OVER(6,"考试已结束");

    private int status;
    private String desc;


    TestPaperStatusEnum(int status, String desc) {
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
        for(TestPaperStatusEnum obj : TestPaperStatusEnum.values()){
            if(obj.getStatus() == (status)){
                return obj.desc;
            }
        }
        return "";
    }
}
