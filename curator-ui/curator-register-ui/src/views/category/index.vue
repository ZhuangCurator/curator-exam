<template>
  <div class="examNotice_container">
    <!-- 卡片视图区域-->
    <el-card class="box-card">
      <!--  搜索与添加区域 -->
      <el-form :model="queryForm" ref="queryFormRef" :inline="true">
        <el-form-item label="类别名称" label-width="80px;">
          <el-input
            v-model="queryForm.examCategoryName"
            placeholder="请输入考试类别名称"
            clearable
            size="small"
            style="width: 240px"
            @keyup.enter.native="getExamCategoryPage"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="small" @click="getExamCategoryPage">搜索</el-button>
        </el-form-item>
      </el-form>
      <el-table :data="examCategoryList" highlight-current-row style="width: 100%">
        <el-table-column type="index" width="50"></el-table-column>
        <el-table-column label="考试类别" prop="examCategoryName" align="center"></el-table-column>
        <el-table-column label="考试开始时间" width="300" prop="examStartTime" align="center"></el-table-column>
        <el-table-column label="考试结束时间" width="300" prop="examEndTime" align="center"></el-table-column>
        <el-table-column label="操作" width="200px;" align="center">
          <template slot-scope="scope">
            <el-button type="success" icon="el-icon-setting" size="mini" @click="showExamSitePage(scope.row)">科目列表</el-button>
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
import { handleExamCategoryPage } from '@/apis/examCategory/examCategory'
export default {
  name: 'CategoryPage',
  data () {
    return {
      // 查询表单参数
      queryForm: {
        // 省
        province: undefined,
        // 市
        city: undefined,
        // 考试类别
        examCategoryName: undefined,
        // 当前页
        current: 1,
        // 当前页大小
        pageSize: 20
      },
      // 考试类别列表
      examCategoryList: [],
      // 考试类别总数
      total: 0
    }
  },
  methods: {
    // 得到考试类别分页数据
    async getExamCategoryPage () {
      this.queryForm.province = this.$store.state.province
      this.queryForm.city = this.$store.state.city
      const { data: res } = await handleExamCategoryPage(this.queryForm)
      if (res.status !== '2000') return this.$message.error(res.message)
      this.examCategoryList = res.data.records
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
    // 跳转科目界面
    showExamSitePage (examCategory) {
      this.$router.push({
        path: 'subject',
        query: {
          c: examCategory.examCategoryId
        }
      })
    }
  },
  mounted () {
    this.getExamCategoryPage()
  }
}
</script>

<style lang="less">
.empty-div {
  text-align: center;
  > p {
    font-size: 20px;
    font-weight: bold;
    color: darkslateblue;
  }
}
</style>
