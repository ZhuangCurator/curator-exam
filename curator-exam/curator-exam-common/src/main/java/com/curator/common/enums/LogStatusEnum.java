package com.curator.common.enums;

/**
 * 日志状态枚举
 *
 * @author Jun
 * @date 2021/4/17
 */
public enum LogStatusEnum {

    /**
     * 正常
     *
     */
    NORMAL(1,"正常"),

    /**
     * 异常
     */
    EXCEPTION(2,"异常");

    private int status;
    private String desc;


    LogStatusEnum(int status, String desc) {
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
        for(LogStatusEnum obj : LogStatusEnum.values()){
            if(obj.getStatus() == (status)){
                return obj.desc;
            }
        }
        return "";
    }
}
