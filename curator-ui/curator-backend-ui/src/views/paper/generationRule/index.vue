<template>
  <div class="generationRule_container">
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>组卷规则管理</el-breadcrumb-item>
      <el-breadcrumb-item>组卷规则列表</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图区域-->
    <el-card class="box-card">
      <!--  搜索与添加区域 -->
      <el-form :model="queryForm" ref="queryFormRef" :inline="true" label-width="100px">
        <el-form-item label="组卷规则名称">
          <el-input
            v-model="queryForm.generationRuleName"
            placeholder="请输入组卷规则名称"
            clearable
            size="small"
            style="width: 240px"
            @keyup.enter.native="getGenerationRulePage"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="info" icon="el-icon-search" size="small" @click="getGenerationRulePage">搜索</el-button>
          <el-button type="warning" icon="el-icon-refresh" size="small" @click="resetQueryForm">重置</el-button>
        </el-form-item>
      </el-form>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-button type="primary" size="mini" v-has-perm="['paper:generationRule:add']" @click="showAddDialog">添加组卷规则</el-button>
        </el-col>
      </el-row>

      <!--  组卷规则列表区域 -->
      <el-table :data="generationRuleList" border stripe>
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
        <el-table-column label="组卷规则名" prop="generationRuleName" align="center"></el-table-column>
        <el-table-column label="试题库" prop="questionBankName" align="center"></el-table-column>
        <el-table-column label="试卷总分" prop="testPaperPoint" align="center"></el-table-column>
        <el-table-column label="简介" prop="summary" align="center"></el-table-column>
        <el-table-column label="操作" width="300px;" align="center" v-if="columnShow">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" size="mini" v-has-perm="['paper:generationRule:update']" @click="showEditDialog(scope.row.generationRuleId)">编辑</el-button>
            <el-button type="danger" icon="el-icon-delete" size="mini" v-has-perm="['paper:generationRule:delete']" @click="deleteGenerationRule(scope.row.generationRuleId)">删除</el-button>
            <el-button type="info" icon="el-icon-setting" size="mini" v-has-perm="['paper:ruleDetail:page']" @click="showRuleDetailView(scope.row.generationRuleId)">规则详情</el-button>
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

    <!-- 添加组卷规则对话框 -->
    <el-dialog title="添加组卷规则" :visible.sync="addDialogVisible" width="30%" @close="handleAddDialogClose">
      <!-- 对话框主题区域 -->
      <el-form ref="addFormRef" :model="addForm" :rules="dialogFormRules" label-width="100px">
        <el-form-item label="组卷规则名" prop="generationRuleName">
          <el-input v-model="addForm.generationRuleName"></el-input>
        </el-form-item>
        <el-form-item label="试题库" prop="questionBankId">
          <el-select v-model="addForm.questionBankId" placeholder="请选择试题库">
            <el-option
              v-for="item in questionBankList"
              :key="item.questionBankId"
              :label="item.questionBankName"
              :value="item.questionBankId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="简介" prop="summary">
          <el-input type="textarea" :rows="2" v-model="addForm.summary"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部按钮区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addGenerationRule">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 修改组卷规则对话框 -->
    <el-dialog title="编辑组卷规则" :visible.sync="editDialogVisible" width="30%" @close="handleEditDialogClose">
      <!-- 对话框主题区域 -->
      <el-form ref="editFormRef" :model="editForm" :rules="dialogFormRules" label-width="100px">
        <el-form-item label="组卷规则名" prop="generationRuleName">
          <el-input v-model="editForm.generationRuleName"></el-input>
        </el-form-item>
        <el-form-item label="试题库" prop="questionBankId">
          <el-select v-model="editForm.questionBankId" placeholder="请选择试题库">
            <el-option
              v-for="item in questionBankList"
              :key="item.questionBankId"
              :label="item.questionBankName"
              :value="item.questionBankId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="简介" prop="summary">
          <el-input type="textarea" :rows="2" v-model="editForm.summary"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部按钮区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="editGenerationRule">确 定</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
import {
  handleAddGenerationRule,
  handleDeleteGenerationRule,
  handleUpdateGenerationRule,
  handleGenerationRulePage,
  handleGenerationRuleQuery
} from '@/apis/paper/generationRule'
import { handleQuestionBankList } from '@/apis/paper/questionBank'
import { showElement } from '@/utils/show'

import { handleGenerationRuleList } from '@/apis/register/examSubject'

