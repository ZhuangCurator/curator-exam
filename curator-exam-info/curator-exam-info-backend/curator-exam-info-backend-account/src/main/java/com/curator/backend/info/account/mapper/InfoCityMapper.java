package com.curator.backend.info.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.curator.backend.info.account.entity.InfoCity;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  地市信息 Mapper 接口
 * </p>
 *
 * @author Jun
 * @since 2021-04-16
 */
public interface InfoCityMapper extends BaseMapper<InfoCity> {

    /**
     * 保存地市
     *
     * @param infoCity 地市
     */
    void saveInfoCity(@Param("infoCity") InfoCity infoCity);
}
