package com.curator.backend.info.account.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 *  地市信息
 * </p>
 *
 * @author Jun
 * @since 2021-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class InfoCity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 地市行政编码
     */
    @TableId(value = "code")
    private String code;

    /**
     * 地市名称
     */
    private String name;

}
