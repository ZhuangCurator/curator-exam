package com.curator.backend.paper.generation.rule.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.curator.backend.paper.generation.rule.entity.PaperGenerationRule;
import com.curator.backend.paper.generation.rule.entity.dto.PaperGenerationRuleDTO;
import com.curator.backend.paper.generation.rule.entity.vo.info.PaperGenerationRuleInfo;
import com.curator.backend.paper.generation.rule.entity.vo.search.PaperGenerationRuleSearch;
import com.curator.backend.paper.generation.rule.mapper.PaperGenerationRuleMapper;
import com.curator.backend.paper.generation.rule.service.PaperGenerationRuleService;
import com.curator.common.constant.CommonConstant;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import com.curator.common.util.ServletUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 试卷生成规则 服务实现类
 *
 * @author Jun
 * @since 2021-05-08
 */
@Service
public class PaperGenerationRuleServiceImpl implements PaperGenerationRuleService {
    
    @Autowired
    private PaperGenerationRuleMapper generationRuleMapper;

    @Override
    public ResultResponse<PageResult<PaperGenerationRuleDTO>> pageWithPaperGenerationRule(PaperGenerationRuleSearch search) {
        Page<PaperGenerationRule> page = new Page<>(search.getCurrent(), search.getPageSize());
        QueryWrapper<PaperGenerationRule> wrapper = new QueryWrapper<>();
        if (Boolean.FALSE.equals(search.getSuperAdmin())) {
            String createAccountId = ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID);
            wrapper.and(wr -> wr.eq("create_account_id", createAccountId)
                    .or(w -> w.eq("parent_account_id", createAccountId)));
        }
        wrapper.like(Help.isNotEmpty(search.getGenerationRuleName()), "generation_rule_name", search.getGenerationRuleName())
                .orderByDesc("create_time");
        IPage<PaperGenerationRule> iPage = generationRuleMapper.selectPage(page, wrapper);
        List<PaperGenerationRuleDTO> resultList = iPage.getRecords().stream()
                .map(this::convertEntity).collect(Collectors.toList());

        PageResult<PaperGenerationRuleDTO> resultPage = new PageResult<>();
        BeanUtils.copyProperties(iPage, resultPage, "records");
        resultPage.setRecords(resultList);

        return ResultResponse.<PageResult<PaperGenerationRuleDTO>>builder().success("试卷生成规则分页查询成功").data(resultPage).build();
    }

    @Override
    public ResultResponse<List<PaperGenerationRuleDTO>> listWithPaperGenerationRule(PaperGenerationRuleSearch search) {
        QueryWrapper<PaperGenerationRule> wrapper = new QueryWrapper<>();
        wrapper.like(Help.isNotEmpty(search.getGenerationRuleName()), "generation_rule_name", search.getGenerationRuleName())
                .orderByDesc("create_time");
        if (Boolean.FALSE.equals(search.getSuperAdmin())) {
            String createAccountId = ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID);
            wrapper.and(wr -> wr.eq("create_account_id", createAccountId)
                    .or(w -> w.eq("parent_account_id", createAccountId)));
        }
        List<PaperGenerationRule> list = generationRuleMapper.selectList(wrapper);
        List<PaperGenerationRuleDTO> resultList = list.stream().map(this::convertEntity).collect(Collectors.toList());

        return ResultResponse.<List<PaperGenerationRuleDTO>>builder().success("试卷生成规则列表查询成功").data(resultList).build();
    }

    @Override
    public ResultResponse<PaperGenerationRuleDTO> getPaperGenerationRule(String id) {
        PaperGenerationRule entity = generationRuleMapper.selectById(id);
        return ResultResponse.<PaperGenerationRuleDTO>builder().success("试卷生成规则查询成功").data(convertEntity(entity)).build();
    }
    
    @Override
    public ResultResponse<PaperGenerationRuleDTO> savePaperGenerationRule(PaperGenerationRuleInfo info) {
        PaperGenerationRule entity = convertInfo(info);
        entity.setCreateAccountId(ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID));
        entity.setParentAccountId(ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_PARENT_ID));
        generationRuleMapper.insert(entity);
        return ResultResponse.<PaperGenerationRuleDTO>builder().success("试卷生成规则添加成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<PaperGenerationRuleDTO> putPaperGenerationRule(PaperGenerationRuleInfo info) {
        PaperGenerationRule entity = convertInfo(info);
        generationRuleMapper.update(entity, new UpdateWrapper<PaperGenerationRule>().eq("generation_rule_id", info.getGenerationRuleId()));
        return ResultResponse.<PaperGenerationRuleDTO>builder().success("试卷生成规则更新成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<String> removePaperGenerationRule(String id) {
        generationRuleMapper.deleteById(id);
        return ResultResponse.<String>builder().success("试卷生成规则删除成功").data(id).build();
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

    /**
     * 将 页面信息 转为 数据库对象
     *
     * @param info 页面信息
     * @return 数据库对应实体类
     */
    private PaperGenerationRule convertInfo(PaperGenerationRuleInfo info) {
        PaperGenerationRule target = new PaperGenerationRule();
        if (Help.isNotEmpty(info)) {
            BeanUtils.copyProperties(info, target);
        }
        return target;
    }
}
