package com.curator.core.info.city.provider;

import com.curator.api.info.provider.InfoCityProvider;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import com.curator.core.info.city.entity.InfoCity;
import com.curator.core.info.city.mapper.InfoCityMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Jun
 * @date 2021/6/6
 */
@DubboService
public class InfoCityProviderImpl implements InfoCityProvider {

    @Autowired
    private InfoCityMapper cityMapper;

    @Override
    public ResultResponse<String> getCityName(String code) {
        String name = "";
        InfoCity entity = cityMapper.selectById(code);
        if (Help.isNotEmpty(entity)) {
            name = entity.getName();
        }
        return ResultResponse.<String>builder().success("地市名称查询成功！").data(name).build();
    }

    @Override
    public ResultResponse<String> saveInfoCity(String code, String name) {
        InfoCity entity = cityMapper.selectById(code);
        if (Help.isEmpty(entity)) {
            entity = new InfoCity();
            entity.setCode(code);
            entity.setName(name);
            cityMapper.insert(entity);
        }
        return ResultResponse.<String>builder().success("地市信息保存成功！").data(code).build();
    }
}
