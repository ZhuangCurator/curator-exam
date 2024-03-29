package com.curator.api.info.enums;

/**
 * 权限状态枚举
 *
 * @author Jun
 * @date 2021/4/16
 */
public enum InfoPowerStatusEnum {

    /**
     * 启用
     *
     */
    ENABLE(1,"启用"),

    /**
     * 停用
     */
    UN_ENABLE(2,"停用");

    private int status;
    private String desc;


    InfoPowerStatusEnum(int status, String desc) {
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
        for(InfoPowerStatusEnum obj : InfoPowerStatusEnum.values()){
            if(obj.getStatus() == (status)){
                return obj.desc;
            }
        }
        return "";
    }
}
