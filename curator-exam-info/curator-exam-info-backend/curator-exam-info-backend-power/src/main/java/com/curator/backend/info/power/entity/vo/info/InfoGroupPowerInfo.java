package com.curator.backend.info.power.entity.vo.info;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 权限组与权限关联信息
 * </p>
 *
 * @author Jun
 * @since 2021-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class InfoGroupPowerInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 权限组ID
     */
    private String powerGroupId;

    /**
     * 权限ID
     */
    private String powerId;

    /**
     * 权限集合
     */
    private List<String> powerIdList;




}
