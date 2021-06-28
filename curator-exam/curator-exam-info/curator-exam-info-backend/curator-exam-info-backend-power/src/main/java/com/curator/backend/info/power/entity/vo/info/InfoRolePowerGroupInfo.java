package com.curator.backend.info.power.entity.vo.info;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 角色与权限组关联
 * </p>
 *
 * @author Jun
 * @since 2021-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class InfoRolePowerGroupInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 权限组id
     */
    private String powerGroupId;

    /**
     * 权限组id集合
     */
    private List<String> powerGroupIdList;

}
