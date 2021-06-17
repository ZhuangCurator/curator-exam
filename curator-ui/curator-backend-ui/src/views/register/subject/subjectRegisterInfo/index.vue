<template>
  <div class="examRegisterInfo_container">
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>考试科目管理</el-breadcrumb-item>
      <el-breadcrumb-item>报名信息管理</el-breadcrumb-item>
      <el-breadcrumb-item>报名信息列表</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图区域-->
    <el-card class="box-card">
      <!--  搜索与添加区域 -->
      <el-form :model="queryForm" ref="queryFormRef" :inline="true" label-width="85px">
        <el-row>
          <el-col :span="8">
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
          </el-col>
          <el-col :span="8">
            <el-form-item label="身份证号">
              <el-input
                v-model="queryForm.idCard"
                placeholder="请输入身份证号"
                clearable
                size="small"
                style="width: 240px"
                @keyup.enter.native="getExamRegisterInfoPage"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="准考证编号">
              <el-input
                v-model="queryForm.admissionNumber"
                placeholder="请输入账户名称"
                clearable
                size="small"
                style="width: 240px"
                @keyup.enter.native="getExamRegisterInfoPage"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="座位号">
              <el-input
                v-model="queryForm.seatNumber"
                placeholder="请输入座位号"
                clearable
                size="small"
                style="width: 240px"
                @keyup.enter.native="getExamRegisterInfoPage"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="考试状态">
              <el-select v-model="queryForm.examStatus" placeholder="请选择考试状态" size="small">
                <el-option :key="0" label="未开始" :value="0"></el-option>
                <el-option :key="1" label="已结束" :value="1"></el-option>
                <el-option :key="2" label="缺考" :value="2"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="是否及格">
              <el-select v-model="queryForm.passed" placeholder="请选择是否及格" size="small">
                <el-option :key="1" label="是" :value="1"></el-option>
                <el-option :key="0" label="否" :value="0"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-form-item>
            <el-button type="info" icon="el-icon-search" size="small" @click="getExamRegisterInfoPage">搜索</el-button>
            <el-button type="warning" icon="el-icon-refresh" size="small" @click="resetQueryForm">重置</el-button>
          </el-form-item>
        </el-row>
      </el-form>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-button type="primary" @click="setExamPassword" v-has-perm="['register:exam:password']">设置口令</el-button>
          <el-button type="primary" @click="assignClassroom" v-has-perm="['register:assign:classroom']">分配教室</el-button>
        </el-col>
      </el-row>
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
        <el-table-column label="教室" prop="examClassroomName" align="center"></el-table-column>
        <el-table-column label="座位号"  width="70px" prop="seatNumber" align="center"></el-table-column>
        <el-table-column label="考试口令" width="80px" prop="examPassword" align="center"></el-table-column>
        <el-table-column label="成绩"  width="70px" prop="score" align="center"></el-table-column>
        <el-table-column label="考试状态" prop="examStatusDesc" align="center"></el-table-column>
        <el-table-column label="及格" width="80px" prop="passed" align="center">
          <template slot-scope="scope" >
            {{ scope.row.passed === 1 ? '及格' : '不及格' }}
          </template>
        </el-table-column>
        <el-table-column label="操作"  align="center" v-if="columnShow">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" size="mini" v-has-perm="['register:people:reExam']" @click="showReExamDialog(scope.row.examRegisterInfoId)">重考</el-button>
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
  handleUpdateExamRegisterInfo,
  handleExamRegisterInfoPage,
  handleGenerateExamPassword, handleAssignClassroom
} from '@/apis/register/examRegister'
import { showElement } from '@/utils/show'

import { handleDeletePowerGroup } from '@/apis/info/powerGroup'

export default {
  name: 'ExamRegisterInfoPage',
  data () {
    return {
      columnShow: true,
      // 是否是超级管理员
      superAdmin: 0,
      // 考试科目id
      examSubjectId: undefined,
      // 考点id
      examSiteId: undefined,
      // 查询表单参数
      queryForm: {
        // 账户名称
        accountName: undefined,
        // 身份证
        idCard: undefined,
        // 准考证编号
        admissionNumber: undefined,
        // 座位号
        seatNumber: undefined,
        // 考试状态(0:未开始考试,1:已结束考试,2:缺考)
        examStatus: undefined,
        // 是否及格（1-是，0-否）
        passed: undefined,
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
  },
  updated () {
    this.showTableColumn()
  },
  methods: {
    // 根据权限数据展示操作列
    showTableColumn () {
      this.columnShow = showElement(['register:info:reExam'])
    },
    // 查询表单重置
    resetQueryForm () {
      this.queryForm.accountName = undefined
      this.queryForm.admissionNumber = undefined
      this.queryForm.seatNumber = undefined
      this.queryForm.examStatus = undefined
      this.queryForm.passed = undefined
      this.queryForm.current = 1
      this.queryForm.pageSize = 20
      this.getExamRegisterInfoPage()
    },
    // 得到报名信息分页数据
    async getExamRegisterInfoPage () {
      this.queryForm.examSubjectId = this.examSubjectId
      this.queryForm.examSiteId = this.examSiteId
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
    // 设置考试口令
    async setExamPassword () {
      const param = { examSubjectId: this.examSubjectId, examSiteId: this.examSiteId }
      this.$confirm('此操作将设置此科目下该考点所有考生的考试口令, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        const { data: res } = await handleGenerateExamPassword(param)
        if (res.status !== '2000') {
          return this.$message.error(res.message)
        }
        this.$message({
          type: 'success',
          message: res.message
        })
        await this.getExamRegisterInfoPage()
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消设置'
        })
      })
    },
    // 考生分配教室
    async assignClassroom () {
      const param = { examSubjectId: this.examSubjectId, examSiteId: this.examSiteId }
      this.$confirm('此操作将为此科目下该考点的考生分配教室，是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        const { data: res } = await handleAssignClassroom(param)
        if (res.status !== '2000') {
          return this.$message.error(res.message)
        }
        this.$message({
          type: 'success',
          message: res.message
        })
        await this.getExamRegisterInfoPage()
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消分配'
        })
      })
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
      const { data: res } = await handleExamRegisterInfoPage(this.editForm)
      if (res.status !== '2000') {
        return this.$message.error(res.message)
      }
      this.$message({
        type: 'success',
        message: res.message
      })
      await this.getExamRegisterInfoPage()
    }
  },
  mounted () {
    this.examSubjectId = this.$route.query.examSubjectId
    this.examSiteId = this.$route.query.examSiteId
    this.getExamRegisterInfoPage()
  }
}
</script>

<style lang="less" scoped>
.date-dialog {
  width: 295px;
}
.el-select {
  width: 240px;
}
</style>
