<template>
  <div class="examNotice_container">
    <!-- 卡片视图区域-->
    <el-card class="box-card">
      <!--  搜索与添加区域 -->
      <el-form :model="queryForm" ref="queryFormRef" :inline="true">
        <el-form-item label="公告名称" label-width="80px;">
          <el-input
            v-model="queryForm.examNoticeName"
            placeholder="请输入考试公告名称"
            clearable
            size="small"
            style="width: 240px"
            @keyup.enter.native="getExamNoticePage"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="small" @click="getExamNoticePage">搜索</el-button>
        </el-form-item>
      </el-form>
      <el-table :data="examNoticeList" highlight-current-row :show-header="false" style="width: 100%">
        <el-table-column type="index" width="50"></el-table-column>
        <el-table-column label="公告名称" align="left">
          <template slot-scope="scope">
            <router-link :to="{ path: '/noticeContent', query: { id: scope.row.examNoticeId }}">{{ scope.row.examNoticeName }}</router-link>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" width="150" prop="createTime" align="center"></el-table-column>
      </el-table>
      <!-- 分页区域 -->
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryForm.current"
        :page-sizes="[1, 5, 10, 50]"
        :page-size="queryForm.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </el-card>
</div>
</template>

<script>
import {
  handleExamNoticePage
} from '@/apis/notice/examNotice'
export default {
  name: 'ExamNoticePage',
  data () {
    return {
      // 查询表单参数
      queryForm: {
        // 考试公告名
        examNoticeName: undefined,
        // 当前页
        current: 1,
        // 当前页大小
        pageSize: 10
      },
      // 考试公告列表
      examNoticeList: [],
      // 考试公告总数
      total: 0
    }
  },
  created () {
    this.getExamNoticePage()
  },
  methods: {
    // 得到考试公告分页数据
    async getExamNoticePage () {
      const { data: res } = await handleExamNoticePage(this.queryForm)
      console.log(res.data)
      if (res.status !== '2000') return this.$message.error(res.message)
      this.examNoticeList = res.data.records
      this.total = res.data.total
    },
    // 处理当前页大小改变
    handleSizeChange (newSize) {
      this.queryForm.pageSize = newSize
      this.getExamNoticePage()
    },
    // 处理当前页码改变
    handleCurrentChange (newCurrent) {
      this.queryForm.current = newCurrent
      this.getExamNoticePage()
    }
  }
}
</script>

<style >
.el-form-item__label {
  text-align: left;
}
</style>
