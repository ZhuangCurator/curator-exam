<template>
  <div class="log_container">
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>日志管理</el-breadcrumb-item>
      <el-breadcrumb-item>日志列表</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图区域-->
    <el-card class="box-card">
      <!--  搜索与添加区域 -->
      <el-form :model="queryForm" ref="queryFormRef" inline label-width="90px">
        <el-row>
          <el-col :span="8">
            <el-form-item label="应用名称">
              <el-input v-model="queryForm.applicationName" placeholder="请输入应用名称" clearable size="small"
                        style="width: 240px" @keyup.enter.native="getLogPage"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="控制器名称">
              <el-input v-model="queryForm.controllerName" placeholder="请输入控制器名称" clearable size="small"
                        style="width: 240px" @keyup.enter.native="getLogPage"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="方法名">
              <el-input v-model="queryForm.method" placeholder="请输入方法名" clearable size="small"
                        style="width: 240px" @keyup.enter.native="getLogPage"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="请求状态">
              <el-select v-model="queryForm.status" placeholder="请选择请求状态" size="small" class="status-select">
                <el-option :key="1" label="正常" :value="1"></el-option>
                <el-option :key="2" label="异常" :value="2"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="请求方式">
              <el-input v-model="queryForm.requestMethod" placeholder="请输入请求方式" clearable size="small"
                        style="width: 240px" @keyup.enter.native="getLogPage"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="请求URL">
              <el-input v-model="queryForm.requestUrl" placeholder="请输入请求URL" clearable size="small"
                        style="width: 240px" @keyup.enter.native="getLogPage"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-form-item label="操作时间">
            <el-date-picker
              v-model="operateTimeArray"
              type="datetimerange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="yyyy-MM-dd HH:mm:ss"
              size="small">
            </el-date-picker>
          </el-form-item>
          <el-form-item class="button_container">
            <el-button type="info" icon="el-icon-search" size="small" @click="getLogPage">搜索</el-button>
            <el-button type="warning" icon="el-icon-refresh" size="small" @click="resetQueryForm">重置</el-button>
          </el-form-item>
        </el-row>
      </el-form>

      <!--  日志列表区域 -->
      <el-table :data="formDataList" border stripe>
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-form label-position="left" class="table-expand">
              <el-form-item label="请求参数">
                <span>{{ props.row.requestParam }}</span>
              </el-form-item>
              <el-form-item label="返回结果">
                <span>{{ props.row.resultResponse }}</span>
              </el-form-item>
              <el-form-item label="异常消息">
                <span>{{ props.row.errorMsg }}</span>
              </el-form-item>
              <el-form-item label="备注">
                <span>{{ props.row.remark }}</span>
              </el-form-item>
              <el-form-item label="创建账户名称">
                <span>{{ props.row.createAccountName }}</span>
              </el-form-item>
              <el-form-item label="请求时间">
                <span>{{ props.row.createTime }}</span>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column label="应用服务" prop="applicationName"></el-table-column>
        <el-table-column label="控制器" prop="controllerName"></el-table-column>
        <el-table-column label="请求方法名" prop="method"></el-table-column>
        <el-table-column label="请求方式" prop="requestMethod" width="80px" align="center"></el-table-column>
        <el-table-column label="请求Url" prop="requestUrl"></el-table-column>
        <el-table-column label="请求IP" prop="requestIp"  width="100px" align="center"></el-table-column>
        <el-table-column label="操作结果" prop="statusDesc" width="80px" align="center"></el-table-column>
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
import { handleLogPage } from '@/apis/info/log'
import { getSuperAdmin } from '@/utils/storage'

export default {
  name: 'LogPage',
  data () {
    return {
      // 是否是超级管理员
      superAdmin: 0,
      // 日期选择器
      operateTimeArray: [],
      // 查询表单参数
      queryForm: {
        // 应用名称
        applicationName: undefined,
        // 控制器名称
        controllerName: undefined,
        // 方法名
        method: undefined,
        // 请求方式
        requestMethod: undefined,
        // 请求url
        requestUrl: undefined,
        // 请求状态
        status: undefined,
        // 开始时间
        startOperateTime: '',
        // 结束时间
        endOperateTime: '',
        // 当前页
        current: 1,
        // 当前页大小
        pageSize: 20
      },
      // 业务类型列表
      businessTypeList: [],
      // 表单分页数据列表
      formDataList: [],
      // 表单数据总数
      total: 0
    }
  },
  created () {
    this.superAdmin = getSuperAdmin()
    this.getLogPage()
  },
  methods: {
    // 查询表单重置
    resetQueryForm () {
      this.queryForm.applicationName = undefined
      this.queryForm.controllerName = undefined
      this.queryForm.method = undefined
      this.queryForm.requestMethod = undefined
      this.queryForm.requestUrl = undefined
      this.queryForm.status = undefined
      this.queryForm.current = 1
      this.queryForm.pageSize = 20
      this.operateTimeArray = []
      this.getLogPage()
    },
    // 得到账户分页数据
    async getLogPage () {
      if (this.operateTimeArray) {
        this.queryForm.startTime = this.operateTimeArray[0]
        this.queryForm.endTime = this.operateTimeArray[1]
      }
      this.queryForm.superAdmin = this.superAdmin
      const { data: res } = await handleLogPage(this.queryForm)
      if (res.status !== '2000') return this.$message.error(res.message)
      this.formDataList = res.data.records
      this.total = res.data.total
    },
    // 处理当前页大小改变
    handleSizeChange (newSize) {
      this.queryForm.pageSize = newSize
      this.getLogPage()
    },
    // 处理当前页码改变
    handleCurrentChange (newCurrent) {
      this.queryForm.current = newCurrent
      this.getLogPage()
    }
  }
}
</script>

<style lang="less" scoped>
.status-select {
  width: 240px;
}
.button_container {
  padding-left: 20px;
}
</style>
