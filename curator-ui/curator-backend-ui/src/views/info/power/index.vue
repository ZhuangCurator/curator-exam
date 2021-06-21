<template>
  <div class="power_container">
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>权限管理</el-breadcrumb-item>
      <el-breadcrumb-item>权限列表</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图区域-->
    <el-card class="box-card">
      <!--  搜索与添加区域 -->
      <el-form :model="queryForm" ref="queryFormRef" inline label-width="68px">
        <el-form-item label="权限名称">
          <el-input
            v-model="queryForm.powerName"
            placeholder="权限名称"
            clearable
            size="small"
            style="width: 240px"
            @keyup.enter.native="getPowerList"
          />
        </el-form-item>
        <el-form-item label="权限状态">
          <el-select v-model="queryForm.powerStatus" placeholder="请选择权限状态" size="small">
            <el-option :key="1" label="启用" :value="1"></el-option>
            <el-option :key="2" label="停用" :value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item class="button_container">
          <el-button type="info" icon="el-icon-search" size="small" @click="getPowerList">搜索</el-button>
          <el-button type="warning" icon="el-icon-refresh" size="small" @click="resetQueryForm">重置</el-button>
        </el-form-item>
      </el-form>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-button type="primary" size="mini" v-has-perm="['power:dir:add']" @click="showTopPowerAddDialog">添加目录</el-button>
        </el-col>
      </el-row>
      <!--  权限列表区域 -->
      <el-table
        :data="tableDataList"
        style="width: 100%;margin-bottom: 20px;"
        row-key="powerId"
        border
        :tree-props="{children: 'children'}">
        <el-table-column prop="powerName" label="权限名称" :show-overflow-tooltip="true" width="130px"></el-table-column>
        <el-table-column prop="powerTypeDesc" align="center" label="权限类型" width="80px"></el-table-column>
        <el-table-column prop="powerPerms" label="权限标识" width="150px"></el-table-column>
        <el-table-column prop="powerPath" label="路由地址" width="130px"></el-table-column>
        <el-table-column prop="powerComponent" label="组件路径"></el-table-column>
        <el-table-column prop="powerIcon" label="权限图标" width="160px"></el-table-column>
        <el-table-column label="权限状态" align="center" width="70px">
          <template slot-scope="scope">
            <el-switch v-model="scope.row.powerStatus" :active-value="1" :inactive-value="2"
                       @change="changePowerStatus(scope.row)"></el-switch>
          </template>
        </el-table-column>
        <el-table-column prop="powerOrder" align="center" label="排序" width="50px"></el-table-column>
        <el-table-column label="操作" align="center" width="350px" v-if="columnShow">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-folder-add" size="mini" v-has-perm="['power:child:add']" @click="showAddDialog(scope.row)">添加下级权限</el-button>
            <el-button type="info" icon="el-icon-edit" size="mini" v-has-perm="['info:power:update']" @click="showEditDialog(scope.row.powerId)">编辑</el-button>
            <el-button type="danger" icon="el-icon-delete" size="mini" v-has-perm="['info:power:delete']" @click="deletePower(scope.row.powerId)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 修改权限对话框 -->
    <el-dialog title="权限编辑" :visible.sync="updateDialogVisible" width="30%" @close="handleUpdateDialogClose">
      <!-- 对话框主题区域 -->
      <el-form ref="updateDialogFormRef" :model="updateDialogForm" :rules="dialogFormRules" label-width="80px">
        <el-form-item label="上级权限" prop="parentId">
          <tree-select
            :options="powerOptions"
            :normalizer="normalizer"
            placeholder="请选择上级权限..."
            v-model="updateDialogForm.parentId"
            disabled
          />
        </el-form-item>
        <el-form-item label="权限名称" prop="powerName">
          <el-input v-model="updateDialogForm.powerName"></el-input>
        </el-form-item>
        <el-form-item label="权限类型" prop="powerType">
          <el-radio-group v-model="updateDialogForm.powerType" disabled>
            <el-radio :label="1">菜单</el-radio>
            <el-radio :label="2">按钮</el-radio>
            <el-radio :label="3">目录</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="权限标识" prop="powerPerms" v-if="updateDialogForm.powerType !== 3">
          <el-input v-model="updateDialogForm.powerPerms"></el-input>
        </el-form-item>
        <el-form-item label="路由地址" prop="powerPath" v-if="updateDialogForm.powerType !== 2">
          <el-input v-model="updateDialogForm.powerPath"></el-input>
        </el-form-item>
        <el-form-item label="组件路径" prop="powerComponent" v-if="updateDialogForm.powerType === 1">
          <el-input v-model="updateDialogForm.powerComponent"></el-input>
        </el-form-item>
        <el-form-item label="权限排序" prop="powerOrder">
          <el-input-number v-model="updateDialogForm.powerOrder" controls-position="right" :min="1"></el-input-number>
        </el-form-item>
        <el-form-item label="权限图标" prop="powerIcon"  v-if="updateDialogForm.powerType === 3">
          <el-input v-model="updateDialogForm.powerIcon"></el-input>
        </el-form-item>
        <el-form-item label="是否隐藏" prop="hide" v-if="updateDialogForm.powerType !== 2">
          <el-radio v-model="updateDialogForm.hide" :label="1">是</el-radio>
          <el-radio v-model="updateDialogForm.hide" :label="0">否</el-radio>
        </el-form-item>
        <el-form-item label="权限备注" prop="powerRemark">
          <el-input v-model="updateDialogForm.powerRemark"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部按钮区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleUpdateDialogClose">取 消</el-button>
        <el-button type="primary" @click="editPower">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 添加权限对话框 -->
    <el-dialog title="权限添加" :visible.sync="addDialogVisible" width="30%" @close="handleAddDialogClose">
      <!-- 对话框主题区域 -->
      <el-form ref="addDialogFormRef" :model="addDialogForm" :rules="dialogFormRules" label-width="80px">
        <el-form-item label="上级权限" prop="parentId">
          <tree-select
            :options="powerOptions"
            :normalizer="normalizer"
            placeholder="请选择上级权限..."
            v-model="addDialogForm.parentId"
            disabled
          />
        </el-form-item>
        <el-form-item label="权限名称" prop="powerName">
          <el-input v-model="addDialogForm.powerName"></el-input>
        </el-form-item>
        <el-form-item label="权限类型" prop="powerType">
          <el-radio-group v-model="addDialogForm.powerType" disabled>
            <el-radio :label="1">菜单</el-radio>
            <el-radio :label="2">按钮</el-radio>
            <el-radio :label="3">目录</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="权限标识" prop="powerPerms" v-if="addDialogForm.powerType !== 3">
          <el-input v-model="addDialogForm.powerPerms"></el-input>
        </el-form-item>
        <el-form-item label="路由地址" prop="powerPath" v-if="addDialogForm.powerType !== 2">
          <el-input v-model="addDialogForm.powerPath"></el-input>
        </el-form-item>
        <el-form-item label="组件路径" prop="powerComponent" v-if="addDialogForm.powerType === 1">
          <el-input v-model="addDialogForm.powerComponent"></el-input>
        </el-form-item>
        <el-form-item label="权限排序" prop="powerOrder">
          <el-input-number v-model="addDialogForm.powerOrder" controls-position="right" :min="1"></el-input-number>
        </el-form-item>
        <el-form-item label="权限图标" prop="powerIcon" v-if="addDialogForm.powerType === 3">
          <el-input v-model="addDialogForm.powerIcon"></el-input>
        </el-form-item>
        <el-form-item label="是否隐藏" prop="hide" v-if="addDialogForm.powerType !== 2">
          <el-radio v-model="addDialogForm.hide" label="1">是</el-radio>
          <el-radio v-model="addDialogForm.hide" label="0">否</el-radio>
        </el-form-item>
        <el-form-item label="权限备注" prop="powerRemark">
          <el-input type="textarea" :rows="2" v-model="addDialogForm.powerRemark"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部按钮区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleAddDialogClose">取 消</el-button>
        <el-button type="primary" @click="addPower">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { handleTreePower, handlePowerQuery, handleAddPower, handleUpdatePower, handleDeletePower, handleUpdatePowerStatus } from '@/apis/info/power'
