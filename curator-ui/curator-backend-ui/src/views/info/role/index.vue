<template>
  <div class="role_container">
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>角色管理</el-breadcrumb-item>
      <el-breadcrumb-item>角色列表</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图区域-->
    <el-card class="box-card">
      <!--  搜索与添加区域 -->
      <el-form :model="queryForm" ref="queryFormRef" :inline="true" label-width="68px">
        <el-form-item label="角色名称">
          <el-input
            v-model="queryForm.roleName"
            placeholder="请输入角色名称"
            clearable
            size="small"
            style="width: 240px"
            @keyup.enter.native="getRolePage"
          />
        </el-form-item>
        <el-form-item label="角色状态">
          <el-select v-model="queryForm.roleStatus" placeholder="请选择角色状态" size="small">
            <el-option :key="1" label="启用" :value="1"></el-option>
            <el-option :key="2" label="停用" :value="2"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="info" icon="el-icon-search" size="small" @click="getRolePage">搜索</el-button>
          <el-button type="warning" icon="el-icon-refresh" size="small" @click="resetQueryForm">重置</el-button>
        </el-form-item>
      </el-form>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-button type="primary" size="mini" v-has-perm="['info:role:add']" @click="showAddDialog">添加角色</el-button>
        </el-col>
      </el-row>

      <!--  角色列表区域 -->
      <el-table :data="roleList" border stripe>
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-form label-position="left" class="table-expand">
              <el-form-item label="创建账户">
                <span>{{ props.row.createAccountName }}</span>
              </el-form-item>
              <el-form-item label="创建时间">
                <span>{{ props.row.createTime }}</span>
              </el-form-item>
              <el-form-item label="更新时间">
                <span>{{ props.row.updateTime }}</span>
              </el-form-item>
              <el-form-item label="备注">
                <span>{{ props.row.roleRemark }}</span>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column label="角色名" prop="roleName" align="center"></el-table-column>
        <el-table-column label="角色类型" prop="roleTypeDesc" align="center"></el-table-column>
        <el-table-column label="状态" align="center">
          <template slot-scope="props">
            <el-switch v-model="props.row.roleStatus" active-text="启用" :active-value="1" inactive-text="停用" :inactive-value="2"
                       @change="changeRoleStatus(props.row)"></el-switch>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="300px;" align="center" v-if="columnShow">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" size="mini" v-has-perm="['info:role:update']" @click="showEditDialog(scope.row.roleId)">编辑</el-button>
            <el-button type="info" icon="el-icon-setting" size="mini" @click="showPowerGroupDialog(scope.row)">权限组绑定</el-button>
            <el-button type="danger" icon="el-icon-delete" size="mini" v-has-perm="['info:role:delete']" @click="deleteRole(scope.row.roleId)">删除</el-button>
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

    <!-- 添加角色对话框 -->
    <el-dialog title="添加角色" :visible.sync="addDialogVisible" width="30%" @close="handleAddDialogClose">
      <!-- 对话框主题区域 -->
      <el-form ref="addFormRef" :model="addForm" :rules="dialogFormRules" label-width="80px">
        <el-form-item label="角色名" prop="roleName">
          <el-input v-model="addForm.roleName"></el-input>
        </el-form-item>
        <el-form-item label="角色类型" prop="roleType">
          <el-select v-model="addForm.roleType" placeholder="请选择角色类型">
            <el-option
              v-for="item in roleTypeList"
              :key="item.status"
              :label="item.desc"
              :value="item.status"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="roleRemark">
          <el-input type="textarea" :rows="2" v-model="addForm.roleRemark"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部按钮区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addRole">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 修改角色对话框 -->
    <el-dialog title="编辑角色" :visible.sync="editDialogVisible" width="30%" @close="handleEditDialogClose">
      <!-- 对话框主题区域 -->
      <el-form ref="editFormRef" :model="editForm" :rules="dialogFormRules" label-width="80px">
        <el-form-item label="角色名" prop="roleName">
          <el-input v-model="editForm.roleName"></el-input>
        </el-form-item>
        <el-form-item label="角色类型" prop="roleType">
          <el-select v-model="editForm.roleType" placeholder="请选择角色类型">
            <el-option
              v-for="item in roleTypeList"
              :key="item.status"
              :label="item.desc"
              :value="parseInt(item.status)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="roleRemark">
          <el-input type="textarea" :rows="2" v-model="editForm.roleRemark"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部按钮区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="editRole">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 修改角色权限对话框 -->
    <el-dialog title="绑定权限组" :visible.sync="authDialogVisible" width="35%" @close="handleAuthDialogClose">
      <!-- 对话框主题区域 -->
      <el-form ref="authFormRef" :model="authForm" :rules="dialogFormRules" label-width="100px">
        <el-form-item label="角色名">
          <el-input v-model="authForm.roleName" disabled></el-input>
        </el-form-item>
        <el-form-item label="权限组列表">
          <el-select v-model="authForm.powerGroupIdList" multiple placeholder="请选择权限组">
            <el-option
              v-for="item in powerGroupOptions"
              :key="item.powerGroupId"
              :label="item.powerGroupName"
              :value="item.powerGroupId">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <!-- 底部按钮区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="authDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleAuthDialogConfirm">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  handleAddRole,
  handleDeleteRole,
  handleUpdateRole,
  handleRolePage,
  handleRoleQuery,
  handleRoleTypeList
} from '@/apis/info/role'
import { handlePowerGroupList, handleAddPowerGroupToRole } from '@/apis/info/powerGroup'
import { showElement } from '@/utils/show'

