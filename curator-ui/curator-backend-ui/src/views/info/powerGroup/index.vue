<template>
  <div class="powerGroup_container">
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>权限组管理</el-breadcrumb-item>
      <el-breadcrumb-item>权限组列表</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图区域-->
    <el-card class="box-card">
      <!--  搜索与添加区域 -->
      <el-form :model="queryForm" ref="queryFormRef" :inline="true" label-width="85px">
        <el-form-item label="权限组名称">
          <el-input
            v-model="queryForm.powerGroupName"
            placeholder="请输入权限组名称"
            clearable
            size="small"
            style="width: 240px"
            @keyup.enter.native="getPowerGroupPage"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="info" icon="el-icon-search" size="small" @click="getPowerGroupPage">搜索</el-button>
          <el-button type="warning" icon="el-icon-refresh" size="small" @click="resetQueryForm">重置</el-button>
        </el-form-item>
      </el-form>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-button type="primary" size="mini" v-has-perm="['info:powerGroup:add']" @click="showAddDialog">添加权限组</el-button>
        </el-col>
      </el-row>

      <!--  权限组列表区域 -->
      <el-table :data="powerGroupList" border stripe>
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-form label-position="left" class="table-expand">
              <el-form-item label="创建时间">
                <span>{{ props.row.createTime }}</span>
              </el-form-item>
              <el-form-item label="更新时间">
                <span>{{ props.row.updateTime }}</span>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column label="权限组名" prop="powerGroupName" align="center"></el-table-column>
        <el-table-column label="备注" prop="remark" align="center"></el-table-column>
        <el-table-column label="操作" width="300px;" align="center" v-if="columnShow">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" size="mini" v-has-perm="['info:powerGroup:update']" @click="showEditDialog(scope.row.powerGroupId)">编辑</el-button>
            <el-button type="info" icon="el-icon-setting" size="mini" v-has-perm="['powerGroup:bind:power']" @click="showPowerDialog(scope.row)">权限绑定</el-button>
            <el-button type="danger" icon="el-icon-delete" size="mini" v-has-perm="['info:powerGroup:delete']" @click="deletePowerGroup(scope.row.powerGroupId)">删除</el-button>
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

    <!-- 添加权限组对话框 -->
    <el-dialog title="添加权限组" :visible.sync="addDialogVisible" width="30%" @close="handleAddDialogClose">
      <!-- 对话框主题区域 -->
      <el-form ref="addFormRef" :model="addForm" :rules="dialogFormRules" label-width="80px">
        <el-form-item label="权限组名" prop="powerGroupName">
          <el-input v-model="addForm.powerGroupName"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" :rows="2" v-model="addForm.remark"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部按钮区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addPowerGroup">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 修改权限组对话框 -->
    <el-dialog title="编辑权限组" :visible.sync="editDialogVisible" width="30%" @close="handleEditDialogClose">
      <!-- 对话框主题区域 -->
      <el-form ref="editFormRef" :model="editForm" :rules="dialogFormRules" label-width="80px">
        <el-form-item label="权限组名" prop="powerGroupName">
          <el-input v-model="editForm.powerGroupName"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" :rows="2" v-model="editForm.remark"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部按钮区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="editPowerGroup">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 修改权限组数据权限对话框 -->
    <el-dialog title="分配权限" :visible.sync="authDialogVisible" width="40%" @close="handleAuthDialogClose">
      <!-- 对话框主题区域 -->
      <el-form ref="authFormRef" :model="authForm" :rules="dialogFormRules" label-width="80px">
        <el-form-item label="权限组名">
          <el-input v-model="authForm.powerGroupName" disabled></el-input>
        </el-form-item>
        <el-form-item label="菜单权限">
          <el-tree :data="powerOptions" :default-checked-keys="authForm.powerIdList" :props="defaultProps" ref="menuTree" node-key="id" show-checkbox></el-tree>
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
  handleAddPowerGroup,
  handleDeletePowerGroup,
  handleUpdatePowerGroup,
  handlePowerGroupPage,
  handlePowerGroupQuery,
  handleAddPowerToPowerGroup
} from '@/apis/info/powerGroup'
import { handlePowerList } from '@/apis/info/power'
import { showElement } from '@/utils/show'

