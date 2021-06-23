<template>
  <div class="ExamSubjectSite_container">
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>考试科目管理</el-breadcrumb-item>
      <el-breadcrumb-item>考点列表</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图区域-->
    <el-card class="box-card">
      <!--  搜索与添加区域 -->
      <el-form :model="queryForm" ref="queryFormRef" :inline="true" label-width="70px">
        <el-form-item label="考点名称">
          <el-input
            v-model="queryForm.examSiteName"
            placeholder="请输入考点名称"
            clearable
            size="small"
            style="width: 240px"
            @keyup.enter.native="getExamSubjectSitePage"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="info" icon="el-icon-search" size="small" @click="getExamSubjectSitePage">搜索</el-button>
          <el-button type="warning" icon="el-icon-refresh" size="small" @click="resetQueryForm">重置</el-button>
        </el-form-item>
      </el-form>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-button type="primary" size="mini" v-has-perm="['register:subjectSite:add']" @click="showAddDialog">添加考点</el-button>
        </el-col>
      </el-row>

      <!--  考点列表区域 -->
      <el-table :data="examSubjectSiteList" border stripe>
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-form label-position="left" class="table-expand">
              <el-form-item label="创建账户">
                <span>{{ props.row.createAccountName }}</span>
              </el-form-item>
              <el-form-item label="创建时间">
                <span>{{ props.row.createTime }}</span>
              </el-form-item>
              <el-form-item label="更新时间">
                <span>{{ props.row.updateTime }}</span>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column label="考点名" prop="examSiteName" align="center"></el-table-column>
        <el-table-column label="人数限制" prop="numberLimit" align="center"></el-table-column>
        <el-table-column label="已报人数" prop="registerNumber" align="center"></el-table-column>
        <el-table-column label="省" prop="provinceName" align="center"></el-table-column>
        <el-table-column label="市" prop="cityName" align="center"></el-table-column>
        <el-table-column label="区" prop="districtName" align="center"></el-table-column>
        <el-table-column label="地址" prop="address" align="center"></el-table-column>
        <el-table-column label="操作" width="300px;" align="center" v-if="columnShow">
          <template slot-scope="scope">
            <el-button type="danger" icon="el-icon-delete" size="mini" v-has-perm="['register:subjectSite:delete']" @click="deleteExamSubjectSite(scope.row.examSubjectSiteId)">删除</el-button>
            <el-button type="info" icon="el-icon-setting" size="mini" @click="showRegisterInfoPage(scope.row)">考生列表</el-button>
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

    <!-- 添加考点对话框 -->
    <el-dialog title="添加考点" :visible.sync="addDialogVisible" width="30%" @close="handleAddDialogClose">
      <!-- 对话框主题区域 -->
      <el-form ref="addFormRef" :model="addForm" :rules="dialogFormRules" label-width="120px">
        <el-form-item label="考点" prop="generationRuleId">
          <el-select v-model="addForm.examSiteIdList" multiple placeholder="请选择考点">
            <el-option
              v-for="item in examSiteList"
              :key="item.examSiteId"
              :label="item.examSiteName"
              :value="item.examSiteId"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <!-- 底部按钮区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addExamSubjectSite">确 定</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
import {
  handleExamSitePageWithSubject,
  handleAddExamSiteToSubject, handleGenerationRuleList
} from '@/apis/register/examSubject'
import { handleExamSiteList } from '@/apis/register/examSite'
import { showElement } from '@/utils/show'

