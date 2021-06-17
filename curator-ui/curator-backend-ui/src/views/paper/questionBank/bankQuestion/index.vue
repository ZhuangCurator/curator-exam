<template>
  <div class="question_container">
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>试题库管理</el-breadcrumb-item>
      <el-breadcrumb-item>试题列表</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图区域-->
    <el-card class="box-card">
      <!--  搜索与添加区域 -->
      <el-form :model="queryForm" ref="queryFormRef" :inline="true" label-width="85px">
        <el-form-item label="题干" label-width="50px">
          <el-input
            v-model="queryForm.questionStem"
            placeholder="请输入题干"
            clearable
            size="small"
            style="width: 240px"
            @keyup.enter.native="getQuestionPage"
          />
        </el-form-item>
        <el-form-item label="试题类型">
          <el-select v-model="queryForm.questionType" placeholder="请选择试题类型" size="small">
            <el-option :key="1" label="单选" :value="1"></el-option>
            <el-option :key="2" label="多选" :value="2"></el-option>
            <el-option :key="3" label="判断" :value="3"></el-option>
            <el-option :key="4" label="填空" :value="4"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="试题难度">
          <el-select v-model="queryForm.questionDifficulty" placeholder="请选择试题难度" size="small">
            <el-option :key="1" label="初级" :value="1"></el-option>
            <el-option :key="2" label="中级" :value="2"></el-option>
            <el-option :key="3" label="高级" :value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="info" icon="el-icon-search" size="small" @click="getQuestionPage">搜索</el-button>
          <el-button type="warning" icon="el-icon-refresh" size="small" @click="resetQueryForm">重置</el-button>
        </el-form-item>
      </el-form>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-button type="primary" size="mini" v-has-perm="['paper:bankQuestion:delete']" @click="batchDeleteQuestion">批量删除</el-button>
        </el-col>
      </el-row>

      <!--  试题列表区域 -->
      <el-table :data="questionList" border stripe @selection-change="handleSelectionChange">
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
        <el-table-column type="selection" width="35"></el-table-column>
        <el-table-column label="题干" prop="questionStem" align="center"></el-table-column>
        <el-table-column label="试题难度" width="70px" prop="questionDifficultyDesc" align="center"></el-table-column>
        <el-table-column label="试题类型" width="70px" prop="questionTypeDesc" align="center"></el-table-column>
        <el-table-column label="分数" width="55px" prop="questionPoint" align="center"></el-table-column>
        <el-table-column label="答案是否有序" width="100px" align="center">
          <template slot-scope="scope" >
            {{ scope.row.ordered === 1 ? '有序' : '无序' }}
          </template>
        </el-table-column>
        <el-table-column label="解析" width="200px" prop="questionAnalysis" align="center"></el-table-column>
        <el-table-column label="操作" width="150px;" align="center" v-if="columnShow">
          <template slot-scope="scope">
            <el-button type="info" icon="el-icon-setting" size="mini" v-has-perm="['paper:preview:question']" @click="showPreviewDialog(scope.row.questionId)">预览试题</el-button>
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
    <!-- 预览试题对话框 -->
    <el-dialog title="预览试题" :visible.sync="previewDialogVisible" width="50%">
      <!-- 对话框主题区域 -->
      <el-row>
        <h3>{{ editForm.questionStem }} ({{editForm.questionPoint}}分)</h3>
      </el-row>
      <el-row v-if="editForm.questionType === 4">
        正确答案：<el-tag  v-for="(item, index) in editForm.questionAnswerRightedList" :key="index">{{ item }}</el-tag>
      </el-row>
      <el-row v-if="editForm.questionType === 1 || editForm.questionType === 2">
          <el-checkbox-group v-model="editForm.questionAnswerRightedList">
            <el-checkbox v-for="(item, index) in editForm.questionAnswerContentList" :label="choseOptions[index]" :key="index" disabled>{{item}}</el-checkbox>
          </el-checkbox-group>
      </el-row>
      <el-row v-if="editForm.questionType === 3">
        <el-checkbox-group v-model="editForm.questionAnswerRightedList">
          <el-checkbox  :label="'1'"  disabled>正确</el-checkbox>
          <el-checkbox  :label="'0'"  disabled>错误</el-checkbox>
        </el-checkbox-group>
      </el-row>
      <el-row class="analysis-row">
        解析：<span class="analysis-span">{{ editForm.questionAnalysis }}</span>
      </el-row>
      <!-- 底部按钮区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="previewDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  handleQuestionQuery
} from '@/apis/paper/question'
import { handleBankQuestionPage, handleDeleteBankQuestion } from '@/apis/paper/questionBank'
import { showElement } from '@/utils/show'

