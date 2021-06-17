<template>
  <div class="examCategory_container">
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>考试类别管理</el-breadcrumb-item>
      <el-breadcrumb-item>考试类别列表</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图区域-->
    <el-card class="box-card">
      <!--  搜索与添加区域 -->
      <el-form :model="queryForm" ref="queryFormRef" :inline="true" label-width="100px">
        <el-form-item label="考试类别名称">
          <el-input
            v-model="queryForm.examCategoryName"
            placeholder="请输入考试类别名称"
            clearable
            size="small"
            style="width: 240px"
            @keyup.enter.native="getExamCategoryPage"
          />
        </el-form-item>
        <el-form-item label="考试开始时间">
          <el-date-picker
            v-model="queryForm.examStartTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            size="small"
            placeholder="选择日期时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="考试结束时间">
          <el-date-picker
            v-model="queryForm.examEndTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            size="small"
            placeholder="选择日期时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="info" icon="el-icon-search" size="small" @click="getExamCategoryPage">搜索</el-button>
          <el-button type="warning" icon="el-icon-refresh" size="small" @click="resetQueryForm">重置</el-button>
        </el-form-item>
      </el-form>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-button type="primary" size="mini" v-has-perm="['register:category:add']" @click="showAddDialog">添加考试类别</el-button>
        </el-col>
      </el-row>

      <!--  考试类别列表区域 -->
      <el-table :data="examCategoryList" border stripe>
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
        <el-table-column label="考试类别名" prop="examCategoryName" align="center"></el-table-column>
        <el-table-column label="考试开始时间" prop="examStartTime" align="center"></el-table-column>
        <el-table-column label="考试结束时间" prop="examEndTime" align="center"></el-table-column>
        <el-table-column label="操作" width="300px;" align="center" v-if="columnShow">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" size="mini" v-has-perm="['register:category:update']" @click="showEditDialog(scope.row.examCategoryId)">编辑</el-button>
            <el-button type="danger" icon="el-icon-delete" size="mini" v-has-perm="['register:category:delete']" @click="deleteExamCategory(scope.row.examCategoryId)">删除</el-button>
            <el-button type="info" icon="el-icon-setting" size="mini" v-has-perm="['register:subject:page']" @click="showSubjectDialog(scope.row.examCategoryId)">考试科目</el-button>
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

    <!-- 添加考试类别对话框 -->
    <el-dialog title="添加考试类别" :visible.sync="addDialogVisible" width="30%" @close="handleAddDialogClose">
      <!-- 对话框主题区域 -->
      <el-form ref="addFormRef" :model="addForm" :rules="dialogFormRules" label-width="120px">
        <el-form-item label="考试类别名" prop="examCategoryName">
          <el-input v-model="addForm.examCategoryName"></el-input>
        </el-form-item>
        <el-form-item label="考试开始时间" prop="examStartTime">
          <el-date-picker
            v-model="addForm.examStartTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            size="small"
            placeholder="选择日期时间"
            class="date-dialog">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="考试结束时间" prop="examEndTime">
          <el-date-picker
            v-model="addForm.examEndTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            size="small"
            placeholder="选择日期时间"
            class="date-dialog">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <!-- 底部按钮区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addExamCategory">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 修改考试类别对话框 -->
    <el-dialog title="编辑考试类别" :visible.sync="editDialogVisible" width="30%" @close="handleEditDialogClose">
      <!-- 对话框主题区域 -->
      <el-form ref="editFormRef" :model="editForm" :rules="dialogFormRules" label-width="120px">
        <el-form-item label="考试类别名" prop="examCategoryName">
          <el-input v-model="editForm.examCategoryName"></el-input>
        </el-form-item>
        <el-form-item label="考试开始时间" prop="examStartTime">
          <el-date-picker
            v-model="editForm.examStartTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            size="small"
            placeholder="选择日期时间"
            class="date-dialog">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="考试结束时间" prop="examEndTime">
          <el-date-picker
            v-model="editForm.examEndTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            size="small"
            placeholder="选择日期时间"
            class="date-dialog">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <!-- 底部按钮区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="editExamCategory">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  handleAddExamCategory,
  handleDeleteExamCategory,
  handleUpdateExamCategory,
  handleExamCategoryPage,
  handleExamCategoryQuery
} from '@/apis/register/examCategory'
import { showElement } from '@/utils/show'

