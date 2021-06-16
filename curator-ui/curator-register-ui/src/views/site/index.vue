<template>
  <div class="examSite_container">
    <!-- 卡片视图区域-->
    <el-card class="box-card">
      <!--  搜索与添加区域 -->
      <el-form :model="queryForm" ref="queryFormRef" :inline="true">
        <el-form-item label="考点名称" label-width="80px;">
          <el-input
            v-model="queryForm.examSiteName"
            placeholder="请输入考试考点名称"
            clearable
            size="small"
            style="width: 240px"
            @keyup.enter.native="getExamSitePage"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="small" @click="getExamSitePage">搜索</el-button>
        </el-form-item>
      </el-form>
      <el-table :data="examSiteList" highlight-current-row  style="width: 100%">
        <el-table-column type="index" width="50"></el-table-column>
        <el-table-column label="考点名称" prop="examSiteName" align="left"></el-table-column>
        <el-table-column label="人数限制" prop="numberLimit" align="center"></el-table-column>
        <el-table-column label="已报人数" prop="registerNumber" align="center"></el-table-column>
        <el-table-column label="省份" prop="provinceName" align="center"></el-table-column>
        <el-table-column label="地市" prop="cityName" align="center"></el-table-column>
        <el-table-column label="区县" prop="districtName" align="center"></el-table-column>
        <el-table-column label="地址" prop="address" align="center"></el-table-column>
        <el-table-column label="操作" width="120px;" align="center">
          <template slot-scope="scope">
            <el-button type="success" icon="el-icon-setting" size="mini" @click="handleAccountRegister(scope.row.examSiteId)">报名</el-button>
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
  handleExamSitePage
} from '@/apis/examSite/examSite'
import { handleAccountRegister } from '@/apis/register'
export default {
  name: 'ExamSitePage',
  data () {
    return {
      examCategoryId: undefined,
      examSubjectId: undefined,
      // 查询表单参数
      queryForm: {
        // 考试科目id
        examSubjectId: undefined,
        // 考点名
        examSiteName: undefined,
        // 当前页
        current: 1,
        // 当前页大小
        pageSize: 20
      },
      // 考试考点列表
      examSiteList: [],
      // 考试考点总数
      total: 0
    }
  },
  methods: {
    // 得到考试考点分页数据
    async getExamSitePage () {
      const { data: res } = await handleExamSitePage(this.queryForm)
      console.log(res.data)
      if (res.status !== '2000') return this.$message.error(res.message)
      this.examSiteList = res.data.records
      this.total = res.data.total
    },
    // 处理当前页大小改变
    handleSizeChange (newSize) {
      this.queryForm.pageSize = newSize
      this.getExamSitePage()
    },
    // 处理当前页码改变
    handleCurrentChange (newCurrent) {
      this.queryForm.current = newCurrent
      this.getExamSitePage()
    },
    // 用户报名
    handleAccountRegister (examSiteId) {
      if (this.$store.state.accountId === '') {
        // 没有登录则跳转到登录页
        this.$router.push({
          path: 'login',
          query: { redirect: this.$router.currentRoute.fullPath }
        })
      } else {
        this.$confirm('是否确认报名该考点?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          const param = {
            examCategoryId: this.examCategoryId,
            examSubjectId: this.examSubjectId,
            examSiteId: examSiteId,
            accountId: this.$store.state.accountId,
            accountName: this.$store.state.accountName
          }
          const { data: res } = await handleAccountRegister(param)
          if (res.status !== '2000') {
            return this.$message.error(res.message)
          }
          this.$message({
            type: 'success',
            message: res.message
          })
          // await this.getExamSubjectPage()
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消报名'
          })
        })
      }
    }
  },
  mounted () {
    this.examSubjectId = this.$route.query.s
    this.examCategoryId = this.$route.query.c
    this.queryForm.examSubjectId = this.$route.query.s
    this.getExamSitePage()
  }
}
</script>

<style >

</style>