export default {
  name: 'ExamSubjectSitePage',
  data () {
    return {
      columnShow: true,
      // 是否是超级管理员
      superAdmin: 0,
      // 考试科目id
      examSubjectId: undefined,
      // 查询表单参数
      queryForm: {
        // 考点名
        examSiteName: undefined,
        // 省
        province: undefined,
        // 市
        city: undefined,
        // 区
        district: undefined,
        // 当前页
        current: 1,
        // 当前页大小
        pageSize: 20
      },
      // 考点列表
      examSiteList: [],
      // 科目下考点列表
      examSubjectSiteList: [],
      // 考点总数
      total: 0,
      // 控制添加考点对话框的显示
      addDialogVisible: false,
      // 添加考点表单数据
      addForm: {
        examSiteIdList: []
      },
      // 考点表单校验规则
      dialogFormRules: {
        examSiteName: [
          {
            required: true,
            message: '请输入考点名',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  created () {
    this.superAdmin = this.$store.state.superAdmin
    this.showExamSiteList()
  },
  updated () {
    this.showTableColumn()
  },
  methods: {
    // 根据权限数据展示操作列
    showTableColumn () {
      this.columnShow = showElement(['register:subjectSite:delete'])
    },
    // 查询表单重置
    resetQueryForm () {
      this.queryForm.examSiteName = undefined
      this.queryForm.province = undefined
      this.queryForm.city = undefined
      this.queryForm.district = undefined
      this.queryForm.current = 1
      this.queryForm.pageSize = 20
      this.getExamSubjectSitePage()
    },
    // 得到考点分页数据
    async getExamSubjectSitePage () {
      this.queryForm.examSubjectId = this.examSubjectId
      this.queryForm.superAdmin = this.superAdmin
      const { data: res } = await handleExamSitePageWithSubject(this.queryForm)
      console.log(res.data)
      if (res.status !== '2000') return this.$message.error(res.message)
      this.examSubjectSiteList = res.data.records
      this.total = res.data.total
    },
    // 处理当前页大小改变
    handleSizeChange (newSize) {
      this.queryForm.pageSize = newSize
      this.getExamSubjectSitePage()
    },
    // 处理当前页码改变
    handleCurrentChange (newCurrent) {
      this.queryForm.current = newCurrent
      this.getExamSubjectSitePage()
    },
    // 监听 添加考点对话框关闭事件
    handleAddDialogClose () {
      // 清空字段
      this.$refs.addFormRef.resetFields()
      this.addForm.examSiteIdList = []
    },
    // 展示添加考点对话框
    showAddDialog () {
      this.addDialogVisible = true
    },
    // 添加考点
    addExamSubjectSite () {
      this.$refs.addFormRef.validate(async valid => {
        if (valid) {
          this.addForm.examSubjectId = this.examSubjectId
          const { data: res } = await handleAddExamSiteToSubject(this.addForm)
          if (res.status !== '2000') return this.$message.error(res.message)
          this.$message.success(res.message)
          this.addDialogVisible = false
          await this.getExamSubjectSitePage()
        }
      })
    },
    // 删除考点
    // deleteExamSubjectSite (examSubjectSiteId) {
    //   this.$confirm('此操作将删除该考点, 是否继续?', '提示', {
    //     confirmButtonText: '确定',
    //     cancelButtonText: '取消',
    //     type: 'warning'
    //   }).then(async () => {
    //     const { data: res } = await handleDeleteExamSite(examSubjectSiteId)
    //     if (res.status !== '2000') {
    //       return this.$message.error(res.message)
    //     }
    //     this.$message({
    //       type: 'success',
    //       message: res.message
    //     })
    //     await this.getExamSubjectSitePage()
    //   }).catch(() => {
    //     this.$message({
    //       type: 'info',
    //       message: '已取消删除'
    //     })
    //   })
    // },
    // 获取考点列表
    async showExamSiteList () {
      const query = { superAdmin: this.superAdmin }
      const { data: result } = await handleExamSiteList(query)
      if (result.status !== '2000') {
        return this.$message.error(result.message)
      }
      this.examSiteList = result.data
    },
    // 添加考点时地市插件触发
    choseAreaOnAdd (data) {
      console.log(data)
      this.addForm.province = data.province.code
      this.addForm.provinceName = data.province.value
      this.addForm.city = data.city.code
      this.addForm.cityName = data.city.value
      this.addForm.district = data.area.code
      this.addForm.districtName = data.area.value
    },
    // 编辑考点时地市插件触发
    choseAreaOnEdit (data) {
      this.editForm.province = data.province.code
      this.editForm.provinceName = data.province.value
      this.editForm.city = data.city.code
      this.editForm.cityName = data.city.value
      this.editForm.district = data.area.code
      this.editForm.districtName = data.area.value
    },
    // 跳转到当前考点下的考生信息界面
    showRegisterInfoPage (examSubjectSite) {
      this.$router.push({
        path: 'subjectRegisterInfoPage',
        query: {
          examSubjectId: examSubjectSite.examSubjectId,
          examSiteId: examSubjectSite.examSiteId
        }
      })
    }
  },
  mounted () {
    this.examSubjectId = this.$route.query.id
    this.getExamSubjectSitePage()
  }
}
</script>

<style lang="less" scoped>
.date-dialog {
  width: 295px;
}
</style>
