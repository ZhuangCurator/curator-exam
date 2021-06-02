package com.curator.backend.info.role.mapper;

import com.curator.backend.info.role.entity.InfoRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  角色信息 Mapper 接口
 * </p>
 *
 * @author Jun
 * @since 2021-04-16
 */
public interface InfoRoleMapper extends BaseMapper<InfoRole> {

    /**
     * 查询账号的角色类型
     *
     * @param accountId 账号ID
     * @return
     */
    Integer getAccountRoleType(String accountId);
}