export default {
  name: 'RolePage',
  data () {
    return {
      columnShow: true,
      // 是否是超级管理员
      superAdmin: 0,
      // 角色类型列表
      roleTypeList: [],
      // 启用的权限组列表
      powerGroupOptions: [],
      defaultProps: {
        children: 'children',
        label: 'label',
        isLeaf: 'leaf'
      },
      // 查询表单参数
      queryForm: {
        // 角色名
        roleName: undefined,
        // 角色状态
        roleStatus: undefined,
        // 当前页
        current: 1,
        // 当前页大小
        pageSize: 20
      },
      // 角色列表
      roleList: [],
      // 角色总数
      total: 0,
      // 控制添加角色对话框的显示
      addDialogVisible: false,
      // 添加角色表单数据
      addForm: {
        roleName: undefined,
        roleType: undefined,
        roleRemark: undefined
      },
      // 控制修改角色对话框的显示
      editDialogVisible: false,
      // 修改角色表单数据
      editForm: {
        roleId: undefined,
        roleName: undefined,
        roleType: undefined,
        roleRemark: undefined
      },
      // 角色表单校验规则
      dialogFormRules: {
        roleName: [
          {
            required: true,
            message: '请输入角色名',
            trigger: 'blur'
          },
          {
            min: 3,
            max: 20,
            message: '角色名长度在3~20字符之间',
            trigger: 'blur'
          }
        ],
        roleType: [
          {
            required: true,
            message: '请选择角色类型',
            trigger: 'blur'
          }
        ]
      },
      // 控制分配权限对话框的显示
      authDialogVisible: false,
      // 分配权限表单数据
      authForm: {
        roleId: undefined,
        roleName: undefined,
        powerGroupIdList: []
      }
    }
  },
  created () {
    this.superAdmin = this.$store.state.superAdmin
    this.showRoleTypeList()
    this.getRolePage()
  },
  updated () {
    this.showTableColumn()
  },
  methods: {
    // 根据权限数据展示操作列
    showTableColumn () {
      this.columnShow = showElement(['info:role:update', 'info:role:delete'])
    },
    // 查询表单重置
    resetQueryForm () {
      this.queryForm.roleName = undefined
      this.queryForm.roleStatus = undefined
      this.queryForm.current = 1
      this.queryForm.pageSize = 20
      this.getRolePage()
    },
    // 得到角色分页数据
    async getRolePage () {
      this.queryForm.superAdmin = this.superAdmin
      const { data: res } = await handleRolePage(this.queryForm)
      console.log(res.data)
      if (res.status !== '2000') return this.$message.error(res.message)
      this.roleList = res.data.records
      this.total = res.data.total
    },
    // 处理当前页大小改变
    handleSizeChange (newSize) {
      this.queryForm.pageSize = newSize
      this.getRolePage()
    },
    // 处理当前页码改变
    handleCurrentChange (newCurrent) {
      this.queryForm.current = newCurrent
      this.getRolePage()
    },
    // 监听switch变化，改变角色状态
    async changeRoleStatus (roleInfo) {
      const str = roleInfo.roleStatus === 1 ? '启用' : '停用'
      this.$confirm('此操作将' + str + '该角色, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        const param = {}
        param.roleId = roleInfo.roleId
        param.roleStatus = roleInfo.roleStatus

        const { data: res } = await handleUpdateRole(param)
        if (res.status !== '2000') {
          // 更新失败的话，将页面的状态修改回去
          roleInfo.roleStatus = str === '启用' ? 1 : 2
          return this.$message.error(res.message)
        }
        this.$message.success('角色' + str + '成功!')
      }).catch(() => {
        // 取消修改的话，将页面的状态修改回去
        roleInfo.status = str === '启用' ? 1 : 2
        this.$message({
          type: 'info',
          message: '已取消' + str + '该角色'
        })
      })
    },
    // 监听 添加角色对话框关闭事件
    handleAddDialogClose () {
      // 清空字段
      this.$refs.addFormRef.resetFields()
    },
    // 展示添加角色对话框
    showAddDialog () {
      this.addDialogVisible = true
    },
    // 添加角色
    addRole () {
      this.$refs.addFormRef.validate(async valid => {
        if (valid) {
          const { data: res } = await handleAddRole(this.addForm)
          if (res.status !== '2000') return this.$message.error(res.message)
          this.$message.success(res.message)
          this.addDialogVisible = false
          await this.getRolePage()
        }
      })
    },
    // 展示编辑角色对话框
    async showEditDialog (roleId) {
      const { data: res } = await handleRoleQuery(roleId)
      console.log(res)
      if (res.status !== '2000') {
        return this.$message.error(res.message)
      }
      // 查询出角色 在展示编辑框
      this.editDialogVisible = true
      this.editForm = res.data
    },
    // 监听 编辑角色对话框关闭事件
    handleEditDialogClose () {
      // 清空字段
      this.$refs.editFormRef.resetFields()
    },
    // 确定编辑角色
    editRole () {
      this.$refs.editFormRef.validate(async valid => {
        if (valid) {
          const { data: res } = await handleUpdateRole(this.editForm)
          if (res.status !== '2000') return this.$message.error(res.message)
          this.$message.success(res.message)
          this.editDialogVisible = false
          await this.getRolePage()
        }
      })
    },
    // 删除角色
    deleteRole (roleId) {
      this.$confirm('此操作将删除该角色, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        const { data: res } = await handleDeleteRole(roleId)
        if (res.status !== '2000') {
          return this.$message.error(res.message)
        }
        this.$message({
          type: 'success',
          message: res.message
        })
        await this.getRolePage()
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    // 展示分配权限对话框
    async showPowerGroupDialog (role) {
      const query = { superAdmin: this.superAdmin }
      const { data: res } = await handlePowerGroupList(query)
      this.powerGroupOptions = res.data
      this.authDialogVisible = true
      this.authForm = role
    },
    // 监听 分配权限对话框关闭事件
    handleAuthDialogClose () {
      // 清空字段
      this.$refs.authFormRef.resetFields()
      this.powerGroupOptions = []
    },
    // 处理分配权限对话框确定按钮
    async handleAuthDialogConfirm () {
      const { data: res } = await handleAddPowerGroupToRole(this.authForm)
      if (res.status !== '2000') return this.$message.error(res.message)
      this.$message.success(res.message)
      this.authDialogVisible = false
      await this.getRolePage()
    },
    // 获取角色类型
    async showRoleTypeList () {
      const { data: result } = await handleRoleTypeList()
      if (result.status !== '2000') {
        return this.$message.error(result.message)
      }
      this.roleTypeList = result.data
    }
  }
}
</script>

<style lang="less" scoped>
.el-tree {
  border: solid 1px #e7e1cd;
}
.el-select {
  width: 100%;
}
</style>
