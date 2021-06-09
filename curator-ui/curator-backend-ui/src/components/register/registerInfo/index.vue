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
        <el-table-column label="考试类别" width="170px;" prop="examCategoryName" align="center"></el-table-column>
        <el-table-column label="考试科目" prop="examSubjectName" align="center"></el-table-column>
        <el-table-column label="考点" prop="examSiteName" align="center"></el-table-column>
        <el-table-column label="教室" prop="examClassroomName" align="center"></el-table-column>
        <el-table-column label="座位号" prop="seatNumber" align="center"></el-table-column>
        <el-table-column label="准考证编号" prop="admissionNumber" align="center"></el-table-column>
        <el-table-column label="考试口令" prop="examPassword" align="center"></el-table-column>
        <el-table-column label="成绩" prop="score" align="center"></el-table-column>
        <el-table-column label="考试状态" prop="examStatusDesc" align="center"></el-table-column>
        <el-table-column label="及格" prop="passed" align="center">
          <template slot-scope="scope">
            {{ scope.row.passed === 1 ? '及格' : '不及格' }}
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
  </div>
</template>

<script>
import {
  handleUpdateExamRegisterInfo,
  handleExamRegisterInfoPage,
  handleExamRegisterInfoQuery
} from '@/apis/register/examRegister'
import { showElement } from '@/utils/show'
import { getSuperAdmin } from '@/utils/storage'

export default {
  name: 'ExamRegisterInfoPage',
  data () {
    return {
      columnShow: true,
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
      total: 0
    }
  },
  created () {
    this.superAdmin = getSuperAdmin()
    this.getExamRegisterInfoPage()
  },
  updated () {
    this.showTableColumn()
  },
  methods: {
    // 根据权限数据展示操作列
    showTableColumn () {
      this.columnShow = showElement(['system:examRegisterInfo:update', 'system:examRegisterInfo:deleted', 'system:examRegisterInfo:bind'])
    },
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
    }
  }
}
</script>

<style lang="less" scoped>
.date-dialog {
  width: 295px;
}
</style>
