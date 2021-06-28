package com.curator.core.paper.provider;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.curator.api.paper.pojo.dto.PaperGenerationRuleDTO;
import com.curator.api.paper.provider.PaperGenerationRuleProvider;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import com.curator.core.paper.entity.PaperGenerationRule;
import com.curator.core.paper.mapper.PaperGenerationRuleMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 试卷生成规则 RPC接口实现类
 *
 * @author Jun
 * @date 2021/5/14
 */
@DubboService
public class PaperGenerationRuleProviderImpl implements PaperGenerationRuleProvider {

    @Autowired
    private PaperGenerationRuleMapper generationRuleMapper;

    @Override
    public ResultResponse<List<PaperGenerationRuleDTO>> listWithPaperGenerationRule(String ruleName) {
        QueryWrapper<PaperGenerationRule> wrapper = new QueryWrapper<>();
        wrapper.like(Help.isNotEmpty(ruleName), "generation_rule_name", ruleName)
                .orderByDesc("create_time");
        List<PaperGenerationRule> list = generationRuleMapper.selectList(wrapper);
        List<PaperGenerationRuleDTO> resultList = list.stream().map(this::convertEntity).collect(Collectors.toList());
        return ResultResponse.<List<PaperGenerationRuleDTO>>builder().success("试卷生成规则列表查询成功").data(resultList).build();
    }

    /**
     * 将 数据库对象 转为 数据传输对象
     *
     * @param entity 数据库信息
     * @return dto实体类
     */
    private PaperGenerationRuleDTO convertEntity(PaperGenerationRule entity) {
        PaperGenerationRuleDTO target = new PaperGenerationRuleDTO();
        if (Help.isNotEmpty(entity)) {
            BeanUtils.copyProperties(entity, target);
        }
        return target;
    }
}