export default {
  name: 'BankQuestionPage',
  data () {
    return {
      columnShow: true,
      // 是否是超级管理员
      superAdmin: 0,
      // 试题库ID
      questionBankId: undefined,
      // 试题id集合
      questionIdList: [],
      // 查询表单参数
      queryForm: {
        // 题干
        questionStem: undefined,
        // 试题类型
        questionType: undefined,
        // 试题难度
        questionDifficulty: undefined,
        // 当前页
        current: 1,
        // 当前页大小
        pageSize: 20
      },
      // 试题列表
      questionList: [],
      // 试题总数
      total: 0,
      // 选择题选项
      choseOptions: ['A', 'B', 'C', 'D'],
      // 修改试题表单数据
      editForm: {
        questionId: undefined,
        questionStem: undefined,
        questionAnalysis: undefined,
        questionDifficulty: undefined,
        questionType: undefined,
        questionPoint: undefined,
        ordered: undefined,
        // 答案选项内容
        questionAnswerContentList: [],
        // 正确答案
        questionAnswerRightedList: []
      },
      // 控制预览试题对话框的显示
      previewDialogVisible: false
    }
  },
  created () {
    this.superAdmin = this.$store.state.superAdmin
  },
  updated () {
    this.showTableColumn()
  },
  methods: {
    // 根据权限数据展示操作列
    showTableColumn () {
      this.columnShow = showElement(['system:question:update', 'system:question:deleted', 'system:question:bind'])
    },
    // 查询表单重置
    resetQueryForm () {
      this.queryForm.questionStem = undefined
      this.queryForm.questionDifficulty = undefined
      this.queryForm.questionType = undefined
      this.queryForm.current = 1
      this.queryForm.pageSize = 20
      this.getQuestionPage()
    },
    // 得到试题分页数据
    async getQuestionPage () {
      this.queryForm.superAdmin = this.superAdmin
      this.queryForm.questionBankId = this.questionBankId
      const { data: res } = await handleBankQuestionPage(this.queryForm)
      console.log(res.data)
      if (res.status !== '2000') return this.$message.error(res.message)
      this.questionList = res.data.records
      this.total = res.data.total
    },
    // 处理当前页大小改变
    handleSizeChange (newSize) {
      this.queryForm.pageSize = newSize
      this.getQuestionPage()
    },
    // 处理当前页码改变
    handleCurrentChange (newCurrent) {
      this.queryForm.current = newCurrent
      this.getQuestionPage()
    },
    // 删除试题
    batchDeleteQuestion () {
      if (this.questionIdList.length === 0) {
        return this.$message.error('请先选择试题')
      }
      this.$confirm('此操作将从此试题库删除这些试题, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        const param = { questionBankId: this.questionBankId, questionIdList: this.questionIdList }
        const { data: res } = await handleDeleteBankQuestion(param)
        if (res.status !== '2000') {
          return this.$message.error(res.message)
        }
        this.$message({
          type: 'success',
          message: res.message
        })
        await this.getQuestionPage()
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    // 展示预览试题对话框
    async showPreviewDialog (questionId) {
      const { data: res } = await handleQuestionQuery(questionId)
      console.log(res)
      if (res.status !== '2000') {
        return this.$message.error(res.message)
      }
      // 查询出试题 在展示预览框
      this.previewDialogVisible = true
      this.editForm = res.data
    },
    // 首行复选框选中触发
    handleSelectionChange (values) {
      this.questionIdList = []
      values.some(val => {
        this.questionIdList.push(val.questionId)
      })
    }
  },
  mounted () {
    this.questionBankId = this.$route.query.id
    this.getQuestionPage()
  }
}
</script>

<style lang="less" scoped>
.el-select {
  width: 100%;
}
.el-tag {
  margin-right: 10px;
}
.el-checkbox {
  display: block;
  color: #9a6e3a;
}
.question-input {
  margin-bottom: 10px;
}
.question-button {
  margin-left: 10px;
}
.analysis-row {
  margin-top: 20px;
}
.analysis-span {
  font-weight: bold;
}
</style>
