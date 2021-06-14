<template>
  <div class="examNotice_container">
    <!-- 卡片视图区域-->
    <el-card class="box-card">
      <!--  搜索与添加区域 -->
      <el-form :model="queryForm" ref="queryFormRef" :inline="true">
        <el-form-item label="地市">
          <v-distpicker hide-area @selected="choseCityOnQuery"></v-distpicker>
        </el-form-item>
        <el-form-item label="考试科目">
          <el-select v-model="queryForm.examCategoryId" placeholder="请选择考试科目">
            <el-option
              v-for="item in examCategoryList"
              :key="item.examCategoryId"
              :label="item.examCategoryName"
              :value="item.examCategoryId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="small" @click="getExamSubjectList">搜索</el-button>
        </el-form-item>
      </el-form>
      <div class="empty-div" v-if="showTable === false" style="height: 350px">
        <img src="../../assets/no-data.png">
        <p>请选择地市和考试类别查询考试科目</p>
      </div>
      <el-table v-else :data="examSubjectList" highlight-current-row style="width: 100%">
        <el-table-column type="index" width="50"></el-table-column>
        <el-table-column label="考试科目" prop="examSubjectName" align="left"></el-table-column>
        <el-table-column label="报名开始时间" width="150" prop="registerStartTime" align="center"></el-table-column>
        <el-table-column label="报名结束时间" width="150" prop="registerEndTime" align="center"></el-table-column>
        <el-table-column label="考试开始时间" width="150" prop="examStartTime" align="center"></el-table-column>
        <el-table-column label="考试结束时间" width="150" prop="examEndTime" align="center"></el-table-column>
        <el-table-column label="操作" width="120px;" align="center">
          <template slot-scope="scope">
            <el-button type="success" icon="el-icon-setting" size="mini" @click="showExamSitePage(scope.row.examSubjectId)">考点列表</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
</div>
</template>

<script>
import { handleExamCategoryList } from '@/apis/examCategory/examCategory.js'
import { handleExamSubjectList } from '@/apis/examSubject/examSubject'
export default {
  name: 'RegisterPage',
  data () {
    return {
      // 查询表单参数
      queryForm: {
        // 省
        province: undefined,
        // 市
        city: undefined,
        // 考试类别
        examCategoryId: undefined,
        // 当前页
        current: 1,
        // 当前页大小
        pageSize: 20
      },
      showTable: false,
      // 考试科目列表
      examCategoryList: [],
      // 考试科目列表
      examSubjectList: []
    }
  },
  methods: {
    // 得到考试类别列表数据
    async getExamCategoryList () {
      const { data: res } = await handleExamCategoryList(this.queryForm)
      if (res.status !== '2000') return this.$message.error(res.message)
      this.examCategoryList = res.data
    },
    // 得到考试科目分页数据
    async getExamSubjectList () {
      this.showTable = true
      const { data: res } = await handleExamSubjectList(this.queryForm)
      if (res.status !== '2000') return this.$message.error(res.message)
      this.examSubjectList = res.data
    },
    // 处理当前页大小改变
    handleSizeChange (newSize) {
      this.queryForm.pageSize = newSize
      this.getExamCategoryList()
    },
    // 处理当前页码改变
    handleCurrentChange (newCurrent) {
      this.queryForm.current = newCurrent
      this.getExamCategoryList()
    },
    // 选择地市触发
    choseCityOnQuery (data) {
      this.queryForm.province = data.province.code
      this.queryForm.city = data.city.code
      this.getExamCategoryList()
    },
    // 跳转考点界面
    showExamSitePage (examSubjectId) {
      this.$router.push({
        path: 'site',
        query: {
          id: examSubjectId
        }
      })
    }
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
