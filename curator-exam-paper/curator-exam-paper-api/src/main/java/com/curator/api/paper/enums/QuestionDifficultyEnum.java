package com.curator.api.paper.enums;

/**
 * 试题难度枚举
 *
 * @author Jun
 * @date 2021/4/16
 */
public enum QuestionDifficultyEnum {

    /**
     * 初级
     *
     */
    PRIMARY(1,"初级"),

    /**
     * 中级
     */
    MIDDLE(2,"中级"),

    /**
     * 高级
     */
    HIGH(3,"高级");

    private int status;
    private String desc;


    QuestionDifficultyEnum(int status, String desc) {
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
        for(QuestionDifficultyEnum obj : QuestionDifficultyEnum.values()){
            if(obj.getStatus() == (status)){
                return obj.desc;
            }
        }
        return "";
    }
}
