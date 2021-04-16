package com.curator.api.info.enums;

/**
 * 账户状态枚举
 *
 * @author Jun
 * @date 2021/4/16
 */
public enum InfoAccountStatusEnum {

    /**
     * 正常
     *
     */
    NORMAL(1,"正常"),

    /**
     * 冻结
     */
    FREEZE(2,"冻结"),

    /**
     * 注销
     */
    LOGOUT(3,"注销");

    private int status;
    private String desc;


    InfoAccountStatusEnum(int status, String desc) {
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
        for(InfoAccountStatusEnum obj : InfoAccountStatusEnum.values()){
            if(obj.getStatus() == (status)){
                return obj.desc;
            }
        }
        return "";
    }
}
