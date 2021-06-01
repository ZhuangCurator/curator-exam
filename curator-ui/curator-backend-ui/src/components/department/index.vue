<template>
  <div class="department_container">
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>部门管理</el-breadcrumb-item>
      <el-breadcrumb-item>部门列表</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图区域-->
    <el-card class="box-card">
      <!--  搜索与添加区域 -->
      <el-form :model="queryForm" ref="queryFormRef" inline label-width="68px">
        <el-form-item label="部门名称">
          <el-input
            v-model="queryForm.deptName"
            placeholder="部门名称"
            clearable
            size="small"
            style="width: 240px"
            @keyup.enter.native="getDepartmentList"
          />
        </el-form-item>
        <el-form-item label="部门状态">
          <el-select v-model="queryForm.status" placeholder="请选择部门状态" size="small">
            <el-option :key="0" label="启用" :value="0"></el-option>
            <el-option :key="1" label="禁用" :value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item class="button_container">
          <el-button type="info" icon="el-icon-search" size="small" @click="getDepartmentList">搜索</el-button>
          <el-button type="warning" icon="el-icon-refresh" size="small" @click="resetQueryForm">重置</el-button>
        </el-form-item>
      </el-form>

      <!--  部门列表区域 -->
      <el-table
        :data="tableDataList"
        style="width: 100%;margin-bottom: 20px;"
        row-key="deptId"
        border
        :tree-props="{children: 'children'}">
        <el-table-column prop="deptName" label="部门名称"></el-table-column>
        <el-table-column label="部门状态">
          <template slot-scope="scope">
            <el-switch v-model="scope.row.status" :active-value="0" :inactive-value="1"
                       @change="changeDeptStatus(scope.row)"></el-switch>
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" >
        </el-table-column>
        <el-table-column label="操作" width="300px" v-if="columnShow">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" size="mini" v-has-perm="['system:dept:add']" @click="showAddDialog(scope.row.deptId)">添加</el-button>
            <el-button type="info" icon="el-icon-edit" size="mini" v-has-perm="['system:dept:update']" @click="showEditDialog(scope.row.deptId)">编辑</el-button>
            <el-button type="danger" icon="el-icon-delete" size="mini" v-has-perm="['system:dept:deleted']" @click="deleteDepartment(scope.row.deptId)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 修改部门对话框 -->
    <el-dialog :title="title" :visible.sync="dialogVisible" width="30%" @close="handleDialogClose">
      <!-- 对话框主题区域 -->
      <el-form ref="dialogFormRef" :model="dialogForm" :rules="dialogFormRules" label-width="80px">
        <el-form-item v-if="dialogForm.parentId !== '0'"  label="上级部门" prop="parentId">
          <tree-select
            :options="deptOptions"
            :normalizer="normalizer"
            placeholder="请选择上级部门..."
            v-model="dialogForm.parentId"
          />
        </el-form-item>
        <el-form-item label="部门名称" prop="deptName">
          <el-input v-model="dialogForm.deptName"></el-input>
        </el-form-item>
        <el-form-item label="部门排序" prop="sort">
          <el-input-number v-model="dialogForm.sort" controls-position="right" :min="1"></el-input-number>
        </el-form-item>
        <el-form-item label="部门状态" prop="status">
          <el-radio-group v-model="dialogForm.status" v-if="diaLogAddFlag">
            <el-radio :label="0">启用</el-radio>
            <el-radio :label="1">禁用</el-radio>
          </el-radio-group>
          <el-radio-group v-model="dialogForm.status" v-else>
            <el-radio disabled :label="0">启用</el-radio>
            <el-radio disabled :label="1">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <!-- 底部按钮区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleDialogClose">取 消</el-button>
        <el-button type="primary" v-if="diaLogAddFlag" @click="addDepartment">确 定</el-button>
        <el-button type="primary" v-else @click="editDepartment">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { handleDepartmentPage, handleDepartmentQuery, handleAddDepartment, handleUpdateDepartment, handleDeleteDepartment, handleUpdateDepartmentStatus } from '@/apis/departemnt'
import TreeSelect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import { showElement } from '@/utils/show'

