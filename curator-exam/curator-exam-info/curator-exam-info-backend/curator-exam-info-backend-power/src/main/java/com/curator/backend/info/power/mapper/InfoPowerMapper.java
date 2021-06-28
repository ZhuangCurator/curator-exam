package com.curator.backend.info.power.mapper;

import com.curator.backend.info.power.entity.InfoPower;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * <p>
 *  权限信息 Mapper 接口
 * </p>
 *
 * @author Jun
 * @since 2021-04-16
 */
public interface InfoPowerMapper extends BaseMapper<InfoPower> {

    /**
     * 得到子级权限的id集合
     *
     * @param powerId 父权限id
     * @return
     */
    Set<String> getChildPowerIdSet(String powerId);

    /**
     * 批量更新权限的状态
     *
     * @param idSet 权限id集合
     * @param status 状态值
     * @param now 更新时间
     */
    void batchUpdatePowerStatus(@Param("idSet") Set<String> idSet, @Param("status") Integer status, @Param("now") LocalDateTime now);
}
