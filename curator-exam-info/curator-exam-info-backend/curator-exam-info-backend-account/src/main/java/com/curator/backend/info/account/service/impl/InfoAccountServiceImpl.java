package com.curator.backend.info.account.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.curator.api.info.enums.InfoAccountStatusEnum;
import com.curator.backend.info.account.entity.InfoAccount;
import com.curator.backend.info.account.entity.dto.InfoAccountDTO;
import com.curator.backend.info.account.entity.vo.info.InfoAccountInfo;
import com.curator.backend.info.account.entity.vo.search.InfoAccountSearch;
import com.curator.backend.info.account.mapper.InfoAccountMapper;
import com.curator.backend.info.account.service.InfoAccountService;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import com.curator.common.util.SecurityUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 账户信息 服务实现类
 * </p>
 *
 * @author Jun
 * @since 2021-04-16
 */
@Service
public class InfoAccountServiceImpl implements InfoAccountService {

    @Autowired
    private InfoAccountMapper accountMapper;

    @Override
    public ResultResponse<PageResult<InfoAccountDTO>> pageWithInfoAccount(InfoAccountSearch search) {
        Page<InfoAccount> page = new Page<>(search.getCurrent(), search.getPageSize());
        QueryWrapper<InfoAccount> wrapper = new QueryWrapper<>();
        wrapper.like(Help.isNotEmpty(search.getAccountName()), "account_name", search.getAccountName())
                .eq(Help.isNotEmpty(search.getAccountStatus()), "account_status", search.getAccountStatus())
                .orderByDesc("create_time");
        if (Boolean.FALSE.equals(search.getSuperAdmin()) && Help.isNotEmpty(search.getCreateAccountId())) {
            wrapper.and(wr -> wr.eq("create_account_id", search.getCreateAccountId())
                    .or(w -> w.eq("parent_account_id", search.getCreateAccountId())));
        }
        IPage<InfoAccount> iPage = accountMapper.selectPage(page, wrapper);
        List<InfoAccountDTO> resultList = iPage.getRecords().stream()
                .map(this::convertEntity).collect(Collectors.toList());

        PageResult<InfoAccountDTO> resultPage = new PageResult<>();
        BeanUtils.copyProperties(iPage, resultPage, "records");
        resultPage.setRecords(resultList);

        return ResultResponse.<PageResult<InfoAccountDTO>>builder().success("账户分页查询成功").data(resultPage).build();
    }

    @Override
    public ResultResponse<List<InfoAccountDTO>> listWithInfoAccount(InfoAccountSearch search) {

        QueryWrapper<InfoAccount> wrapper = new QueryWrapper<>();
        wrapper.like(Help.isNotEmpty(search.getAccountName()), "account_name", search.getAccountName())
                .eq("account_status", InfoAccountStatusEnum.NORMAL.getStatus())
                .orderByDesc("create_time");
        if (Boolean.FALSE.equals(search.getSuperAdmin()) && Help.isNotEmpty(search.getCreateAccountId())) {
            wrapper.and(wr -> wr.eq("create_account_id", search.getCreateAccountId())
                    .or(w -> w.eq("parent_account_id", search.getCreateAccountId())));
        }
        List<InfoAccount> list = accountMapper.selectList(wrapper);
        List<InfoAccountDTO> resultList = list.stream().map(this::convertEntity).collect(Collectors.toList());

        return ResultResponse.<List<InfoAccountDTO>>builder().success("账户列表查询成功").data(resultList).build();
    }

    @Override
    public ResultResponse<InfoAccountDTO> getInfoAccount(String infoAccountId) {
        InfoAccount entity = accountMapper.selectById(infoAccountId);
        return ResultResponse.<InfoAccountDTO>builder().success("账户查询成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<InfoAccountDTO> saveInfoAccount(InfoAccountInfo info) {
        InfoAccount entity = convertInfo(info);
        // 判断账户名是否重复
        QueryWrapper<InfoAccount> wrapper = new QueryWrapper<>();
        wrapper.eq("account_name", entity.getAccountName());
        InfoAccount infoAccount = accountMapper.selectOne(wrapper);
        if (Help.isNotEmpty(infoAccount)) {
            // 数据库已存在该账户名
            return ResultResponse.<InfoAccountDTO>builder().failure("该账户名已被使用!").build();
        }
        String salt = RandomUtil.randomString(RandomUtil.randomInt(10, 15));
        String encryptPassword = SecurityUtil.encryptPassword(entity.getAccountPassword(), salt);
        entity.setAccountPassword(encryptPassword);
        entity.setSalt(salt);
        // 新用户状态为正常
        entity.setAccountStatus(InfoAccountStatusEnum.NORMAL.getStatus());
        accountMapper.insert(entity);
        return ResultResponse.<InfoAccountDTO>builder().success("账户添加成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<InfoAccountDTO> putInfoAccount(InfoAccountInfo info) {
        InfoAccount entity = convertInfo(info);
        // 判断账户名是否重复
        QueryWrapper<InfoAccount> wrapper = new QueryWrapper<>();
        wrapper.eq("account_name", entity.getAccountName())
                .ne("account_id", entity.getAccountId());
        InfoAccount infoAccount = accountMapper.selectOne(wrapper);
        if (Help.isNotEmpty(infoAccount)) {
            // 数据库已存在该账户名
            return ResultResponse.<InfoAccountDTO>builder().failure("该账户名已被使用!").build();
        }
        accountMapper.update(entity, new UpdateWrapper<InfoAccount>().eq("account_id", info.getAccountId()));
        return ResultResponse.<InfoAccountDTO>builder().success("账户更新成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<String> removeInfoAccount(String infoAccountId) {
        accountMapper.deleteById(infoAccountId);
        return ResultResponse.<String>builder().success("账户删除成功").data(infoAccountId).build();
    }

    /**
     * 将 {@link InfoAccount} 转为 {@link InfoAccountDTO}
     *
     * @param entity 账户数据库信息
     * @return {@link InfoAccountDTO}
     */
    private InfoAccountDTO convertEntity(InfoAccount entity) {
        InfoAccountDTO target = new InfoAccountDTO();
        if (Help.isNotEmpty(entity)) {
            BeanUtils.copyProperties(entity, target);
        }
        return target;
    }

    /**
     * 将 {@link InfoAccountInfo} 转为 {@link InfoAccount}
     *
     * @param info 账户 页面信息
     * @return {@link InfoAccount}
     */
    private InfoAccount convertInfo(InfoAccountInfo info) {
        InfoAccount target = new InfoAccount();
        if (Help.isNotEmpty(info)) {
            BeanUtils.copyProperties(info, target);
        }
        return target;
    }
}
