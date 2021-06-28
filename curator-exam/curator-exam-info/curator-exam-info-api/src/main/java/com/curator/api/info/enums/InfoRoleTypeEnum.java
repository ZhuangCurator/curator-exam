package com.curator.api.info.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 角色类型枚举
 *
 * @author Jun
 * @date 2021/4/16
 */
public enum InfoRoleTypeEnum {

    /**
     * 超级管理员
     *
     */
    SUPER_ADMIN(1,"超级管理员"),

    /**
     * 超级管理员子账号
     */
    SUPER_ADMIN_SON(2,"超级管理员子账号"),

    /**
     * 省级管理员
     */
    PROVINCE_ADMIN(3,"省级管理员"),

    /**
     * 省级管理员子账号
     */
    PROVINCE_ADMIN_SON(4,"省级管理员子账号"),

    /**
     * 市级管理员
     */
    CITY_ADMIN(5,"市级管理员"),

    /**
     * 市级管理员子账号
     */
    CITY_ADMIN_SON(6,"市级管理员子账号");

    private int status;
    private String desc;


    InfoRoleTypeEnum(int status, String desc) {
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
        for(InfoRoleTypeEnum obj : InfoRoleTypeEnum.values()){
            if(obj.getStatus() == (status)){
                return obj.desc;
            }
        }
        return "";
    }

    public static List<HashMap<String, String>> getRoleTypeList() {
        List<HashMap<String, String>> list = new ArrayList<>();
        for(InfoRoleTypeEnum obj : InfoRoleTypeEnum.values()){
            HashMap<String, String> map = new HashMap<>();
            map.put("status", String.valueOf(obj.getStatus()));
            map.put("desc", obj.getDesc());
            list.add(map);
        }
        return list;
    }
}
