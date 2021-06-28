package com.curator.api.paper.enums;

/**
 * 交卷方式类型枚举
 *
 * @author Jun
 * @date 2021/4/16
 */
public enum HandInTypeEnum {

    /**
     * 主动交卷
     *
     */
    INITIATIVE_HAND_IN(1,"主动交卷"),

    /**
     * 考试时间到,自动交卷
     */
    TIME_IS_UP(2,"考试时间到,自动交卷"),

    /**
     * 作弊次数到,自动交卷
     */
    CHEAT_IS_UP(3,"作弊次数到,自动交卷");

    private int status;
    private String desc;


    HandInTypeEnum(int status, String desc) {
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
        for(HandInTypeEnum obj : HandInTypeEnum.values()){
            if(obj.getStatus() == (status)){
                return obj.desc;
            }
        }
        return "";
    }
}
