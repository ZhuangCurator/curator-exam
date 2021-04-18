package com.curator.api.info.enums;

/**
 * 角色状态枚举
 *
 * @author Jun
 * @date 2021/4/16
 */
public enum InfoRoleStatusEnum {

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


    InfoRoleStatusEnum(int status, String desc) {
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
        for(InfoRoleStatusEnum obj : InfoRoleStatusEnum.values()){
            if(obj.getStatus() == (status)){
                return obj.desc;
            }
        }
        return "";
    }
}
