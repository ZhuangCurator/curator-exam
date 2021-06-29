<template>
  <div class="registerInfo_container">
    <!-- 卡片视图区域-->
    <el-card class="box-card">
      <el-table :data="registerInfoList" highlight-current-row style="width: 100%">
        <el-table-column type="index" width="50"></el-table-column>
        <el-table-column label="考生姓名" prop="accountName" align="center"></el-table-column>
        <el-table-column label="考试类别" prop="examCategoryName" align="center"></el-table-column>
        <el-table-column label="考试科目" prop="examSubjectName" align="center"></el-table-column>
        <el-table-column label="考试状态" prop="examStatusDesc" align="center"></el-table-column>
        <el-table-column label="准考证号" prop="admissionNumber" align="center"></el-table-column>
        <el-table-column label="考试成绩" prop="score" align="center"></el-table-column>
        <el-table-column label="操作" width="120px;" align="center">
          <template slot-scope="scope">
            <el-button type="success" icon="el-icon-setting" size="mini" @click="printAdmissionTicket(scope.row.examRegisterInfoId)">准考证打印</el-button>
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
  handlePageWithRegisterInfo,
  handlePreviewAdmissionTicket,
  handlePrintAdmissionTicket
} from '@/apis/register'
export default {
  name: 'ScorePage',
  data () {
    return {
      // 查询表单参数
      queryForm: {
        // 账户ID
        accountId: this.$store.state.accountId,
        // 当前页
        current: 1,
        // 当前页大小
        pageSize: 20
      },
      // 报名信息列表
      registerInfoList: [],
      // 报名信息总数
      total: 0
    }
  },
  created () {
    this.getRegisterInfoPage()
  },
  methods: {
    // 得到报名信息分页数据
    async getRegisterInfoPage () {
      const { data: res } = await handlePageWithRegisterInfo(this.queryForm)
      console.log(res.data)
      if (res.status !== '2000') return this.$message.error(res.message)
      this.registerInfoList = res.data.records
      this.total = res.data.total
    },
    // 处理当前页大小改变
    handleSizeChange (newSize) {
      this.queryForm.pageSize = newSize
      this.getRegisterInfoPage()
    },
    // 处理当前页码改变
    handleCurrentChange (newCurrent) {
      this.queryForm.current = newCurrent
      this.getRegisterInfoPage()
    },
    // 打印准考证
    async printAdmissionTicket (examRegisterInfoId) {
      // 首先进行预校验
      const param = {
        examRegisterInfoId: examRegisterInfoId
      }
      const { data: result } = await handlePreviewAdmissionTicket(param)
      if (result.status !== '2000') {
        return this.$message.error(result.message)
      }
      // 成功即进行打印操作
      const { data: res } = await handlePrintAdmissionTicket(param)
      const link = document.createElement('a')
      const blob = new Blob([res], {
        type: 'application/pdf'
      })
      link.style.display = 'none'
      link.href = URL.createObjectURL(blob)
      link.download = '馆长全国综合考试准考证'
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
    }
  }
}
</script>

<style >

</style>
