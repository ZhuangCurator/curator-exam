<template>
  <div class="user_container">
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>用户管理</el-breadcrumb-item>
      <el-breadcrumb-item>用户列表</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图区域-->
    <el-card class="box-card">
      <!--  搜索与添加区域 -->
      <el-form :model="queryForm" ref="queryFormRef" :inline="true" label-width="68px">
        <el-form-item label="用户名称">
          <el-input
            v-model="queryForm.userName"
            placeholder="请输入用户名称"
            clearable
            size="small"
            style="width: 240px"
            @keyup.enter.native="getUserPage"
          />
        </el-form-item>
        <el-form-item label="用户状态">
          <el-select v-model="queryForm.status" placeholder="请选择用户状态" size="small">
            <el-option :key="0" label="启用" :value="0"></el-option>
            <el-option :key="1" label="禁用" :value="1"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="info" icon="el-icon-search" size="small" @click="getUserPage">搜索</el-button>
          <el-button type="warning" icon="el-icon-refresh" size="small" @click="resetQueryForm">重置</el-button>
        </el-form-item>
      </el-form>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-button type="primary" @click="addDialogVisible = true" v-has-perm="['system:user:add']">添加用户</el-button>
        </el-col>
      </el-row>

      <!--  用户列表区域 -->
      <el-table :data="userList" border stripe>
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-form label-position="left" class="table-expand">
              <el-form-item label="创建时间">
                <span>{{ props.row.createTime }}</span>
              </el-form-item>
              <el-form-item label="更新时间">
                <span>{{ props.row.updateTime }}</span>
              </el-form-item>
              <el-form-item label="头像地址">
                <span>{{ props.row.avatar }}</span>
              </el-form-item>
              <el-form-item label="状态">
                <span>{{ props.row.statusDesc }}</span>
              </el-form-item>
              <el-form-item label="备注">
                <span>{{ props.row.remark }}</span>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column label="用户名" prop="userName" align="center"></el-table-column>
        <el-table-column label="邮箱" prop="email" align="center"></el-table-column>
        <el-table-column label="电话" prop="phone" align="center"></el-table-column>
        <el-table-column label="性别" prop="sexDesc" align="center"></el-table-column>
        <el-table-column label="状态" align="center">
          <template slot-scope="props">
            <el-switch v-model="props.row.status" active-text="启用" :active-value="0" inactive-text="禁用" :inactive-value="1"
                       @change="changeUserStatus(props.row)"></el-switch>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="300px;" align="center" v-if="columnShow">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" size="mini" v-has-perm="['system:user:update']" @click="showEditDialog(scope.row.userId)">编辑
            </el-button>
            <el-button type="danger" icon="el-icon-delete" size="mini" v-has-perm="['system:user:deleted']" @click="deleteUser(scope.row.userId)">删除
            </el-button>
            <el-button type="warning" icon="el-icon-setting" size="mini" v-has-perm="['system:user:bind']" @click="showAssignRoleDialog(scope.row)">分配角色</el-button>
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

    <!-- 添加用户对话框 -->
    <el-dialog title="添加用户" :visible.sync="addDialogVisible" width="30%" @close="handleAddDialogClose">
      <!-- 对话框主题区域 -->
      <el-form ref="addFormRef" :model="addForm" :rules="addFormRules" label-width="80px">
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="addForm.userName"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="addForm.password" type="password"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="addForm.email"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="addForm.phone"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-radio-group v-model="addForm.sex">
            <el-radio :label="0">男</el-radio>
            <el-radio :label="1">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="头像" prop="avatar">
          <el-input v-model="addForm.avatar"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="addForm.remark"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部按钮区域 -->
      <span slot="footer" class="dialog-footer">
    <el-button @click="addDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="addUser">确 定</el-button>
  </span>
    </el-dialog>

    <!-- 修改用户对话框 -->
    <el-dialog title="编辑用户" :visible.sync="editDialogVisible" width="30%" @close="handleEditDialogClose">
      <!-- 对话框主题区域 -->
      <el-form ref="editFormRef" :model="editForm" :rules="editFormRules" label-width="80px">
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="editForm.userName" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="editForm.email"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="editForm.phone"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-radio-group v-model="editForm.sex">
            <el-radio :label="0">男</el-radio>
            <el-radio :label="1">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="头像" prop="avatar">
          <el-input v-model="editForm.avatar"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="editForm.remark"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部按钮区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="editUser">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 分配角色对话框 -->
    <el-dialog title="分配角色" :visible.sync="assignRoleDialogVisible" width="30%" @close="handleAssignRoleDialogClose">
      <!-- 对话框主题区域 -->
      <el-form ref="assignRoleFormRef" :model="assignRoleForm" :rules="assignRoleFormRules" label-width="80px">
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="assignRoleForm.userName" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="角色" prop="roleIdList">
          <el-select v-model="assignRoleForm.roleIdList" multiple placeholder="请选择角色">
            <el-option
              v-for="item in roleList"
              :key="item.roleId"
              :label="item.roleName"
              :value="item.roleId"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <!-- 底部按钮区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="assignRoleDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleAssignRole">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { handleUpdateUser, handleUserQuery, handleUserPage, handleAddUser, handleDeleteUser, handleBindRoleWithUser } from '@/apis/user'
