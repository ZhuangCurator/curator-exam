<template>
  <div class="examRegisterInfo_container">
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>报名信息管理</el-breadcrumb-item>
      <el-breadcrumb-item>报名信息列表</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图区域-->
    <el-card class="box-card">
      <!--  搜索与添加区域 -->
      <el-form :model="queryForm" ref="queryFormRef" :inline="true" label-width="70px">
        <el-form-item label="账户名称">
          <el-input
            v-model="queryForm.accountName"
            placeholder="请输入账户名称"
            clearable
            size="small"
            style="width: 240px"
            @keyup.enter.native="getExamRegisterInfoPage"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="info" icon="el-icon-search" size="small" @click="getExamRegisterInfoPage">搜索</el-button>
          <el-button type="warning" icon="el-icon-refresh" size="small" @click="resetQueryForm">重置</el-button>
        </el-form-item>
      </el-form>

      <!--  报名信息列表区域 -->
      <el-table :data="examRegisterInfoList" border stripe>
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
        <el-table-column label="账户" prop="accountName" align="center"></el-table-column>
        <el-table-column label="身份证号" prop="idCard" width="150px" align="center"></el-table-column>
        <el-table-column label="准考证编号" prop="admissionNumber" width="150px" align="center"></el-table-column>
        <el-table-column label="考试类别" width="170px;" prop="examCategoryName" align="center"></el-table-column>
        <el-table-column label="考试科目" prop="examSubjectName" align="center"></el-table-column>
        <el-table-column label="考点" prop="examSiteName" align="center"></el-table-column>
        <el-table-column label="教室" prop="examClassroomName" align="center"></el-table-column>
        <el-table-column label="座位号" prop="seatNumber" align="center"></el-table-column>
        <el-table-column label="考试口令" prop="examPassword" align="center"></el-table-column>
        <el-table-column label="成绩" prop="score" align="center"></el-table-column>
        <el-table-column label="考试状态" prop="examStatusDesc" align="center"></el-table-column>
        <el-table-column label="及格" prop="passed" align="center">
          <template slot-scope="scope">
            {{ scope.row.passed === 1 ? '及格' : '不及格' }}
          </template>
        </el-table-column>
        <el-table-column label="操作"  width="250px" align="center">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" size="mini" @click="showReExamDialog(scope.row.examRegisterInfoId)">重考</el-button>
            <el-button type="primary" icon="el-icon-edit" size="mini" @click="showReviewExam(scope.row)">考试回顾</el-button>
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
    <!-- 考生重考对话框 -->
    <el-dialog title="考生重考" :visible.sync="reExamDialogVisible" width="30%" @close="handleReExamDialogClose">
      <!-- 对话框主题区域 -->
      <el-form ref="reExamFormRef" :model="editForm" label-width="80px">
        <el-form-item label="重考类型">
          <el-input v-model="editForm.examRegisterInfoId" style="display:none"></el-input>
          <el-radio-group v-model="editForm.paperStatus">
            <el-radio :label="3">新卷重考</el-radio>
            <el-radio :label="4">原卷重考</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <!-- 底部按钮区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="reExamDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleReExamDialogConfirm">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  handleTestPaperQuery,
  handleExamRegisterInfoPage,
  handleReExam
} from '@/apis/register/examRegister'
export default {
  name: 'ExamRegisterInfoPage',
  data () {
    return {
      // 是否是超级管理员
      superAdmin: 0,
      // 查询表单参数
      queryForm: {
        // 账户名称
        accountName: undefined,
        // 当前页
        current: 1,
        // 当前页大小
        pageSize: 20
      },
      // 报名信息列表
      examRegisterInfoList: [],
      // 报名信息总数
      total: 0,
      // 修改报名信息表单数据
      editForm: {
        examRegisterInfoId: undefined,
        paperStatus: undefined
      },
      // 控制修改权限组对话框的显示
      reExamDialogVisible: false
    }
  },
  created () {
    this.superAdmin = this.$store.state.superAdmin
    this.getExamRegisterInfoPage()
  },
  methods: {
    // 查询表单重置
    resetQueryForm () {
      this.queryForm.accountName = undefined
      this.queryForm.current = 1
      this.queryForm.pageSize = 20
      this.getExamRegisterInfoPage()
    },
    // 得到报名信息分页数据
    async getExamRegisterInfoPage () {
      const { data: res } = await handleExamRegisterInfoPage(this.queryForm)
      console.log(res.data)
      if (res.status !== '2000') return this.$message.error(res.message)
      this.examRegisterInfoList = res.data.records
      this.total = res.data.total
    },
    // 处理当前页大小改变
    handleSizeChange (newSize) {
      this.queryForm.pageSize = newSize
      this.getExamRegisterInfoPage()
    },
    // 处理当前页码改变
    handleCurrentChange (newCurrent) {
      this.queryForm.current = newCurrent
      this.getExamRegisterInfoPage()
    },
    // 展示重考对话框
    showReExamDialog (examRegisterInfoId) {
      this.reExamDialogVisible = true
      this.editForm.examRegisterInfoId = examRegisterInfoId
    },
    // 重考对话框关闭
    handleReExamDialogClose () {
      this.editForm.examRegisterInfoId = undefined
      this.editForm.paperStatus = undefined
    },
    // 重考对话框确定
    async handleReExamDialogConfirm () {
      const { data: res } = await handleReExam(this.editForm)
      if (res.status !== '2000') {
        return this.$message.error(res.message)
      }
      this.$message({
        type: 'success',
        message: res.message
      })
      this.reExamDialogVisible = false
      await this.getExamRegisterInfoPage()
    },
    // 跳转到试卷界面
    async showReviewExam (info) {
      if (info.examStatus === 0) {
        this.$message({
          type: 'error',
          message: '该考生还未开始考试,不允许进行此操作'
        })
      } else if (info.examStatus === 2) {
        this.$message({
          type: 'error',
          message: '该考生缺席此次考试,不允许进行此操作'
        })
      } else {
        const { data: res } = await handleTestPaperQuery(info.examRegisterInfoId)
        if (res.status !== '2000') {
          return this.$message.error(res.message)
        }
        console.log(res)
        await this.$router.push({
          path: '/paper/paperPage',
          query: {
            e: info.examRegisterInfoId,
            p: res.data,
            g: info.generationRuleId
          }
        })
      }
    }
  }
}
</script>

<style lang="less" scoped>
.date-dialog {
  width: 295px;
}
</style>
