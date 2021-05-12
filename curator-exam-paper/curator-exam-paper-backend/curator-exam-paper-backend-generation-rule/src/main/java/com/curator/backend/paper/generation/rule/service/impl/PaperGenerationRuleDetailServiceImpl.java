package com.curator.backend.paper.generation.rule.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.curator.backend.paper.generation.rule.entity.PaperGenerationRule;
import com.curator.backend.paper.generation.rule.entity.PaperGenerationRuleDetail;
import com.curator.backend.paper.generation.rule.entity.dto.PaperGenerationRuleDetailDTO;
import com.curator.backend.paper.generation.rule.entity.vo.info.PaperGenerationRuleDetailInfo;
import com.curator.backend.paper.generation.rule.mapper.PaperGenerationRuleDetailMapper;
import com.curator.backend.paper.generation.rule.mapper.PaperGenerationRuleMapper;
import com.curator.backend.paper.generation.rule.service.PaperGenerationRuleDetailService;
import com.curator.common.constant.CommonConstant;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import com.curator.common.util.ServletUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 试卷生成规则详情 服务实现类
 *
 * @author Jun
 * @since 2021-05-08
 */
@Service
public class PaperGenerationRuleDetailServiceImpl implements PaperGenerationRuleDetailService {

    @Autowired
    private PaperGenerationRuleDetailMapper generationRuleDetailMapper;
    @Autowired
    private PaperGenerationRuleMapper generationRuleMapper;

    @Override
    public ResultResponse<List<PaperGenerationRuleDetailDTO>> listWithPaperGenerationRuleDetail(String generationRuleId) {
        QueryWrapper<PaperGenerationRuleDetail> wrapper = new QueryWrapper<>();
        wrapper.eq("generation_rule_id", generationRuleId)
                .orderByDesc("create_time");
        List<PaperGenerationRuleDetail> list = generationRuleDetailMapper.selectList(wrapper);
        List<PaperGenerationRuleDetailDTO> resultList = list.stream().map(this::convertEntity).collect(Collectors.toList());

        return ResultResponse.<List<PaperGenerationRuleDetailDTO>>builder().success("试卷生成规则详情列表查询成功").data(resultList).build();

    }

    @Override
    public ResultResponse<PaperGenerationRuleDetailDTO> getPaperGenerationRuleDetail(String id) {
        PaperGenerationRuleDetail entity = generationRuleDetailMapper.selectById(id);
        return ResultResponse.<PaperGenerationRuleDetailDTO>builder().success("试卷生成规则详情查询成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<PaperGenerationRuleDetailDTO> savePaperGenerationRuleDetail(PaperGenerationRuleDetailInfo info) {
        PaperGenerationRuleDetail entity = convertInfo(info);
        entity.setCreateAccountId(ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID));
        entity.setParentAccountId(ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_PARENT_ID));
        generationRuleDetailMapper.insert(entity);
        // 同时更新试卷生成规则中的试卷总分
        updateTestPaperPoint(entity.getGenerationRuleId());
        return ResultResponse.<PaperGenerationRuleDetailDTO>builder().success("试卷生成规则详情添加成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<PaperGenerationRuleDetailDTO> putPaperGenerationRuleDetail(PaperGenerationRuleDetailInfo info) {
        PaperGenerationRuleDetail entity = convertInfo(info);
        generationRuleDetailMapper.update(entity, new UpdateWrapper<PaperGenerationRuleDetail>().eq("generation_rule_detail_id", info.getGenerationRuleDetailId()));
        // 同时更新试卷生成规则中的试卷总分
        updateTestPaperPoint(entity.getGenerationRuleId());
        return ResultResponse.<PaperGenerationRuleDetailDTO>builder().success("试卷生成规则详情更新成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<String> removePaperGenerationRuleDetail(String id) {
        PaperGenerationRuleDetail detail = generationRuleDetailMapper.selectById(id);
        generationRuleDetailMapper.deleteById(id);
        // 同时更新试卷生成规则中的试卷总分
        updateTestPaperPoint(detail.getGenerationRuleId());
        return ResultResponse.<String>builder().success("试卷生成规则详情删除成功").data(id).build();
    }

    /**
     * 同时更新试卷生成规则中的试卷总分
     *
     * @param generationRuleId 试卷生成规则id
     */
    private void updateTestPaperPoint(String generationRuleId) {
        QueryWrapper<PaperGenerationRuleDetail> detailQueryWrapper = new QueryWrapper<>();
        detailQueryWrapper.eq("generation_rule_id", generationRuleId);
        List<PaperGenerationRuleDetail> detailList = generationRuleDetailMapper.selectList(detailQueryWrapper);
        Integer testPaperPoint = detailList.stream().mapToInt(detail -> detail.getQuestionNumber() * detail.getQuestionPoint()).sum();
        PaperGenerationRule rule = new PaperGenerationRule();
        rule.setGenerationRuleId(generationRuleId);
        rule.setTestPaperPoint(testPaperPoint);
        generationRuleMapper.updateById(rule);
    }

    /**
     * 将 数据库对象 转为 数据传输对象
     *
     * @param entity 数据库信息
     * @return dto实体类
     */
    private PaperGenerationRuleDetailDTO convertEntity(PaperGenerationRuleDetail entity) {
        PaperGenerationRuleDetailDTO target = new PaperGenerationRuleDetailDTO();
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
    private PaperGenerationRuleDetail convertInfo(PaperGenerationRuleDetailInfo info) {
        PaperGenerationRuleDetail target = new PaperGenerationRuleDetail();
        if (Help.isNotEmpty(info)) {
            BeanUtils.copyProperties(info, target);
        }
        return target;
    }
}