import { handleRolePage } from '@/apis/role'
import { showElement } from '@/utils/show'
export default {
  name: 'Users',
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
      // 角色列表
      roleList: [],
      // 查询表单参数
      queryForm: {
        // 用户名
        userName: undefined,
        // 用户状态
        status: undefined,
        // 当前页
        current: 1,
        // 当前页大小
        pageSize: 20
      },
      // 用户列表
      userList: [],
      // 用户总数
      total: 0,
      // 控制添加用户对话框的显示
      addDialogVisible: false,
      // 添加用户表单数据
      addForm: {
        userName: undefined,
        password: undefined,
        roleIdList: [],
        email: undefined,
        phone: undefined,
        sex: undefined,
        avatar: undefined,
        remark: undefined
      },
      // 添加用户表单校验规则
      addFormRules: {
        userName: [
          {
            required: true,
            message: '请输入用户名',
            trigger: 'blur'
          },
          {
            min: 3,
            max: 10,
            message: '用户名长度在3~10字符之间',
            trigger: 'blur'
          }
        ],
        password: [
          {
            required: true,
            message: '请输入密码',
            trigger: 'blur'
          },
          {
            min: 5,
            max: 20,
            message: '密码长度在5~20字符之间',
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
        ],
        sex: [
          {
            required: true,
            message: '请选择性别',
            trigger: 'change'
          }
        ],
        avatar: [
          {
            required: true,
            message: '请上传头像',
            trigger: 'blur'
          }
        ]
      },
      // 控制修改用户对话框的显示
      editDialogVisible: false,
      // 修改用户表单数据
      editForm: {
        userId: undefined,
        userName: undefined,
        roleIdList: undefined,
        email: undefined,
        phone: undefined,
        sex: undefined,
        avatar: undefined,
        remark: undefined
      },
      // 修改用户表单校验规则
      editFormRules: {
        userName: [
          {
            required: true,
            message: '请输入用户名',
            trigger: 'blur'
          },
          {
            min: 3,
            max: 10,
            message: '用户名长度在3~10字符之间',
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
        ],
        sex: [
          {
            required: true,
            message: '请选择性别',
            trigger: 'change'
          }
        ],
        avatar: [
          {
            required: true,
            message: '请上传头像',
            trigger: 'blur'
          }
        ]
      },
      // 控制分配角色对话框的展示
      assignRoleDialogVisible: false,
      // 分配角色表单数据
      assignRoleForm: {
        userId: undefined,
        userName: undefined,
        roleIdList: undefined
      },
      // 分配角色表单验证规则
      assignRoleFormRules: {
        roleIdList: [
          { type: 'array', required: true, message: '请至少选择一个角色', trigger: 'change' }
        ]
      }
    }
  },
  created () {
    this.getUserPage()
  },
  updated () {
    this.showTableColumn()
  },
  methods: {
    // 根据权限数据展示操作列
    showTableColumn () {
      this.columnShow = showElement(['system:user:update', 'system:user:deleted', 'system:user:bind'])
    },
    // 查询表单重置
    resetQueryForm () {
      this.queryForm.userName = undefined
      this.queryForm.status = undefined
      this.queryForm.current = 1
      this.queryForm.pageSize = 20
      // this.$refs.queryFormRef.resetFields()
      this.getUserPage()
    },
    // 得到用户分页数据
    async getUserPage () {
      const { data: res } = await handleUserPage(this.queryForm)
      console.log(res.data)
      if (res.status !== '2000') return this.$message.error(res.message)
      this.userList = res.data.records
      this.total = res.data.total
    },
    // 处理当前页大小改变
    handleSizeChange (newSize) {
      this.queryForm.pageSize = newSize
      this.getUserPage()
    },
    // 处理当前页码改变
    handleCurrentChange (newCurrent) {
      this.queryForm.current = newCurrent
      this.getUserPage()
    },
    // 监听switch变化，改变用户状态
    async changeUserStatus (userInfo) {
      const str = userInfo.status === 0 ? '启用' : '禁用'
      this.$confirm('此操作将' + str + '该用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        const param = {}
        param.userId = userInfo.userId
        param.status = userInfo.status

        const { data: res } = await handleUpdateUser(param)
        if (res.status !== '2000') {
          // 更新失败的话，将页面的状态修改回去
          userInfo.status = str === '启用' ? 1 : 0
          return this.$message.error(res.message)
        }
        this.$message.success('用户' + str + '成功!')
      }).catch(() => {
        // 取消修改的话，将页面的状态修改回去
        userInfo.status = str === '启用' ? 1 : 0
        this.$message({
          type: 'info',
          message: '已取消' + str + '该用户'
        })
      })
    },
    // 监听 添加用户对话框关闭事件
    handleAddDialogClose () {
      // 清空字段
      this.$refs.addFormRef.resetFields()
    },
    // 添加用户
    addUser () {
      this.$refs.addFormRef.validate(async valid => {
        if (valid) {
          const { data: res } = await handleAddUser(this.addForm)
          if (res.status !== '2000') return this.$message.error(res.message)
          this.$message.success(res.message)
          this.addDialogVisible = false
          await this.getUserPage()
        }
      })
    },
    // 展示编辑用户对话框
    async showEditDialog (userId) {
      const { data: res } = await handleUserQuery(userId)
      console.log(res)
      if (res.status !== '2000') {
        return this.$message.error(res.message)
      }
      // 查询出用户 在展示编辑框
      this.editDialogVisible = true
      this.editForm = res.data
    },
    // 监听 编辑用户对话框关闭事件
    handleEditDialogClose () {
      // 清空字段
      this.$refs.editFormRef.resetFields()
    },
    // 确定编辑用户
    editUser () {
      this.$refs.editFormRef.validate(async valid => {
        if (valid) {
          const { data: res } = await handleUpdateUser(this.editForm)
          if (res.status !== '2000') return this.$message.error(res.message)
          this.$message.success(res.message)
          this.editDialogVisible = false
          await this.getUserPage()
        }
      })
    },
    // 删除用户
    deleteUser (userId) {
      this.$confirm('此操作将删除该用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        const { data: res } = await handleDeleteUser(userId)
        if (res.status !== '2000') {
          return this.$message.error(res.message)
        }
        this.$message({
          type: 'success',
          message: res.message
        })
        await this.getUserPage()
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    // 展示分配角色对话框
    async showAssignRoleDialog (user) {
      // 获取角色列表
      const query = { status: 0 }
      const { data: result } = await handleRolePage(query)
      if (result.status !== '2000') {
        return this.$message.error(result.message)
      }
      this.roleList = result.data.records
      console.log(this.roleList)
      this.assignRoleDialogVisible = true
      this.assignRoleForm = user
    },
    // 分配角色对话框确定按钮
    async handleAssignRole () {
      const { data: res } = await handleBindRoleWithUser(this.assignRoleForm)
      if (res.status !== '2000') return this.$message.error(res.message)
      this.$message.success(res.message)
      this.assignRoleDialogVisible = false
      await this.getUserPage()
    },
    // 监听 分配角色对话框关闭事件
    handleAssignRoleDialogClose () {
      // 清空字段
      this.$refs.assignRoleFormRef.resetFields()
    }
  }
}
</script>

<style lang="less" scoped>
.el-select {
  width: 100%;
}
</style>
