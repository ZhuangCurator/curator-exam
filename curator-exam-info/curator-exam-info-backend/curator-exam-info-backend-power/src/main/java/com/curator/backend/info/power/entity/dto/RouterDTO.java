package com.curator.backend.info.power.entity.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 路由对象
 *
 * @author Jun
 * @date 2021/4/29
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RouterDTO implements Serializable {

    /**
     * 路由名字
     */
    private String name;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 组件地址
     */
    private String component;

    /**
     * 其他元素
     */
    private Meta meta;

    /**
     * 子路由
     */
    private List<RouterDTO> children;

    public static class Meta implements Serializable {
        /**
         * 设置该路由在侧边栏展示的名字
         */
        private String title;

        /**
         * 设置该路由的图标
         */
        private String icon;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public Meta(String title, String icon) {
            this.title = title;
            this.icon = icon;
        }
    }
}
