package com.curator.api.log.enums;

/**
 * @author Jun
 * @date 2021/4/17
 */
public enum RequestLogStatusEnum {

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


    RequestLogStatusEnum(int status, String desc) {
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
        for(RequestLogStatusEnum obj : RequestLogStatusEnum.values()){
            if(obj.getStatus() == (status)){
                return obj.desc;
            }
        }
        return "";
    }
}
