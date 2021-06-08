<template>
  <div class="user_container">
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>普通账户管理</el-breadcrumb-item>
      <el-breadcrumb-item>普通账户列表</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图区域-->
    <el-card class="box-card">
      <!--  搜索与添加区域 -->
      <el-form :model="queryForm" ref="queryFormRef" :inline="true" label-width="80px">
        <el-form-item label="普通账户名称" label-width="100px">
          <el-input
            v-model="queryForm.accountName"
            placeholder="请输入账户名称"
            clearable
            size="small"
            style="width: 240px"
            @keyup.enter.native="getAccountPage"
          />
        </el-form-item>
        <el-form-item label="账户状态">
          <el-select v-model="queryForm.status" placeholder="请选择账户状态" size="small">
            <el-option :key="1" label="正常" :value="1"></el-option>
            <el-option :key="2" label="冻结" :value="2"></el-option>
            <el-option :key="3" label="注销" :value="3"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="info" icon="el-icon-search" size="small" @click="getAccountPage">搜索</el-button>
          <el-button type="warning" icon="el-icon-refresh" size="small" @click="resetQueryForm">重置</el-button>
        </el-form-item>
      </el-form>

      <!--  账户列表区域 -->
      <el-table :data="accountList" border stripe>
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-form label-position="left" class="table-expand">
              <el-form-item label="创建时间">
                <span>{{ props.row.createTime }}</span>
              </el-form-item>
              <el-form-item label="更新时间">
                <span>{{ props.row.updateTime }}</span>
              </el-form-item>
              <el-form-item label="状态">
                <span>{{ props.row.accountStatusDesc }}</span>
              </el-form-item>
              <el-form-item label="备注">
                <span>{{ props.row.remark }}</span>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column label="账户名" prop="accountName" align="center"></el-table-column>
        <el-table-column label="邮箱" prop="email" align="center"></el-table-column>
        <el-table-column label="电话" prop="phone" align="center"></el-table-column>
        <el-table-column label="操作" width="300px;" align="center" v-if="columnShow">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" size="mini" v-has-perm="['system:account:update']" @click="showEditDialog(scope.row.accountId)">编辑
            </el-button>
            <el-button type="danger" icon="el-icon-delete" size="mini" v-has-perm="['system:account:deleted']" @click="deleteAccount(scope.row.accountId)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页区域 -->
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryForm.current"
        :page-sizes="[1, 2, 20, 50]"
        :page-size="queryForm.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </el-card>

    <!-- 修改账户对话框 -->
    <el-dialog title="编辑普通账户" :visible.sync="editDialogVisible" width="30%" @close="handleEditDialogClose">
      <!-- 对话框主题区域 -->
      <el-form ref="editFormRef" :model="editForm" :rules="editFormRules" label-width="100px">
        <el-form-item label="普通账户名" prop="accountName">
          <el-input v-model="editForm.accountName" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="editForm.email"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="editForm.phone"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="editForm.remark"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部按钮区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="editAccount">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  handleUpdateAccount,
  handleAccountQuery,
  handleAccountPage,
  handleDeleteAccount
} from '@/apis/info/account'
import { showElement } from '@/utils/show'
import { getSuperAdmin } from '@/utils/storage'
export default {
  name: 'AccountPage',
  data () {
    // 自定义邮箱校验规则
    const checkEmail = (rule, value, callback) => {
      const regEmail = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/
      if (regEmail.test(value)) {
        return callback()
      }
      callback(new Error('请输入合法的邮箱账号'))
    }

    // 自定义手机号码校验规则
    const checkPhone = (rule, value, callback) => {
      const regEmail = /^1[3-9]\d{9}$/
      if (regEmail.test(value)) {
        return callback()
      }
      callback(new Error('请输入合法的手机号'))
    }
    return {
      columnShow: true,
      // 是否是超级管理员
      superAdmin: 0,
      // 角色名
      roleName: undefined,
      // 角色列表
      roleList: [],
      // 查询表单参数
      queryForm: {
        // 账户名
        accountName: undefined,
        // 账户状态
        accountStatus: undefined,
        // 是否查询普通账号
        ordinary: 1,
        // 当前页
        current: 1,
        // 当前页大小
        pageSize: 20
      },
      // 账户列表
      accountList: [],
      // 账户总数
      total: 0,
      // 控制修改账户对话框的显示
      editDialogVisible: false,
      // 修改账户表单数据
      editForm: {
        accountId: undefined,
        accountName: undefined,
        email: undefined,
        phone: undefined,
        remark: undefined
      },
      // 修改账户表单校验规则
      editFormRules: {
        accountName: [
          {
            required: true,
            message: '请输入账户名',
            trigger: 'blur'
          },
          {
            min: 3,
            max: 20,
            message: '账户名长度在3~20字符之间',
            trigger: 'blur'
          }
        ],
        email: [
          {
            required: true,
            message: '请输入邮箱帐号',
            trigger: 'blur'
          },
          {
            validator: checkEmail,
            trigger: 'blur'
          }
        ],
        phone: [
          {
            required: true,
            message: '请输入联系电话',
            trigger: 'blur'
          },
          {
            validator: checkPhone,
            trigger: 'blur'
          }
        ]
      }
    }
  },
  created () {
    this.superAdmin = getSuperAdmin()
    this.getAccountPage()
  },
  updated () {
    this.showTableColumn()
  },
  methods: {
    // 根据权限数据展示操作列
    showTableColumn () {
      this.columnShow = showElement(['info:account:update', 'info:account:deleted', 'system:account:bind'])
    },
    // 查询表单重置
    resetQueryForm () {
      this.queryForm.accountName = undefined
      this.queryForm.status = undefined
      this.queryForm.current = 1
      this.queryForm.pageSize = 20
      // this.$refs.queryFormRef.resetFields()
      this.getAccountPage()
    },
    // 得到普通账户分页数据
    async getAccountPage () {
      const { data: res } = await handleAccountPage(this.queryForm)
      console.log(res.data)
      if (res.status !== '2000') return this.$message.error(res.message)
      this.accountList = res.data.records
      this.total = res.data.total
    },
    // 处理当前页大小改变
    handleSizeChange (newSize) {
      this.queryForm.pageSize = newSize
      this.getAccountPage()
    },
    // 处理当前页码改变
    handleCurrentChange (newCurrent) {
      this.queryForm.current = newCurrent
      this.getAccountPage()
    },
    // 监听switch变化，改变账户状态
    async changeAccountStatus (accountInfo) {
      const str = accountInfo.status === 0 ? '启用' : '禁用'
      this.$confirm('此操作将' + str + '该账户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        const param = {}
        param.accountId = accountInfo.accountId
        param.status = accountInfo.status

        const { data: res } = await handleUpdateAccount(param)
        if (res.status !== '2000') {
          // 更新失败的话，将页面的状态修改回去
          accountInfo.status = str === '启用' ? 1 : 0
          return this.$message.error(res.message)
        }
        this.$message.success('账户' + str + '成功!')
      }).catch(() => {
        // 取消修改的话，将页面的状态修改回去
        accountInfo.status = str === '启用' ? 1 : 0
        this.$message({
          type: 'info',
          message: '已取消' + str + '该账户'
        })
      })
    },
    // 展示编辑账户对话框
    async showEditDialog (accountId) {
      const { data: res } = await handleAccountQuery(accountId)
      console.log(res)
      if (res.status !== '2000') {
        return this.$message.error(res.message)
      }
      // 查询出账户 在展示编辑框
      this.editDialogVisible = true
      this.editForm = res.data
    },
    // 监听 编辑账户对话框关闭事件
    handleEditDialogClose () {
      // 清空字段
      this.$refs.editFormRef.resetFields()
    },
    // 确定编辑账户
    editAccount () {
      this.$refs.editFormRef.validate(async valid => {
        if (valid) {
          const { data: res } = await handleUpdateAccount(this.editForm)
          if (res.status !== '2000') return this.$message.error(res.message)
          this.$message.success(res.message)
          this.editDialogVisible = false
          await this.getAccountPage()
        }
      })
    },
    // 删除账户
    deleteAccount (accountId) {
      this.$confirm('此操作将删除该账户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        const { data: res } = await handleDeleteAccount(accountId)
        if (res.status !== '2000') {
          return this.$message.error(res.message)
        }
        this.$message({
          type: 'success',
          message: res.message
        })
        await this.getAccountPage()
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    }
  }
}
</script>

<style lang="less" scoped>
.el-select {
  width: 100%;
}
</style>