export default {
  name: 'Department',
  components: { TreeSelect },
  data () {
    return {
      columnShow: true,
      // 查询表单数据
      queryForm: {
        deptName: '',
        status: ''
      },
      // 部门树选项
      deptOptions: [],
      // 表格数据列表
      tableDataList: [],
      // 对话框title
      title: '',
      // 控制对话框的展示隐藏
      dialogVisible: false,
      // 当前对话框是不是添加对话框, false为编辑对话框
      diaLogAddFlag: true,
      // 对话框表单数据
      dialogForm: {
        deptName: '',
        sort: '',
        status: ''
      },
      dialogFormRules: {
        deptName: [
          {
            required: true,
            message: '请输入账户名',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  created () {
    this.getDepartmentList()
  },
  updated () {
    this.showTableColumn()
  },
  methods: {
    // 根据权限数据展示操作列
    showTableColumn () {
      this.columnShow = showElement(['system:dept:update', 'system:dept:deleted', 'system:dept:add'])
    },
    // 查询表单重置
    resetQueryForm () {
      this.queryForm.deptName = ''
      this.queryForm.status = ''
      this.getDepartmentList()
    },
    // 获取部门数据列表
    async getDepartmentList () {
      const { data: res } = await handleDepartmentPage(this.queryForm)
      console.log(res.data)
      this.tableDataList = res.data
    },
    // 转换部门数据结构
    normalizer (node) {
      if (!node.children) {
        delete node.children
      }
      return {
        id: node.deptId,
        label: node.deptName,
        children: node.children
      }
    },
    // 展示编辑对话框
    async showEditDialog (deptId) {
      const { data: res } = await handleDepartmentQuery(deptId)
      if (res.status !== '2000') return this.$message.error(res.message())
      // 获取所有的部门数据
      const { data: result } = await handleDepartmentPage()
      this.deptOptions = result.data
      // 展示对话框
      this.dialogVisible = true
      // 表示当前对话框为编辑对话框
      this.diaLogAddFlag = false
      this.title = '部门编辑'
      this.dialogForm = res.data
    },
    // 展示添加对话框
    async showAddDialog (deptId) {
      // 获取所有的部门数据
      const { data: result } = await handleDepartmentPage()
      this.deptOptions = result.data
      // 展示对话框
      this.dialogVisible = true
      // 表示当前对话框为添加对话框
      this.diaLogAddFlag = true
      this.title = '部门添加'
      this.dialogForm.parentId = deptId
      this.dialogForm.sort = 1
      this.dialogForm.status = 0
    },
    // 监听 对话框关闭事件
    handleDialogClose () {
      // 清空字段
      this.$refs.dialogFormRef.resetFields()
      this.dialogVisible = false
    },
    // 确定编辑部门
    editDepartment () {
      this.$refs.dialogFormRef.validate(async valid => {
        if (valid) {
          const { data: res } = await handleUpdateDepartment(this.dialogForm)
          if (res.status !== '2000') return this.$message.error(res.message)
          this.$message.success(res.message)
          this.dialogVisible = false
          this.dialogForm = {}
          await this.getDepartmentList()
        }
      })
    },
    // 确定添加部门
    addDepartment () {
      this.$refs.dialogFormRef.validate(async valid => {
        if (valid) {
          const { data: res } = await handleAddDepartment(this.dialogForm)
          if (res.status !== '2000') return this.$message.error(res.message)
          this.$message.success(res.message)
          this.dialogVisible = false
          this.dialogForm = {}
          await this.getDepartmentList()
        }
      })
    },
    // 删除部门
    deleteDepartment (deptId) {
      this.$confirm('此操作将删除该部门, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        const { data: res } = await handleDeleteDepartment(deptId)
        if (res.status !== '2000') {
          return this.$message.error(res.message)
        }
        this.$message({
          type: 'success',
          message: res.message
        })
        await this.getDepartmentList()
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    // 监听switch变化，改变部门状态
    async changeDeptStatus (deptInfo) {
      const str = deptInfo.status === 0 ? '启用' : '禁用'
      this.$confirm('此操作将' + str + '该部门及其子部门, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        const param = {}
        param.deptId = deptInfo.deptId
        param.status = deptInfo.status

        const { data: res } = await handleUpdateDepartmentStatus(param)
        if (res.status !== '2000') {
          // 更新失败的话，将页面的状态修改回去
          deptInfo.status = str === '启用' ? 1 : 0
          return this.$message.error(res.message)
        }
        await this.getDepartmentList()
        this.$message.success('该部门及其子部门' + str + '成功!')
      }).catch(() => {
        // 取消修改的话，将页面的状态修改回去
        deptInfo.status = str === '启用' ? 1 : 0
        this.$message({
          type: 'info',
          message: '已取消' + str + '该部门及其子部门'
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
