package com.curator.backend.register.exam.site.mapper;

import com.curator.backend.register.exam.site.entity.ExamSite;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 考点 Mapper 接口
 *
 * @author Jun
 * @since 2021-05-13
 */
public interface ExamSiteMapper extends BaseMapper<ExamSite> {

    /**
     * 查询当前地区下考点的最大序列号
     *
     * @param district 区(代码)
     * @return
     */
    Integer selectMaxSerialNum(@Param("district") String district);
}