import TreeSelect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import { showElement } from '@/utils/show'

export default {
  name: 'PowerPage',
  components: { TreeSelect },
  data () {
    return {
      columnShow: true,
      // 查询表单数据
      queryForm: {
        powerName: undefined,
        powerStatus: undefined
      },
      // 权限树选项
      powerOptions: [],
      // 表格数据列表
      tableDataList: [],
      // 控制添加对话框的展示隐藏
      addDialogVisible: false,
      // 控制编辑对话框的展示隐藏
      updateDialogVisible: false,
      // 添加对话框表单数据
      addDialogForm: {
        parentId: undefined,
        powerName: undefined,
        powerOrder: undefined,
        powerType: undefined,
        powerPath: undefined,
        powerPerms: undefined,
        powerComponent: undefined,
        powerIcon: undefined,
        hide: undefined,
        powerRemark: undefined
      },
      // 编辑对话框表单数据
      updateDialogForm: {
        parentId: undefined,
        powerId: undefined,
        powerName: undefined,
        powerOrder: undefined,
        powerType: undefined,
        powerPath: undefined,
        powerPerms: undefined,
        powerComponent: undefined,
        powerIcon: undefined,
        hide: undefined,
        powerRemark: undefined
      },
      dialogFormRules: {
        parentId: [
          {
            required: true,
            message: '请选择上级权限',
            trigger: 'blur'
          }
        ],
        powerName: [
          {
            required: true,
            message: '请输入账户名',
            trigger: 'blur'
          }
        ],
        powerType: [
          {
            required: true,
            message: '请选择权限类型',
            trigger: 'change'
          }
        ],
        powerOrder: [
          {
            required: true,
            message: '请输入权限排序',
            trigger: 'blur'
          }
        ],
        powerPath: [
          {
            required: true,
            message: '请输入路由地址',
            trigger: 'blur'
          }
        ],
        powerComponent: [
          {
            required: true,
            message: '请输入路由地址',
            trigger: 'blur'
          }
        ],
        powerIcon: [
          {
            required: true,
            message: '请输入权限图标',
            trigger: 'blur'
          }
        ],
        powerPerms: [
          {
            required: true,
            message: '请输入权限标识',
            trigger: 'blur'
          }
        ],
        hide: [
          {
            required: true,
            message: '请选择是否隐藏！',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  created () {
    this.getPowerList()
  },
  updated () {
    this.showTableColumn()
  },
  methods: {
    // 根据权限数据展示操作列
    showTableColumn () {
      this.columnShow = showElement(['info:power:update', 'info:power:delete'])
    },
    // 查询表单重置
    resetQueryForm () {
      this.queryForm.powerName = undefined
      this.queryForm.powerStatus = undefined
      this.getPowerList()
    },
    // 获取权限数据列表
    async getPowerList () {
      const { data: res } = await handleTreePower(this.queryForm)
      console.log(res.data)
      this.tableDataList = res.data
    },
    // 转换权限数据结构
    normalizer (node) {
      if (!node.children) {
        delete node.children
      }
      return {
        id: node.powerId,
        label: node.powerName,
        children: node.children
      }
    },
    // 展示编辑对话框
    async showEditDialog (powerId) {
      const { data: res } = await handlePowerQuery(powerId)
      if (res.status !== '2000') return this.$message.error(res.message())
      // 获取所有的权限数据
      const { data: result } = await handleTreePower()
      if (result.status !== '2000') return this.$message.error(result.message)
      const topPower = { powerId: 0, powerName: '顶级权限', children: [] }
      topPower.children = result.data
      this.powerOptions.push(topPower)
      // 展示对话框
      this.updateDialogVisible = true
      this.updateDialogForm = res.data
    },
    // 展示顶级权限添加对话框
    async showTopPowerAddDialog () {
      const topPower = { powerId: 0, powerName: '顶级权限', children: [] }
      this.powerOptions.push(topPower)
      // 展示对话框
      this.addDialogVisible = true
      this.addDialogForm.parentId = 0
      this.addDialogForm.sort = 1
      this.addDialogForm.powerType = 3
    },
    // 展示添加对话框
    async showAddDialog (power) {
      const topPower = { powerId: power.powerId, powerName: power.powerName, children: [] }
      this.powerOptions.push(topPower)
      // 展示对话框
      this.addDialogVisible = true
      this.addDialogForm.parentId = power.powerId
      this.addDialogForm.sort = 1
      if (power.powerType === 3) {
        this.addDialogForm.powerType = 1
      } else if (power.powerType === 1) {
        this.addDialogForm.powerType = 2
      } else if (power.powerType === 2) {
        this.addDialogVisible = false
        this.$message.error('按钮权限不允许添加下级权限!')
      }
    },
    // 监听 添加对话框关闭事件
    handleAddDialogClose () {
      // 清空字段
      this.$refs.addDialogFormRef.resetFields()
      this.addDialogVisible = false
      this.powerOptions = []
    },
    // 监听 编辑对话框关闭事件
    handleUpdateDialogClose () {
      // 清空字段
      this.$refs.updateDialogFormRef.resetFields()
      this.updateDialogVisible = false
      this.powerOptions = []
    },
    // 确定编辑权限
    editPower () {
      this.$refs.updateDialogFormRef.validate(async valid => {
        if (valid) {
          const { data: res } = await handleUpdatePower(this.updateDialogForm)
          if (res.status !== '2000') return this.$message.error(res.message)
          this.$message.success(res.message)
          this.updateDialogVisible = false
          await this.getPowerList()
        }
      })
    },
    // 确定添加权限
    addPower () {
      this.$refs.addDialogFormRef.validate(async valid => {
        if (valid) {
          const { data: res } = await handleAddPower(this.addDialogForm)
          if (res.status !== '2000') return this.$message.error(res.message)
          this.$message.success(res.message)
          this.addDialogVisible = false
          await this.getPowerList()
        }
      })
    },
    // 删除权限
    deletePower (powerId) {
      this.$confirm('此操作将删除该权限, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        const { data: res } = await handleDeletePower(powerId)
        if (res.status !== '2000') {
          return this.$message.error(res.message)
        }
        this.$message({
          type: 'success',
          message: res.message
        })
        await this.getPowerList()
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    // 监听switch变化，改变权限状态
    async changePowerStatus (powerInfo) {
      const str = powerInfo.powerStatus === 1 ? '启用' : '停用'
      this.$confirm('此操作将' + str + '该权限及其子权限, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        const param = {}
        param.powerId = powerInfo.powerId
        param.powerStatus = powerInfo.powerStatus

        const { data: res } = await handleUpdatePowerStatus(param)
        if (res.status !== '2000') {
          // 更新失败的话，将页面的状态修改回去
          powerInfo.powerStatus = str === '启用' ? 1 : 2
          return this.$message.error(res.message)
        }
        await this.getPowerList()
        this.$message.success('该权限及其子权限' + str + '成功!')
      }).catch(() => {
        // 取消修改的话，将页面的状态修改回去
        powerInfo.powerStatus = str === '启用' ? 1 : 2
        this.$message({
          type: 'info',
          message: '已取消' + str + '该权限及其子权限'
        })
      })
    }
  }
}
</script>

<style lang="less" scoped>
.el-input-number {
  width: 100%;
}
</style>
