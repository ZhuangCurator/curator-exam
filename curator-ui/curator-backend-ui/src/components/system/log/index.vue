<template>
  <div class="log_container">
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>系统管理</el-breadcrumb-item>
      <el-breadcrumb-item>系统日志</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图区域-->
    <el-card class="box-card">
      <!--  搜索与添加区域 -->
      <el-form :model="queryForm" ref="queryFormRef" inline label-width="68px">
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
          <el-form-item label="业务类型">
            <el-select v-model="queryForm.businessType" placeholder="请选择业务类型">
              <el-option
                v-for="item in businessTypeList"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="操作结果">
            <el-select v-model="queryForm.status" placeholder="请选择操作结果" size="small">
              <el-option :key="0" label="成功" :value="0"></el-option>
              <el-option :key="1" label="失败" :value="1"></el-option>
            </el-select>
          </el-form-item>
        </el-row>
        <el-row>
          <el-form-item label="操作模块">
            <el-input
              v-model="queryForm.title"
              placeholder="请输入操作模块"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="getLogPage"
            />
          </el-form-item>
          <el-form-item label="操作人员">
            <el-input
              v-model="queryForm.operatorName"
              placeholder="请输入操作人员名称"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="getLogPage"
            />
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
              <el-form-item label="操作人员">
                <span>{{ props.row.operatorName }}</span>
              </el-form-item>
              <el-form-item label="请求URL">
                <span>{{ props.row.operateUrl }}</span>
              </el-form-item>
              <el-form-item label="请求方法">
                <span>{{ props.row.method }}</span>
              </el-form-item>
              <el-form-item label="请求参数">
                <span>{{ props.row.operateParam }}</span>
              </el-form-item>
              <el-form-item label="返回结果">
                <span>{{ props.row.jsonResult }}</span>
              </el-form-item>
              <el-form-item label="错误消息">
                <span>{{ props.row.errorMsg }}</span>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column label="应用服务" prop="application"></el-table-column>
        <el-table-column label="操作模块" prop="title"></el-table-column>
        <el-table-column label="业务类型" prop="businessTypeDesc"></el-table-column>
        <el-table-column label="请求方式" prop="requestMethod"></el-table-column>
        <el-table-column label="主机" prop="operateIp"></el-table-column>
        <el-table-column label="操作备注" prop="remark"></el-table-column>
        <el-table-column label="操作结果" prop="statusDesc"></el-table-column>
        <el-table-column label="操作日期" prop="insertTime"></el-table-column>
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
import { handleLogPage, handleBusinessType } from '@/apis/log'

export default {
  name: 'LogPage',
  data () {
    return {
      // 日期选择器
      operateTimeArray: [],
      // 查询表单参数
      queryForm: {
        // 操作账户名
        operatorName: '',
        // 日志状态
        status: '',
        // 业务类型
        businessType: '',
        // 操作模块
        title: '',
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
    this.getBusinessType()
    this.getLogPage()
  },
  methods: {
    // 查询表单重置
    resetQueryForm () {
      this.queryForm.operatorName = ''
      this.queryForm.status = ''
      this.queryForm.businessType = ''
      this.queryForm.title = ''
      this.queryForm.current = 1
      this.queryForm.pageSize = 20
      this.operateTimeArray = []
      this.getLogPage()
    },
    // 得到业务类型查询参数
    async getBusinessType () {
      const { data: res } = await handleBusinessType()
      const map = res.data
      for (const key in map) {
        const obj = {}
        obj.value = key
        obj.label = map[key]
        this.businessTypeList.push(obj)
      }
    },
    // 得到账户分页数据
    async getLogPage () {
      if (this.operateTimeArray) {
        this.queryForm.startOperateTime = this.operateTimeArray[0]
        this.queryForm.endOperateTime = this.operateTimeArray[1]
      }
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

</style>