export default {
  name: 'GenerationRulePage',
  data () {
    return {
      columnShow: true,
      // 是否是超级管理员
      superAdmin: 0,
      // 试题库列表
      questionBankList: [],
      // 查询表单参数
      queryForm: {
        // 组卷规则名
        generationRuleName: undefined,
        // 当前页
        current: 1,
        // 当前页大小
        pageSize: 20
      },
      // 组卷规则列表
      generationRuleList: [],
      // 组卷规则总数
      total: 0,
      // 控制添加组卷规则对话框的显示
      addDialogVisible: false,
      // 添加组卷规则表单数据
      addForm: {
        generationRuleName: undefined,
        questionBankId: undefined,
        summary: undefined
      },
      // 控制修改组卷规则对话框的显示
      editDialogVisible: false,
      // 修改组卷规则表单数据
      editForm: {
        generationRuleId: undefined,
        generationRuleName: undefined,
        questionBankId: undefined,
        summary: undefined
      },
      // 组卷规则表单校验规则
      dialogFormRules: {
        generationRuleName: [
          {
            required: true,
            message: '请输入组卷规则名',
            trigger: 'blur'
          },
          {
            min: 3,
            max: 20,
            message: '组卷规则名长度在3~20字符之间',
            trigger: 'blur'
          }
        ],
        questionBankId: [
          {
            required: true,
            message: '请选择试题库',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  created () {
    this.superAdmin = this.$store.state.superAdmin
    this.showQuestionBankList()
    this.getGenerationRulePage()
  },
  updated () {
    this.showTableColumn()
  },
  methods: {
    // 根据权限数据展示操作列
    showTableColumn () {
      this.columnShow = showElement(['paper:generationRule:update', 'paper:generationRule:delete', 'paper:ruleDetail:page'])
    },
    // 查询表单重置
    resetQueryForm () {
      this.queryForm.generationRuleName = undefined
      this.queryForm.current = 1
      this.queryForm.pageSize = 20
      this.getGenerationRulePage()
    },
    // 得到组卷规则分页数据
    async getGenerationRulePage () {
      this.queryForm.superAdmin = this.superAdmin
      const { data: res } = await handleGenerationRulePage(this.queryForm)
      console.log(res.data)
      if (res.status !== '2000') return this.$message.error(res.message)
      this.generationRuleList = res.data.records
      this.total = res.data.total
    },
    // 处理当前页大小改变
    handleSizeChange (newSize) {
      this.queryForm.pageSize = newSize
      this.getGenerationRulePage()
    },
    // 处理当前页码改变
    handleCurrentChange (newCurrent) {
      this.queryForm.current = newCurrent
      this.getGenerationRulePage()
    },
    // 监听 添加组卷规则对话框关闭事件
    handleAddDialogClose () {
      // 清空字段
      this.$refs.addFormRef.resetFields()
    },
    // 展示添加组卷规则对话框
    showAddDialog () {
      this.addDialogVisible = true
    },
    // 添加组卷规则
    addGenerationRule () {
      this.$refs.addFormRef.validate(async valid => {
        if (valid) {
          const { data: res } = await handleAddGenerationRule(this.addForm)
          if (res.status !== '2000') return this.$message.error(res.message)
          this.$message.success(res.message)
          this.addDialogVisible = false
          await this.getGenerationRulePage()
        }
      })
    },
    // 展示编辑组卷规则对话框
    async showEditDialog (generationRuleId) {
      const { data: res } = await handleGenerationRuleQuery(generationRuleId)
      console.log(res)
      if (res.status !== '2000') {
        return this.$message.error(res.message)
      }
      // 查询出组卷规则 在展示编辑框
      this.editDialogVisible = true
      this.editForm = res.data
    },
    // 监听 编辑组卷规则对话框关闭事件
    handleEditDialogClose () {
      // 清空字段
      this.$refs.editFormRef.resetFields()
    },
    // 确定编辑组卷规则
    editGenerationRule () {
      this.$refs.editFormRef.validate(async valid => {
        if (valid) {
          const { data: res } = await handleUpdateGenerationRule(this.editForm)
          if (res.status !== '2000') return this.$message.error(res.message)
          this.$message.success(res.message)
          this.editDialogVisible = false
          await this.getGenerationRulePage()
        }
      })
    },
    // 删除组卷规则
    deleteGenerationRule (generationRuleId) {
      this.$confirm('此操作将删除该组卷规则, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        const { data: res } = await handleDeleteGenerationRule(generationRuleId)
        if (res.status !== '2000') {
          return this.$message.error(res.message)
        }
        this.$message({
          type: 'success',
          message: res.message
        })
        await this.getGenerationRulePage()
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    // 获取试题库列表
    async showQuestionBankList () {
      const param = { superAdmin: this.superAdmin }
      const { data: result } = await handleQuestionBankList(param)
      if (result.status !== '2000') {
        return this.$message.error(result.message)
      }
      this.questionBankList = result.data
    },
    // 跳转规则详情
    showRuleDetailView (generationRuleId) {
      this.$router.push({
        path: 'ruleDetailPage',
        query: {
          id: generationRuleId
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
