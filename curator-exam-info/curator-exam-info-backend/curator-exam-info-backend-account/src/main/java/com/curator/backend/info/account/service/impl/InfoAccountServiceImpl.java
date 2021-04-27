package com.curator.backend.info.account.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.curator.api.info.enums.InfoAccountStatusEnum;
import com.curator.api.info.enums.InfoRoleStatusEnum;
import com.curator.backend.info.account.entity.InfoAccount;
import com.curator.backend.info.account.entity.dto.InfoAccountDTO;
import com.curator.backend.info.account.entity.vo.info.InfoAccountInfo;
import com.curator.backend.info.account.entity.vo.search.InfoAccountSearch;
import com.curator.backend.info.account.mapper.InfoAccountMapper;
import com.curator.backend.info.account.service.InfoAccountService;
import com.curator.backend.info.role.entity.InfoRole;
import com.curator.backend.info.role.mapper.InfoRoleMapper;
import com.curator.common.constant.CommonConstant;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import com.curator.common.util.SecurityUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    private InfoRoleMapper roleMapper;

    @Override
    public ResultResponse<PageResult<InfoAccountDTO>> pageWithInfoAccount(InfoAccountSearch search, HttpServletRequest request) {
        Page<InfoAccount> page = new Page<>(search.getCurrent(), search.getPageSize());
        QueryWrapper<InfoAccount> wrapper = new QueryWrapper<>();
        wrapper.like(Help.isNotEmpty(search.getAccountName()), "account_name", search.getAccountName())
                .eq(Help.isNotEmpty(search.getAccountStatus()), "account_status", search.getAccountStatus())
                .orderByDesc("create_time");
        if (Boolean.FALSE.equals(search.getSuperAdmin())) {
            String createAccountId = request.getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID);
            wrapper.and(wr -> wr.eq("create_account_id", createAccountId)
                    .or(w -> w.eq("parent_account_id", createAccountId)));
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
    public ResultResponse<List<InfoAccountDTO>> listWithInfoAccount(InfoAccountSearch search, HttpServletRequest request) {

        QueryWrapper<InfoAccount> wrapper = new QueryWrapper<>();
        wrapper.like(Help.isNotEmpty(search.getAccountName()), "account_name", search.getAccountName())
                .eq("account_status", InfoAccountStatusEnum.NORMAL.getStatus())
                .orderByDesc("create_time");
        if (Boolean.FALSE.equals(search.getSuperAdmin())) {
            String createAccountId = request.getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID);
            wrapper.and(wr -> wr.eq("create_account_id", createAccountId)
                    .or(w -> w.eq("parent_account_id", createAccountId)));
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
    public ResultResponse<InfoAccountDTO> saveInfoAccount(InfoAccountInfo info, HttpServletRequest request) {
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
        entity.setParentId(request.getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID));
        entity.setCreateAccountId(request.getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID));
        entity.setParentAccountId(request.getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_PARENT_ID));
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

    @Override
    public ResultResponse<InfoAccountDTO> saveInfoSuperAdminAccount() {
        // 删除原有的超级管理员角色
        QueryWrapper<InfoRole> roleWrapper = new QueryWrapper<>();
        roleWrapper.eq("role_name", CommonConstant.DEFAULT_SUPER_ADMIN_ROLE);
        roleMapper.delete(roleWrapper);
        // 插入
        InfoRole infoRole = new InfoRole();
        infoRole.setRoleName(CommonConstant.DEFAULT_SUPER_ADMIN_ROLE);
        infoRole.setRoleRemark("默认的超级管理员角色");
        infoRole.setCreateAccountId("0");
        infoRole.setParentAccountId("0");
        infoRole.setRoleStatus(InfoRoleStatusEnum.ENABLE.getStatus());
        roleMapper.insert(infoRole);

        // 删除原有的超级管理员
        QueryWrapper<InfoAccount> accountWrapper = new QueryWrapper<>();
        accountWrapper.eq("account_name", CommonConstant.DEFAULT_SUPER_ADMIN);
        accountMapper.delete(accountWrapper);
        // 插入
        InfoAccount infoAccount = new InfoAccount();
        infoAccount.setAccountName(CommonConstant.DEFAULT_SUPER_ADMIN);
        String salt = RandomUtil.randomString(RandomUtil.randomInt(10, 15));
        String encryptPassword = SecurityUtil.encryptPassword("123456", salt);
        infoAccount.setAccountPassword(encryptPassword);
        infoAccount.setSalt(salt);
        infoAccount.setAccountStatus(InfoAccountStatusEnum.NORMAL.getStatus());
        infoAccount.setEmail("curator@qq.com");
        infoAccount.setPhone("123456");
        infoAccount.setParentId("0");
        infoAccount.setCreateAccountId("0");
        infoAccount.setParentAccountId("0");
        infoAccount.setRoleId(infoRole.getRoleId());
        accountMapper.insert(infoAccount);
        return ResultResponse.<InfoAccountDTO>builder().success("超级管理员创建成功").data(convertEntity(infoAccount)).build();
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
            InfoRole infoRole = roleMapper.selectById(entity.getRoleId());
            target.setRoleName(infoRole.getRoleName());
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
