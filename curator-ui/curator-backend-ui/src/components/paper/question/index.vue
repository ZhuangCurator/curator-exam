<template>
  <div class="question_container">
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>试题管理</el-breadcrumb-item>
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
          <el-button type="primary" size="mini" v-has-perm="['paper:question:add']" @click="showAddDialog">添加试题</el-button>
        </el-col>
      </el-row>

      <!--  试题列表区域 -->
      <el-table :data="questionList" border stripe>
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
        <el-table-column label="操作" width="300px;" align="center" v-if="columnShow">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" size="mini" v-has-perm="['paper:question:update']" @click="showEditDialog(scope.row.questionId)">编辑</el-button>
            <el-button type="danger" icon="el-icon-delete" size="mini" v-has-perm="['paper:question:deleted']" @click="deleteQuestion(scope.row.questionId)">删除</el-button>
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

    <!-- 添加试题对话框 -->
    <el-dialog title="添加试题" :visible.sync="addDialogVisible" width="40%" @close="handleAddDialogClose">
      <!-- 对话框主题区域 -->
      <el-form ref="addFormRef" :model="addForm" :rules="dialogFormRules" label-width="110px">
        <el-form-item label="试题类型" prop="questionType">
          <el-select v-model="addForm.questionType" placeholder="请选择试题类型" size="small" @change="questionTypeChange">
            <el-option :key="1" label="单选" :value="1"></el-option>
            <el-option :key="2" label="多选" :value="2"></el-option>
            <el-option :key="3" label="判断" :value="3"></el-option>
            <el-option :key="4" label="填空" :value="4"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="试题难度" prop="questionDifficulty">
          <el-select v-model="addForm.questionDifficulty" placeholder="请选择试题难度" size="small">
            <el-option :key="1" label="初级" :value="1"></el-option>
            <el-option :key="2" label="中级" :value="2"></el-option>
            <el-option :key="3" label="高级" :value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="试题分数" prop="questionPoint">
          <el-input v-model="addForm.questionPoint"></el-input>
        </el-form-item>
        <el-form-item label="答案是否有序"  v-if="addForm.questionType === 4" prop="ordered">
          <el-radio-group v-model="addForm.ordered">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="试题题干" prop="questionStem">
          <el-input  type="textarea" :rows="2" v-model="addForm.questionStem"></el-input>
        </el-form-item>
        <el-form-item label="试题选项" v-if="addForm.questionType === 1 || addForm.questionType === 2" prop="questionAnswerContent">
          <el-input  placeholder="请输入选项内容" class="question-input" v-model="addForm.questionAnswerContent[index]" v-for="(item, index) in choseOptions" :key="item">
            <template slot="prepend">{{ item }}</template>
          </el-input>
        </el-form-item>
        <el-form-item label="试题答案" v-if="addForm.questionType === 1 || addForm.questionType === 2" prop="questionAnswerRighted">
          <el-checkbox-group v-model="addForm.questionAnswerRighted" :min="0" :max="addForm.questionType === 1 ? 1 : 4">
            <el-checkbox v-for="(item, index) in choseOptions" :label="item" :key="index">{{item}}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="试题答案" v-if="addForm.questionType === 3" prop="questionAnswerRighted">
          <el-radio-group v-model="addForm.questionAnswerRighted[0]">
            <el-radio :label="1">正确</el-radio>
            <el-radio :label="0">错误</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="试题答案" v-if="addForm.questionType === 4" prop="questionAnswerRighted">
          <template v-for="(item, index) of addForm.questionAnswerRighted">
            <div class="question-input" :key="'div:' + index">
              <el-input v-model="addForm.questionAnswerRighted[index]" style="width: 400px" :key="index"></el-input>
              <el-button class="question-button" @click="addQuestionAnswerInput" :key="'add:' + index" type="primary" v-if="index + 1 === addForm.questionAnswerRighted.length">增加</el-button>
              <el-button class="question-button" @click="deleteQuestionAnswerInput(index)" :key="'delete:' + index"  type="danger"  v-if="index !== 0">删除</el-button>
            </div>
          </template>
        </el-form-item>
        <el-form-item label="解析" prop="questionAnalysis">
          <el-input type="textarea" :rows="2" v-model="addForm.questionAnalysis"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部按钮区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addQuestion">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 修改试题对话框 -->
    <el-dialog title="编辑试题" :visible.sync="editDialogVisible" width="30%" @close="handleEditDialogClose">
      <!-- 对话框主题区域 -->
      <el-form ref="editFormRef" :model="editForm" :rules="dialogFormRules" label-width="80px">
        <el-form-item label="题干" prop="questionStem">
          <el-input v-model="editForm.questionStem"></el-input>
        </el-form-item>
        <el-form-item label="解析" prop="questionAnalysis">
          <el-input type="textarea" :rows="2" v-model="editForm.questionAnalysis"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部按钮区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="editQuestion">确 定</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
