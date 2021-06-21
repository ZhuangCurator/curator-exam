package com.curator.backend.info.account.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.curator.api.info.enums.InfoAccountStatusEnum;
import com.curator.api.info.enums.InfoRoleStatusEnum;
import com.curator.api.info.enums.InfoRoleTypeEnum;
import com.curator.api.info.provider.InfoCityProvider;
import com.curator.backend.info.account.entity.InfoAccount;
import com.curator.backend.info.account.entity.InfoCity;
import com.curator.backend.info.account.entity.dto.InfoAccountDTO;
import com.curator.backend.info.account.entity.vo.info.InfoAccountInfo;
import com.curator.backend.info.account.entity.vo.search.InfoAccountSearch;
import com.curator.backend.info.account.mapper.InfoAccountMapper;
import com.curator.backend.info.account.mapper.InfoCityMapper;
import com.curator.backend.info.account.service.InfoAccountService;
import com.curator.backend.info.role.entity.InfoRole;
import com.curator.backend.info.role.mapper.InfoRoleMapper;
import com.curator.common.constant.CommonConstant;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import com.curator.common.util.SecurityUtil;
import com.curator.common.util.ServletUtil;
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
    @Autowired
    private InfoRoleMapper roleMapper;
    @Autowired
    private InfoCityMapper infoCityMapper;

    @Override
    public ResultResponse<PageResult<InfoAccountDTO>> pageWithInfoAccount(InfoAccountSearch search) {
        Page<InfoAccount> page = new Page<>(search.getCurrent(), search.getPageSize());
        QueryWrapper<InfoAccount> wrapper = new QueryWrapper<>();
        wrapper.like(Help.isNotEmpty(search.getAccountName()), "account_name", search.getAccountName())
                .eq(Help.isNotEmpty(search.getAccountStatus()), "account_status", search.getAccountStatus())
                .isNull(search.getOrdinary().equals(Boolean.TRUE), "role_id")
                .isNotNull(search.getOrdinary().equals(Boolean.FALSE), "role_id")
                .orderByDesc("create_time");
        if (Boolean.FALSE.equals(search.getSuperAdmin())) {
            String createAccountId = ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID);
            if(search.getChild().equals(Boolean.TRUE)) {
                // 查询子账户
                wrapper.eq("parent_id", createAccountId);
            }else if(search.getOrdinary().equals(Boolean.FALSE)){
                // 查询管理员账户
                wrapper.isNotNull("role_id")
                        .and(wr -> wr.eq("create_account_id", createAccountId)
                        .or(w -> w.eq("parent_account_id", createAccountId)));
            }
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
        if (Boolean.FALSE.equals(search.getSuperAdmin())) {
            String createAccountId = ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID);
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
    public ResultResponse<InfoAccountDTO> saveInfoAccount(InfoAccountInfo info) {
        String createAccountId = ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID);
        InfoAccount account = accountMapper.selectById(createAccountId);
        if(account.getRoleType() == InfoRoleTypeEnum.CITY_ADMIN.getStatus() ||
                account.getRoleType() == InfoRoleTypeEnum.CITY_ADMIN_SON.getStatus() ||
                account.getRoleType() == InfoRoleTypeEnum.PROVINCE_ADMIN_SON.getStatus() ||
                account.getRoleType() == InfoRoleTypeEnum.SUPER_ADMIN_SON.getStatus()) {
            // 市级管理员和各级管理员子账号 不能创建下级管理员账号
            return ResultResponse.<InfoAccountDTO>builder().failure("当前账户级别不允许创建管理员账户!").build();
        }
        // 校验账户信息
        ResultResponse<InfoAccountDTO> res = checkInfoAccount(info);
        if (!res.getSucceeded()) {
            return res;
        }
        // 记录下地市信息
        saveInfoCity(info);
        InfoAccount entity = convertInfo(info);
        if(Help.isEmpty(entity.getRoleType())) {
            InfoRole role = roleMapper.selectById(entity.getRoleId());
            entity.setRoleType(role.getRoleType());
        }
        String salt = RandomUtil.randomString(RandomUtil.randomInt(10, 15));
        String encryptPassword = SecurityUtil.encryptPassword("123456", salt);
        entity.setAccountPassword(encryptPassword);
        entity.setSalt(salt);
        // 新用户状态为正常
        entity.setAccountStatus(InfoAccountStatusEnum.NORMAL.getStatus());
        entity.setCreateAccountId(ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID));
        entity.setParentAccountId(ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_PARENT_ID));
        accountMapper.insert(entity);
        return ResultResponse.<InfoAccountDTO>builder().success("账户添加成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<InfoAccountDTO> saveSonInfoAccount(InfoAccountInfo info) {
        // 校验账户信息
        ResultResponse<InfoAccountDTO> res = checkInfoAccount(info);
        if (!res.getSucceeded()) {
            return res;
        }
        InfoAccount entity = convertInfo(info);
        // 继承父账号的省市字段
        String parentId = ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID);
        InfoAccount parentAccount = accountMapper.selectById(parentId);
        entity.setProvince(parentAccount.getProvince());
        entity.setCity(parentAccount.getCity());
        // 新用户状态为正常
        entity.setAccountStatus(InfoAccountStatusEnum.NORMAL.getStatus());
        entity.setParentId(parentId);
        entity.setCreateAccountId(parentId);
        entity.setParentAccountId(ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_PARENT_ID));
        // 加密盐
        String salt = RandomUtil.randomString(RandomUtil.randomInt(10, 15));
        String encryptPassword = SecurityUtil.encryptPassword("123456", salt);
        entity.setSalt(salt);
        entity.setAccountPassword(encryptPassword);

        accountMapper.insert(entity);
        return ResultResponse.<InfoAccountDTO>builder().success("子账户添加成功").data(convertEntity(entity)).build();
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
        infoAccount.setRemark("超级管理员账号");
        infoAccount.setCreateAccountId("0");
        infoAccount.setParentAccountId("0");
        infoAccount.setRoleId(infoRole.getRoleId());
        accountMapper.insert(infoAccount);
        return ResultResponse.<InfoAccountDTO>builder().success("超级管理员创建成功").data(convertEntity(infoAccount)).build();
    }

    /**
     * 检查账户信息
     *
     * @param info 账户页面信息
     * @return
     */
    private ResultResponse<InfoAccountDTO> checkInfoAccount(InfoAccountInfo info) {
        if(Help.isEmpty(info.getRoleId())) {
            return ResultResponse.<InfoAccountDTO>builder().failure("请选择角色!").build();
        }
        // 判断账户名是否重复
        QueryWrapper<InfoAccount> wrapper = new QueryWrapper<>();
        wrapper.eq("account_name", info.getAccountName());
        InfoAccount infoAccount = accountMapper.selectOne(wrapper);
        if (Help.isNotEmpty(infoAccount)) {
            // 数据库已存在该账户名
            return ResultResponse.<InfoAccountDTO>builder().failure("该账户名已被使用!").build();
        }

        return ResultResponse.<InfoAccountDTO>builder().success().build();
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
            if(Help.isNotEmpty(entity.getRoleId())) {
                InfoRole infoRole = roleMapper.selectById(entity.getRoleId());
                if (Help.isEmpty(infoRole)) {
                    target.setRoleName("该角色已被删除");
                } else {
                    target.setRoleName(infoRole.getRoleName());
                }
            } else {
                target.setRoleName("该账户没有分配角色");
            }
            InfoAccount createAccount = accountMapper.selectById(entity.getCreateAccountId());
            if(Help.isNotEmpty(createAccount)) {
                target.setCreateAccountName(createAccount.getAccountName());
            }
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

    private void saveInfoCity(InfoAccountInfo info) {
        InfoCity entity;
        if(Help.isNotEmpty(info.getProvince())) {
            entity = new InfoCity();
            entity.setCode(info.getProvince());
            entity.setName(info.getProvinceName());
            infoCityMapper.saveInfoCity(entity);
        }
        if(Help.isNotEmpty(info.getCity())) {
            entity = new InfoCity();
            entity.setCode(info.getCity());
            entity.setName(info.getCityName());
            infoCityMapper.saveInfoCity(entity);
        }
    }
}
