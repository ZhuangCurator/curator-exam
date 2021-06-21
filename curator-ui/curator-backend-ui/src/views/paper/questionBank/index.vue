<template>
  <div class="questionBank_container">
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>试题库管理</el-breadcrumb-item>
      <el-breadcrumb-item>试题库列表</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图区域-->
    <el-card class="box-card">
      <!--  搜索与添加区域 -->
      <el-form :model="queryForm" ref="queryFormRef" :inline="true" label-width="85px">
        <el-form-item label="试题库名称">
          <el-input
            v-model="queryForm.questionBankName"
            placeholder="请输入试题库名称"
            clearable
            size="small"
            style="width: 240px"
            @keyup.enter.native="getQuestionBankPage"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="info" icon="el-icon-search" size="small" @click="getQuestionBankPage">搜索</el-button>
          <el-button type="warning" icon="el-icon-refresh" size="small" @click="resetQueryForm">重置</el-button>
        </el-form-item>
      </el-form>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-button type="primary" size="mini" v-has-perm="['paper:questionBank:add']" @click="showAddDialog">添加试题库</el-button>
        </el-col>
      </el-row>

      <!--  试题库列表区域 -->
      <el-table :data="questionBankList" border stripe>
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
        <el-table-column label="试题库名" prop="questionBankName" align="center"></el-table-column>
        <el-table-column label="简介" prop="summary" align="center"></el-table-column>
        <el-table-column label="操作" width="300px;" align="center" v-if="columnShow">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" size="mini" v-has-perm="['paper:questionBank:update']" @click="showEditDialog(scope.row.questionBankId)">编辑</el-button>
            <el-button type="danger" icon="el-icon-delete" size="mini" v-has-perm="['paper:questionBank:delete']" @click="deleteQuestionBank(scope.row.questionBankId)">删除</el-button>
            <el-button type="info" icon="el-icon-setting" size="mini" v-has-perm="['paper:question:bind']" @click="showQuestionView(scope.row.questionBankId)">试题管理</el-button>
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

    <!-- 添加试题库对话框 -->
    <el-dialog title="添加试题库" :visible.sync="addDialogVisible" width="30%" @close="handleAddDialogClose">
      <!-- 对话框主题区域 -->
      <el-form ref="addFormRef" :model="addForm" :rules="dialogFormRules" label-width="80px">
        <el-form-item label="试题库名" prop="questionBankName">
          <el-input v-model="addForm.questionBankName"></el-input>
        </el-form-item>
        <el-form-item label="简介" prop="summary">
          <el-input type="textarea" :rows="2" v-model="addForm.summary"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部按钮区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addQuestionBank">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 修改试题库对话框 -->
    <el-dialog title="编辑试题库" :visible.sync="editDialogVisible" width="30%" @close="handleEditDialogClose">
      <!-- 对话框主题区域 -->
      <el-form ref="editFormRef" :model="editForm" :rules="dialogFormRules" label-width="80px">
        <el-form-item label="试题库名" prop="questionBankName">
          <el-input v-model="editForm.questionBankName"></el-input>
        </el-form-item>
        <el-form-item label="简介" prop="summary">
          <el-input type="textarea" :rows="2" v-model="editForm.summary"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部按钮区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="editQuestionBank">确 定</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
import {
  handleAddQuestionBank,
  handleDeleteQuestionBank,
  handleUpdateQuestionBank,
  handleQuestionBankPage,
  handleQuestionBankQuery
} from '@/apis/paper/questionBank'
import { showElement } from '@/utils/show'

