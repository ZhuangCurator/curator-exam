package com.curator.backend.info.power.entity.vo.info;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 权限组
 * </p>
 *
 * @author Jun
 * @since 2021-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class InfoPowerGroupInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 权限组主键
     */
    private String powerGroupId;

    /**
     * 权限组名
     */
    private String powerGroupName;

    /**
     * 备注
     */
    private String remark;

}