export default {
  name: 'PowerGroupPage',
  data () {
    return {
      columnShow: true,
      // 是否是超级管理员
      superAdmin: 0,
      // 启用的权限列表
      powerOptions: [],
      defaultProps: {
        children: 'children',
        label: 'label',
        isLeaf: 'leaf'
      },
      // 查询表单参数
      queryForm: {
        // 权限组名
        powerGroupName: undefined,
        // 当前页
        current: 1,
        // 当前页大小
        pageSize: 20
      },
      // 权限组列表
      powerGroupList: [],
      // 权限组总数
      total: 0,
      // 控制添加权限组对话框的显示
      addDialogVisible: false,
      // 添加权限组表单数据
      addForm: {
        powerGroupName: undefined,
        powerGroupType: undefined,
        remark: undefined
      },
      // 控制修改权限组对话框的显示
      editDialogVisible: false,
      // 修改权限组表单数据
      editForm: {
        powerGroupId: undefined,
        powerGroupName: undefined,
        powerGroupType: undefined,
        remark: undefined
      },
      // 权限组表单校验规则
      dialogFormRules: {
        powerGroupName: [
          {
            required: true,
            message: '请输入权限组名',
            trigger: 'blur'
          },
          {
            min: 3,
            max: 20,
            message: '权限组名长度在3~20字符之间',
            trigger: 'blur'
          }
        ],
        powerGroupType: [
          {
            required: true,
            message: '请选择权限组类型',
            trigger: 'blur'
          }
        ]
      },
      // 控制分配权限对话框的显示
      authDialogVisible: false,
      // 分配权限表单数据
      authForm: {
        powerGroupId: undefined,
        powerGroupName: undefined,
        powerIdList: []
      }
    }
  },
  created () {
    this.superAdmin = this.$store.state.superAdmin
    this.getPowerGroupPage()
  },
  updated () {
    this.showTableColumn()
  },
  methods: {
    // 根据权限数据展示操作列
    showTableColumn () {
      this.columnShow = showElement(['system:powerGroup:update', 'system:powerGroup:deleted', 'system:powerGroup:bind'])
    },
    // 查询表单重置
    resetQueryForm () {
      this.queryForm.powerGroupName = undefined
      this.queryForm.current = 1
      this.queryForm.pageSize = 20
      this.getPowerGroupPage()
    },
    // 得到权限组分页数据
    async getPowerGroupPage () {
      this.queryForm.superAdmin = this.superAdmin
      const { data: res } = await handlePowerGroupPage(this.queryForm)
      console.log(res.data)
      if (res.status !== '2000') return this.$message.error(res.message)
      this.powerGroupList = res.data.records
      this.total = res.data.total
    },
    // 处理当前页大小改变
    handleSizeChange (newSize) {
      this.queryForm.pageSize = newSize
      this.getPowerGroupPage()
    },
    // 处理当前页码改变
    handleCurrentChange (newCurrent) {
      this.queryForm.current = newCurrent
      this.getPowerGroupPage()
    },
    // 监听 添加权限组对话框关闭事件
    handleAddDialogClose () {
      // 清空字段
      this.$refs.addFormRef.resetFields()
    },
    // 展示添加权限组对话框
    showAddDialog () {
      this.addDialogVisible = true
    },
    // 添加权限组
    addPowerGroup () {
      this.$refs.addFormRef.validate(async valid => {
        if (valid) {
          const { data: res } = await handleAddPowerGroup(this.addForm)
          if (res.status !== '2000') return this.$message.error(res.message)
          this.$message.success(res.message)
          this.addDialogVisible = false
          await this.getPowerGroupPage()
        }
      })
    },
    // 展示编辑权限组对话框
    async showEditDialog (powerGroupId) {
      const { data: res } = await handlePowerGroupQuery(powerGroupId)
      console.log(res)
      if (res.status !== '2000') {
        return this.$message.error(res.message)
      }
      // 查询出权限组 在展示编辑框
      this.editDialogVisible = true
      this.editForm = res.data
    },
    // 监听 编辑权限组对话框关闭事件
    handleEditDialogClose () {
      // 清空字段
      this.$refs.editFormRef.resetFields()
    },
    // 确定编辑权限组
    editPowerGroup () {
      this.$refs.editFormRef.validate(async valid => {
        if (valid) {
          const { data: res } = await handleUpdatePowerGroup(this.editForm)
          if (res.status !== '2000') return this.$message.error(res.message)
          this.$message.success(res.message)
          this.editDialogVisible = false
          await this.getPowerGroupPage()
        }
      })
    },
    // 删除权限组
    deletePowerGroup (powerGroupId) {
      this.$confirm('此操作将删除该权限组, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        const { data: res } = await handleDeletePowerGroup(powerGroupId)
        if (res.status !== '2000') {
          return this.$message.error(res.message)
        }
        this.$message({
          type: 'success',
          message: res.message
        })
        await this.getPowerGroupPage()
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    // 展示分配权限对话框
    async showPowerDialog (powerGroup) {
      const query = { powerStatus: 1 }
      const { data: res } = await handlePowerList(query)
      const result = this.formatPowerList(res.data)
      this.powerOptions = result
      this.authDialogVisible = true
      this.authForm = powerGroup
    },
    // 递归遍历权限树,得到新的树
    formatPowerList (arr) {
      const result = []
      for (let i = 0; i < arr.length; i++) {
        const item = arr[i]
        const node = { id: item.powerId, label: item.powerName }
        result.push(node)
        if (item.children) {
          node.leaf = false
          node.children = this.formatPowerList(item.children)
        } else {
          node.leaf = true
        }
      }
      return result
    },
    // 监听 分配权限对话框关闭事件
    handleAuthDialogClose () {
      // 清空字段
      this.$refs.authFormRef.resetFields()
      this.menuOptions = []
    },
    // 处理分配权限对话框确定按钮
    async handleAuthDialogConfirm () {
      const result = []
      this.$refs.menuTree.getCheckedNodes(false, true).some(node => {
        result.push(node.id)
      })
      this.authForm.powerIdList = result
      console.log(this.authForm)
      const { data: res } = await handleAddPowerToPowerGroup(this.authForm)
      if (res.status !== '2000') return this.$message.error(res.message)
      this.$message.success(res.message)
      this.authDialogVisible = false
      await this.getPowerGroupPage()
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
