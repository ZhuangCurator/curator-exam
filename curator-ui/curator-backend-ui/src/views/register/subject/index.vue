<template>
  <div class="examSubject_container">
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>考试科目管理</el-breadcrumb-item>
      <el-breadcrumb-item>考试科目列表</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图区域-->
    <el-card class="box-card">
      <!--  搜索与添加区域 -->
      <el-form :model="queryForm" ref="queryFormRef" :inline="true" label-width="100px">
        <el-form-item label="考试科目名称">
          <el-input
            v-model="queryForm.examSubjectName"
            placeholder="请输入考试科目名称"
            clearable
            size="small"
            style="width: 240px"
            @keyup.enter.native="getExamSubjectPage"
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
          <el-button type="info" icon="el-icon-search" size="small" @click="getExamSubjectPage">搜索</el-button>
          <el-button type="warning" icon="el-icon-refresh" size="small" @click="resetQueryForm">重置</el-button>
        </el-form-item>
      </el-form>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-button type="primary" size="mini" v-has-perm="['register:subject:add']" @click="showAddDialog">添加考试科目</el-button>
        </el-col>
      </el-row>

      <!--  考试科目列表区域 -->
      <el-table :data="examSubjectList" border stripe>
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
            </el-form>
          </template>
        </el-table-column>
        <el-table-column label="考试科目名" prop="examSubjectName" align="center"></el-table-column>
        <el-table-column label="报名开始时间" prop="registerStartTime" align="center"></el-table-column>
        <el-table-column label="报名结束时间" prop="registerEndTime" align="center"></el-table-column>
        <el-table-column label="考试开始时间" prop="examStartTime" align="center"></el-table-column>
        <el-table-column label="考试结束时间" prop="examEndTime" align="center"></el-table-column>
        <el-table-column label="考试时长(分钟)" width="120px;" prop="examDuration" align="center"></el-table-column>
        <el-table-column label="及格线" width="70px;" prop="passingScore" align="center"></el-table-column>
        <el-table-column label="操作" width="300px;" align="center" v-if="columnShow">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" size="mini" v-has-perm="['register:subject:update']" @click="showEditDialog(scope.row.examSubjectId)">编辑</el-button>
            <el-button type="danger" icon="el-icon-delete" size="mini" v-has-perm="['register:subject:delete']" @click="deleteExamSubject(scope.row.examSubjectId)">删除</el-button>
            <el-button type="info" icon="el-icon-setting" size="mini" v-has-perm="['subject:site:page']" @click="showSubjectSiteDialog(scope.row.examSubjectId)">考点列表</el-button>
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

    <!-- 添加考试科目对话框 -->
    <el-dialog title="添加考试科目" :visible.sync="addDialogVisible" width="30%" @close="handleAddDialogClose">
      <!-- 对话框主题区域 -->
      <el-form ref="addFormRef" :model="addForm" :rules="dialogFormRules" label-width="120px">
        <el-form-item label="考试科目名" prop="examSubjectName">
          <el-input v-model="addForm.examSubjectName"></el-input>
        </el-form-item>
        <el-form-item label="报名开始时间" prop="registerStartTime">
          <el-date-picker
            v-model="addForm.registerStartTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            size="small"
            placeholder="选择日期时间"
            class="date-dialog">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="报名结束时间" prop="registerEndTime">
          <el-date-picker
            v-model="addForm.registerEndTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            size="small"
            placeholder="选择日期时间"
            class="date-dialog">
          </el-date-picker>
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
        <el-form-item label="试卷生成规则" prop="generationRuleId">
          <el-select v-model="addForm.generationRuleId" placeholder="请选择试卷生成规则">
            <el-option
              v-for="item in generationRuleList"
              :key="item.generationRuleId"
              :label="item.generationRuleName"
              :value="item.generationRuleId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="考试时长" prop="examDuration">
          <el-input v-model="addForm.examDuration"></el-input>
        </el-form-item>
        <el-form-item label="及格分数线" prop="passingScore">
          <el-input v-model="addForm.passingScore"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部按钮区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addExamSubject">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 修改考试科目对话框 -->
    <el-dialog title="编辑考试科目" :visible.sync="editDialogVisible" width="30%" @close="handleEditDialogClose">
      <!-- 对话框主题区域 -->
      <el-form ref="editFormRef" :model="editForm" :rules="dialogFormRules" label-width="120px">
        <el-form-item label="考试科目名" prop="examSubjectName">
          <el-input v-model="editForm.examSubjectName"></el-input>
        </el-form-item>
        <el-form-item label="报名开始时间" prop="registerStartTime">
          <el-date-picker
            v-model="editForm.registerStartTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            size="small"
            placeholder="选择日期时间"
            class="date-dialog">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="报名结束时间" prop="registerEndTime">
          <el-date-picker
            v-model="editForm.registerEndTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            size="small"
            placeholder="选择日期时间"
            class="date-dialog">
          </el-date-picker>
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
        <el-form-item label="试卷生成规则" prop="generationRuleId">
          <el-select v-model="editForm.generationRuleId" placeholder="请选择试卷生成规则">
            <el-option
              v-for="item in generationRuleList"
              :key="item.generationRuleId"
              :label="item.generationRuleName"
              :value="item.generationRuleId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="考试时长" prop="examDuration">
          <el-input v-model="editForm.examDuration"></el-input>
        </el-form-item>
        <el-form-item label="及格分数线" prop="passingScore">
          <el-input v-model="editForm.passingScore"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部按钮区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="editExamSubject">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  handleAddExamSubject,
  handleDeleteExamSubject,
  handleUpdateExamSubject,
  handleExamSubjectPage,
  handleExamSubjectQuery,
  handleGenerationRuleList
} from '@/apis/register/examSubject'
import { showElement } from '@/utils/show'