export default {
  name: 'QuestionBankPage',
  data () {
    return {
      columnShow: true,
      // 是否是超级管理员
      superAdmin: 0,
      // 查询表单参数
      queryForm: {
        // 试题库名
        questionBankName: undefined,
        // 当前页
        current: 1,
        // 当前页大小
        pageSize: 20
      },
      // 试题库列表
      questionBankList: [],
      // 试题库总数
      total: 0,
      // 控制添加试题库对话框的显示
      addDialogVisible: false,
      // 添加试题库表单数据
      addForm: {
        questionBankName: undefined,
        summary: undefined
      },
      // 控制修改试题库对话框的显示
      editDialogVisible: false,
      // 修改试题库表单数据
      editForm: {
        questionBankId: undefined,
        questionBankName: undefined,
        summary: undefined
      },
      // 试题库表单校验规则
      dialogFormRules: {
        questionBankName: [
          {
            required: true,
            message: '请输入试题库名',
            trigger: 'blur'
          },
          {
            min: 3,
            max: 20,
            message: '试题库名长度在3~20字符之间',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  created () {
    this.superAdmin = this.$store.state.superAdmin
    this.getQuestionBankPage()
  },
  updated () {
    this.showTableColumn()
  },
  methods: {
    // 根据权限数据展示操作列
    showTableColumn () {
      this.columnShow = showElement(['paper:questionBank:update', 'paper:questionBank:delete', 'paper:question:bind'])
    },
    // 查询表单重置
    resetQueryForm () {
      this.queryForm.questionBankName = undefined
      this.queryForm.current = 1
      this.queryForm.pageSize = 20
      this.getQuestionBankPage()
    },
    // 得到试题库分页数据
    async getQuestionBankPage () {
      this.queryForm.superAdmin = this.superAdmin
      const { data: res } = await handleQuestionBankPage(this.queryForm)
      console.log(res.data)
      if (res.status !== '2000') return this.$message.error(res.message)
      this.questionBankList = res.data.records
      this.total = res.data.total
    },
    // 处理当前页大小改变
    handleSizeChange (newSize) {
      this.queryForm.pageSize = newSize
      this.getQuestionBankPage()
    },
    // 处理当前页码改变
    handleCurrentChange (newCurrent) {
      this.queryForm.current = newCurrent
      this.getQuestionBankPage()
    },
    // 监听 添加试题库对话框关闭事件
    handleAddDialogClose () {
      // 清空字段
      this.$refs.addFormRef.resetFields()
    },
    // 展示添加试题库对话框
    showAddDialog () {
      this.addDialogVisible = true
    },
    // 添加试题库
    addQuestionBank () {
      this.$refs.addFormRef.validate(async valid => {
        if (valid) {
          const { data: res } = await handleAddQuestionBank(this.addForm)
          if (res.status !== '2000') return this.$message.error(res.message)
          this.$message.success(res.message)
          this.addDialogVisible = false
          await this.getQuestionBankPage()
        }
      })
    },
    // 展示编辑试题库对话框
    async showEditDialog (questionBankId) {
      const { data: res } = await handleQuestionBankQuery(questionBankId)
      console.log(res)
      if (res.status !== '2000') {
        return this.$message.error(res.message)
      }
      // 查询出试题库 在展示编辑框
      this.editDialogVisible = true
      this.editForm = res.data
    },
    // 监听 编辑试题库对话框关闭事件
    handleEditDialogClose () {
      // 清空字段
      this.$refs.editFormRef.resetFields()
    },
    // 确定编辑试题库
    editQuestionBank () {
      this.$refs.editFormRef.validate(async valid => {
        if (valid) {
          const { data: res } = await handleUpdateQuestionBank(this.editForm)
          if (res.status !== '2000') return this.$message.error(res.message)
          this.$message.success(res.message)
          this.editDialogVisible = false
          await this.getQuestionBankPage()
        }
      })
    },
    // 删除试题库
    deleteQuestionBank (questionBankId) {
      this.$confirm('此操作将删除该试题库, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        const { data: res } = await handleDeleteQuestionBank(questionBankId)
        if (res.status !== '2000') {
          return this.$message.error(res.message)
        }
        this.$message({
          type: 'success',
          message: res.message
        })
        await this.getQuestionBankPage()
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    // 跳转试题页面
    showQuestionView (questionBankId) {
      this.$router.push({
        path: 'bankQuestionPage',
        query: {
          id: questionBankId
        }
      })
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
