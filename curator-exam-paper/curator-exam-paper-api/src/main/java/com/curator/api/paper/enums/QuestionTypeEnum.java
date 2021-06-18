package com.curator.api.paper.enums;

/**
 * 试题类型枚举
 *
 * @author Jun
 * @date 2021/4/16
 */
public enum QuestionTypeEnum {

    /**
     * 单选
     *
     */
    SINGLE_CHOICE(1,"单选题"),

    /**
     * 多选
     */
    MULTIPLE_CHOICE(2,"多选题"),

    /**
     * 判断
     */
    JUDGMENT(3,"判断题"),

    /**
     * 填空
     */
    FILL_BLANK(4,"填空题");

    private int status;
    private String desc;


    QuestionTypeEnum(int status, String desc) {
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
        for(QuestionTypeEnum obj : QuestionTypeEnum.values()){
            if(obj.getStatus() == (status)){
                return obj.desc;
            }
        }
        return "";
    }
}