export default {
  name: 'ExamCategoryPage',
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
        // 考试类别名
        examCategoryName: undefined,
        // 开始时间
        examStartTime: undefined,
        // 结束时间
        examEndTime: undefined,
        // 当前页
        current: 1,
        // 当前页大小
        pageSize: 20
      },
      // 考试类别列表
      examCategoryList: [],
      // 考试类别总数
      total: 0,
      // 控制添加考试类别对话框的显示
      addDialogVisible: false,
      // 添加考试类别表单数据
      addForm: {
        examCategoryName: undefined,
        examStartTime: undefined,
        examEndTime: undefined
      },
      // 控制修改考试类别对话框的显示
      editDialogVisible: false,
      // 修改考试类别表单数据
      editForm: {
        examCategoryId: undefined,
        examCategoryName: undefined,
        examStartTime: undefined,
        examEndTime: undefined
      },
      // 考试类别表单校验规则
      dialogFormRules: {
        examCategoryName: [
          {
            required: true,
            message: '请输入考试类别名',
            trigger: 'blur'
          },
          {
            min: 3,
            max: 20,
            message: '考试类别名长度在3~20字符之间',
            trigger: 'blur'
          }
        ],
        examStartTime: [
          {
            required: true,
            message: '请选择考试开始时间',
            trigger: 'blur'
          }
        ],
        examEndTime: [
          {
            required: true,
            message: '请选择考试结束时间',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  created () {
    this.superAdmin = this.$store.state.superAdmin
    this.getExamCategoryPage()
  },
  updated () {
    this.showTableColumn()
  },
  methods: {
    // 根据权限数据展示操作列
    showTableColumn () {
      this.columnShow = showElement(['system:examCategory:update', 'system:examCategory:deleted', 'system:examCategory:bind'])
    },
    // 查询表单重置
    resetQueryForm () {
      this.queryForm.examCategoryName = undefined
      this.queryForm.examStartTime = undefined
      this.queryForm.examEndTime = undefined
      this.queryForm.current = 1
      this.queryForm.pageSize = 20
      this.getExamCategoryPage()
    },
    // 得到考试类别分页数据
    async getExamCategoryPage () {
      this.queryForm.superAdmin = this.superAdmin
      const { data: res } = await handleExamCategoryPage(this.queryForm)
      console.log(res.data)
      if (res.status !== '2000') return this.$message.error(res.message)
      this.examCategoryList = res.data.records
      this.total = res.data.total
    },
    // 处理当前页大小改变
    handleSizeChange (newSize) {
      this.queryForm.pageSize = newSize
      this.getExamCategoryPage()
    },
    // 处理当前页码改变
    handleCurrentChange (newCurrent) {
      this.queryForm.current = newCurrent
      this.getExamCategoryPage()
    },
    // 监听 添加考试类别对话框关闭事件
    handleAddDialogClose () {
      // 清空字段
      this.$refs.addFormRef.resetFields()
    },
    // 展示添加考试类别对话框
    showAddDialog () {
      this.addDialogVisible = true
    },
    // 添加考试类别
    addExamCategory () {
      this.$refs.addFormRef.validate(async valid => {
        if (valid) {
          const { data: res } = await handleAddExamCategory(this.addForm)
          if (res.status !== '2000') return this.$message.error(res.message)
          this.$message.success(res.message)
          this.addDialogVisible = false
          await this.getExamCategoryPage()
        }
      })
    },
    // 展示编辑考试类别对话框
    async showEditDialog (examCategoryId) {
      const { data: res } = await handleExamCategoryQuery(examCategoryId)
      console.log(res)
      if (res.status !== '2000') {
        return this.$message.error(res.message)
      }
      // 查询出考试类别 在展示编辑框
      this.editDialogVisible = true
      this.editForm = res.data
    },
    // 监听 编辑考试类别对话框关闭事件
    handleEditDialogClose () {
      // 清空字段
      this.$refs.editFormRef.resetFields()
    },
    // 确定编辑考试类别
    editExamCategory () {
      this.$refs.editFormRef.validate(async valid => {
        if (valid) {
          const { data: res } = await handleUpdateExamCategory(this.editForm)
          if (res.status !== '2000') return this.$message.error(res.message)
          this.$message.success(res.message)
          this.editDialogVisible = false
          await this.getExamCategoryPage()
        }
      })
    },
    // 删除考试类别
    deleteExamCategory (examCategoryId) {
      this.$confirm('此操作将删除该考试类别, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        const { data: res } = await handleDeleteExamCategory(examCategoryId)
        if (res.status !== '2000') {
          return this.$message.error(res.message)
        }
        this.$message({
          type: 'success',
          message: res.message
        })
        await this.getExamCategoryPage()
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    // 跳转考试科目
    showSubjectDialog (examCategoryId) {
      this.$router.push({
        path: 'subjectPage',
        query: {
          id: examCategoryId
        }
      })
    }
  }
}
</script>

<style lang="less" scoped>
.date-dialog {
  width: 295px;
}
</style>
