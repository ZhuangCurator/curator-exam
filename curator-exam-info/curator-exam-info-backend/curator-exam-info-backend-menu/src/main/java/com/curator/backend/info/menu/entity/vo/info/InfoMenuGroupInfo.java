package com.curator.backend.info.menu.entity.vo.info;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 菜单组
 * </p>
 *
 * @author Jun
 * @since 2021-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class InfoMenuGroupInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单组主键
     */
    private String menuGroupId;

    /**
     * 菜单组名
     */
    private String menuGroupName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建账户 id
     */
    private String createAccountId;

    /**
     * 创建账户父账户 id
     */
    private String parentAccountId;

}