import {
  handleAddQuestion,
  handleDeleteQuestion,
  handleUpdateQuestion,
  handleQuestionPage,
  handleQuestionQuery
} from '@/apis/paper/question'
import { showElement } from '@/utils/show'
import { getSuperAdmin } from '@/utils/storage'

export default {
  name: 'questionPage',
  data () {
    return {
      columnShow: true,
      // 是否是超级管理员
      superAdmin: 0,
      // 试题库ID
      questionBankId: undefined,
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
      // 控制添加试题对话框的显示
      addDialogVisible: false,
      // 添加试题表单数据
      addForm: {
        questionStem: undefined,
        questionAnalysis: undefined,
        questionDifficulty: undefined,
        questionType: 1,
        questionPoint: undefined,
        ordered: 0,
        // 答案选项内容
        questionAnswerContent: [],
        // 正确答案
        questionAnswerRighted: []
      },
      // 控制修改试题对话框的显示
      editDialogVisible: false,
      // 修改试题表单数据
      editForm: {
        questionId: undefined,
        questionStem: undefined,
        questionAnalysis: undefined
      },
      // 试题表单校验规则
      dialogFormRules: {
        questionType: [
          {
            required: true,
            message: '请选择试题类型',
            trigger: 'blur'
          }
        ],
        questionDifficulty: [
          {
            required: true,
            message: '请选择试题难度',
            trigger: 'blur'
          }
        ],
        ordered: [
          {
            required: true,
            message: '请选择答案是否有序',
            trigger: 'blur'
          }
        ],
        questionPoint: [
          {
            pattern: /^[0-9]+$/,
            message: '请输入正整数',
            trigger: 'blur'
          },
          {
            required: true,
            message: '请输入试题分数',
            trigger: 'blur'
          }
        ],
        questionStem: [
          {
            required: true,
            message: '请输入题干',
            trigger: 'blur'
          }
        ],
        questionAnswerContent: [
          {
            type: 'array',
            required: true,
            message: '请输入选项内容',
            trigger: 'blur'
          }
        ],
        questionAnswerRighted: [
          {
            type: 'array',
            required: true,
            message: '请选择正确答案',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  created () {
    this.superAdmin = getSuperAdmin()
    this.getQuestionPage()
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
      const { data: res } = await handleQuestionPage(this.queryForm)
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
    // 监听 添加试题对话框关闭事件
    handleAddDialogClose () {
      // 清空字段
      this.$refs.addFormRef.resetFields()
      // // 重置正确答案数组
      // this.addForm.questionAnswerRighted = []
      // // 重置选项内容数组
      // this.addForm.questionAnswerContent = []
    },
    // 展示添加试题对话框
    showAddDialog () {
      this.addDialogVisible = true
    },
    // 添加试题
    addQuestion () {
      this.$refs.addFormRef.validate(async valid => {
        if (valid) {
          const { data: res } = await handleAddQuestion(this.addForm)
          if (res.status !== '2000') return this.$message.error(res.message)
          this.$message.success(res.message)
          this.addDialogVisible = false
          await this.getQuestionPage()
        }
      })
    },
    // 展示编辑试题对话框
    async showEditDialog (questionId) {
      const { data: res } = await handleQuestionQuery(questionId)
      console.log(res)
      if (res.status !== '2000') {
        return this.$message.error(res.message)
      }
      // 查询出试题 在展示编辑框
      this.editDialogVisible = true
      this.editForm = res.data
    },
    // 监听 编辑试题对话框关闭事件
    handleEditDialogClose () {
      // 清空字段
      this.$refs.editFormRef.resetFields()
    },
    // 确定编辑试题
    editQuestion () {
      this.$refs.editFormRef.validate(async valid => {
        if (valid) {
          const { data: res } = await handleUpdateQuestion(this.editForm)
          if (res.status !== '2000') return this.$message.error(res.message)
          this.$message.success(res.message)
          this.editDialogVisible = false
          await this.getQuestionPage()
        }
      })
    },
    // 删除试题
    deleteQuestion (questionId) {
      this.$confirm('此操作将删除该试题, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        const { data: res } = await handleDeleteQuestion(questionId)
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
    // 当添加试题时 试题类型发生改变
    questionTypeChange (questionType) {
      // 重置正确答案数组
      this.addForm.questionAnswerRighted = []
      // 重置选项内容数组
      this.addForm.questionAnswerContent = []
      if (questionType === 4) {
        this.addForm.questionAnswerRighted.push('')
      }
    },
    addQuestionAnswerInput () {
      this.addForm.questionAnswerRighted.push('')
    },
    deleteQuestionAnswerInput (index) {
      this.addForm.questionAnswerRighted.splice(index, 1)
    },
    // 跳转试题页面
    showQuestionView (questionId) {
      this.$router.push({
        path: 'questionPage',
        query: {
          id: questionId
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
.question-input {
  margin-bottom: 10px;
}
.question-button {
  margin-left: 10px;
}
</style>