import { handleRoleTypeList } from '@/apis/info/role'

export default {
  name: 'ExamSubjectPage',
  data () {
    return {
      columnShow: true,
      // 是否是超级管理员
      superAdmin: 0,
      // 考试类别id
      examCategoryId: undefined,
      // 试卷生成规则列表
      generationRuleList: [],
      // 查询表单参数
      queryForm: {
        // 考试科目名
        examSubjectName: undefined,
        // 开始时间
        examStartTime: undefined,
        // 结束时间
        examEndTime: undefined,
        // 当前页
        current: 1,
        // 当前页大小
        pageSize: 20
      },
      // 考试科目列表
      examSubjectList: [],
      // 考试科目总数
      total: 0,
      // 控制添加考试科目对话框的显示
      addDialogVisible: false,
      // 添加考试科目表单数据
      addForm: {
        examCategoryId: undefined,
        examSubjectName: undefined,
        registerStartTime: undefined,
        registerEndTime: undefined,
        examStartTime: undefined,
        examEndTime: undefined,
        generationRuleId: undefined,
        examDuration: undefined,
        passingScore: undefined
      },
      // 控制修改考试科目对话框的显示
      editDialogVisible: false,
      // 修改考试科目表单数据
      editForm: {
        examSubjectId: undefined,
        examSubjectName: undefined,
        registerStartTime: undefined,
        registerEndTime: undefined,
        examStartTime: undefined,
        examEndTime: undefined,
        examDuration: undefined,
        passingScore: undefined
      },
      // 考试科目表单校验规则
      dialogFormRules: {
        examSubjectName: [
          {
            required: true,
            message: '请输入考试科目名',
            trigger: 'blur'
          },
          {
            min: 3,
            max: 20,
            message: '考试科目名长度在3~20字符之间',
            trigger: 'blur'
          }
        ],
        registerStartTime: [
          {
            required: true,
            message: '请选择报名开始时间',
            trigger: 'blur'
          }
        ],
        registerEndTime: [
          {
            required: true,
            message: '请选择报名结束时间',
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
        ],
        generationRuleId: [
          {
            required: true,
            message: '请选择生成试卷规则',
            trigger: 'blur'
          }
        ],
        examDuration: [
          {
            pattern: /^[0-9]+$/,
            message: '请输入正整数',
            trigger: 'blur'
          },
          {
            required: true,
            message: '请输入考试时长(单位： 分钟)',
            trigger: 'blur'
          }
        ],
        passingScore: [
          {
            pattern: /^[0-9]+$/,
            message: '请输入正整数',
            trigger: 'blur'
          },
          {
            required: true,
            message: '请输入及格分数线',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  created () {
    this.superAdmin = this.$store.state.superAdmin
    this.showGenerationRuleList()
  },
  updated () {
    this.showTableColumn()
  },
  methods: {
    // 根据权限数据展示操作列
    showTableColumn () {
      this.columnShow = showElement(['register:subject:update', 'register:subject:delete', 'subject:site:page'])
    },
    // 查询表单重置
    resetQueryForm () {
      this.queryForm.examSubjectName = undefined
      this.queryForm.examStartTime = undefined
      this.queryForm.examEndTime = undefined
      this.queryForm.current = 1
      this.queryForm.pageSize = 20
      this.getExamSubjectPage()
    },
    // 得到考试科目分页数据
    async getExamSubjectPage () {
      this.queryForm.superAdmin = this.superAdmin
      this.queryForm.examCategoryId = this.examCategoryId
      const { data: res } = await handleExamSubjectPage(this.queryForm)
      console.log(res.data)
      if (res.status !== '2000') return this.$message.error(res.message)
      this.examSubjectList = res.data.records
      this.total = res.data.total
    },
    // 处理当前页大小改变
    handleSizeChange (newSize) {
      this.queryForm.pageSize = newSize
      this.getExamSubjectPage()
    },
    // 处理当前页码改变
    handleCurrentChange (newCurrent) {
      this.queryForm.current = newCurrent
      this.getExamSubjectPage()
    },
    // 监听 添加考试科目对话框关闭事件
    handleAddDialogClose () {
      // 清空字段
      this.$refs.addFormRef.resetFields()
    },
    // 展示添加考试科目对话框
    showAddDialog () {
      this.addDialogVisible = true
    },
    // 添加考试科目
    addExamSubject () {
      this.$refs.addFormRef.validate(async valid => {
        if (valid) {
          this.addForm.examCategoryId = this.examCategoryId
          const { data: res } = await handleAddExamSubject(this.addForm)
          if (res.status !== '2000') return this.$message.error(res.message)
          this.$message.success(res.message)
          this.addDialogVisible = false
          await this.getExamSubjectPage()
        }
      })
    },
    // 展示编辑考试科目对话框
    async showEditDialog (examSubjectId) {
      const { data: res } = await handleExamSubjectQuery(examSubjectId)
      console.log(res)
      if (res.status !== '2000') {
        return this.$message.error(res.message)
      }
      // 查询出考试科目 在展示编辑框
      this.editDialogVisible = true
      this.editForm = res.data
    },
    // 监听 编辑考试科目对话框关闭事件
    handleEditDialogClose () {
      // 清空字段
      this.$refs.editFormRef.resetFields()
    },
    // 确定编辑考试科目
    editExamSubject () {
      this.$refs.editFormRef.validate(async valid => {
        if (valid) {
          const { data: res } = await handleUpdateExamSubject(this.editForm)
          if (res.status !== '2000') return this.$message.error(res.message)
          this.$message.success(res.message)
          this.editDialogVisible = false
          await this.getExamSubjectPage()
        }
      })
    },
    // 删除考试科目
    deleteExamSubject (examSubjectId) {
      this.$confirm('此操作将删除该考试科目, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        const { data: res } = await handleDeleteExamSubject(examSubjectId)
        if (res.status !== '2000') {
          return this.$message.error(res.message)
        }
        this.$message({
          type: 'success',
          message: res.message
        })
        await this.getExamSubjectPage()
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    // 获取生成试卷规则列表
    async showGenerationRuleList () {
      const { data: result } = await handleGenerationRuleList()
      if (result.status !== '2000') {
        return this.$message.error(result.message)
      }
      this.generationRuleList = result.data
    },
    // 跳转考试科目下的考点
    showSubjectSiteDialog (examSubjectId) {
      this.$router.push({
        path: 'subjectSitePage',
        query: {
          id: examSubjectId
        }
      })
    }
  },
  mounted () {
    this.examCategoryId = this.$route.query.id
    this.getExamSubjectPage()
  }
}
</script>

<style lang="less" scoped>
.date-dialog {
  width: 295px;
}
.el-select {
  width: 100%;
}
</style>
