package com.curator.api.info.enums;

/**
 * 权限类型枚举
 *
 * @author Jun
 * @date 2021/4/16
 */
public enum InfoPowerTypeEnum {

    /**
     * 菜单
     *
     */
    MENU(1,"菜单"),

    /**
     * 按钮
     */
    BUTTON(2,"按钮"),

    /**
     * 目录
     */
    DIR(3,"目录");

    private int status;
    private String desc;


    InfoPowerTypeEnum(int status, String desc) {
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
        for(InfoPowerTypeEnum obj : InfoPowerTypeEnum.values()){
            if(obj.getStatus() == (status)){
                return obj.desc;
            }
        }
        return "";
    }
}
