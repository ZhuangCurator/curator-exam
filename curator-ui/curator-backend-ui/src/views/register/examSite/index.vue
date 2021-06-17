<template>
  <div class="examSite_container">
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>考点管理</el-breadcrumb-item>
      <el-breadcrumb-item>考点列表</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图区域-->
    <el-card class="box-card">
      <!--  搜索与添加区域 -->
      <el-form :model="queryForm" ref="queryFormRef" :inline="true" label-width="100px">
        <el-form-item label="考点名称">
          <el-input
            v-model="queryForm.examSiteName"
            placeholder="请输入考点名称"
            clearable
            size="small"
            style="width: 240px"
            @keyup.enter.native="getExamSitePage"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="info" icon="el-icon-search" size="small" @click="getExamSitePage">搜索</el-button>
          <el-button type="warning" icon="el-icon-refresh" size="small" @click="resetQueryForm">重置</el-button>
        </el-form-item>
      </el-form>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-button type="primary" size="mini" v-has-perm="['register:subjectSite:add']" @click="showAddDialog">添加考点</el-button>
        </el-col>
      </el-row>

      <!--  考点列表区域 -->
      <el-table :data="examSiteList" border stripe>
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
        <el-table-column label="考点名" prop="examSiteName" align="center"></el-table-column>
        <el-table-column label="人数限制" prop="numberLimit" align="center"></el-table-column>
        <el-table-column label="省" prop="provinceName" align="center"></el-table-column>
        <el-table-column label="市" prop="cityName" align="center"></el-table-column>
        <el-table-column label="区" prop="districtName" align="center"></el-table-column>
        <el-table-column label="地址" prop="address" align="center"></el-table-column>
        <el-table-column label="操作" width="300px;" align="center" v-if="columnShow">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" size="mini" v-has-perm="['register:subjectSite:update']" @click="showEditDialog(scope.row.examSiteId)">编辑</el-button>
            <el-button type="danger" icon="el-icon-delete" size="mini" v-has-perm="['register:subjectSite:deleted']" @click="deleteExamSite(scope.row.examSiteId)">删除</el-button>
            <el-button type="info" icon="el-icon-setting" size="mini" @click="showClassroomPage(scope.row.examSiteId)">教室列表</el-button>
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
    <el-dialog title="添加考点" :visible.sync="addDialogVisible" width="41%" @close="handleAddDialogClose">
      <!-- 对话框主题区域 -->
      <el-form ref="addFormRef" :model="addForm" :rules="dialogFormRules" label-width="120px">
        <el-form-item label="考点名" prop="examSiteName">
          <el-input v-model="addForm.examSiteName"></el-input>
        </el-form-item>
        <el-form-item label="考点人数限制" prop="numberLimit">
          <el-input v-model="addForm.numberLimit"></el-input>
        </el-form-item>
        <el-form-item label="地市" prop="district">
          <v-distpicker @selected="choseAreaOnAdd"></v-distpicker>
        </el-form-item>
        <el-form-item label="详细地址" prop="address">
          <el-input v-model="addForm.address"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部按钮区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addExamSite">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 修改考点对话框 -->
    <el-dialog title="编辑考点" :visible.sync="editDialogVisible" width="41%" @close="handleEditDialogClose">
      <!-- 对话框主题区域 -->
      <el-form ref="editFormRef" :model="editForm" :rules="dialogFormRules" label-width="120px">
        <el-form-item label="考点名" prop="examSiteName">
          <el-input v-model="editForm.examSiteName"></el-input>
        </el-form-item>
        <el-form-item label="考点人数限制" prop="numberLimit">
          <el-input v-model="editForm.numberLimit"></el-input>
        </el-form-item>
        <el-form-item label="地市" prop="district">
          <v-distpicker :province="editForm.provinceName"  :city="editForm.cityName" :area="editForm.districtName"
                        @selected="choseAreaOnEdit">
          </v-distpicker>
        </el-form-item>
        <el-form-item label="详细地址" prop="address">
          <el-input v-model="editForm.address"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部按钮区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="editExamSite">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  handleAddExamSite,
  handleDeleteExamSite,
  handleUpdateExamSite,
  handleExamSitePage,
  handleExamSiteQuery
} from '@/apis/register/examSite'
import { showElement } from '@/utils/show'

export default {
  name: 'ExamSitePage',
  data () {
    return {
      columnShow: true,
      // 是否是超级管理员
      superAdmin: 0,
      // 启用的权限列表
      powerOptions: [],
      defaultProps: {
        children: 'children',
        label: 'label',
        isLeaf: 'leaf'
      },
      // 查询表单参数
      queryForm: {
        // 考点名
        examSiteName: undefined,
        // 当前页
        current: 1,
        // 当前页大小
        pageSize: 20
      },
      // 考点列表
      examSiteList: [],
      // 考点总数
      total: 0,
      // 控制添加考点对话框的显示
      addDialogVisible: false,
      // 添加考点表单数据
      addForm: {
        examSiteName: undefined,
        numberLimit: undefined,
        province: undefined,
        provinceName: undefined,
        city: undefined,
        cityName: undefined,
        district: undefined,
        districtName: undefined,
        address: undefined
      },
      // 控制修改考点对话框的显示
      editDialogVisible: false,
      // 修改考点表单数据
      editForm: {
        examSiteId: undefined,
        examSiteName: undefined,
        numberLimit: undefined,
        province: undefined,
        provinceName: undefined,
        city: undefined,
        cityName: undefined,
        district: undefined,
        districtName: undefined,
        address: undefined
      },
      // 考点表单校验规则
      dialogFormRules: {
        examSiteName: [
          {
            required: true,
            message: '请输入考点名',
            trigger: 'blur'
          },
          {
            min: 3,
            max: 20,
            message: '考点名长度在3~20字符之间',
            trigger: 'blur'
          }
        ],
        numberLimit: [
          {
            pattern: /^[0-9]+$/,
            message: '请输入正整数',
            trigger: 'blur'
          },
          {
            required: true,
            message: '请输入考点人数限制',
            trigger: 'blur'
          }
        ],
        district: [
          {
            required: true,
            message: '请选择地市区',
            trigger: 'blur'
          }
        ],
        address: [
          {
            required: true,
            message: '请输入考点地址',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  created () {
    this.superAdmin = this.$store.state.superAdmin
    this.getExamSitePage()
  },
  updated () {
    this.showTableColumn()
  },
  methods: {
    // 根据权限数据展示操作列
    showTableColumn () {
      this.columnShow = showElement(['system:subjectSite:update', 'system:subjectSite:deleted', 'system:subjectSite:bind'])
    },
    // 查询表单重置
    resetQueryForm () {
      this.queryForm.examSiteName = undefined
      this.queryForm.current = 1
      this.queryForm.pageSize = 20
      this.getExamSitePage()
    },
    // 得到考点分页数据
    async getExamSitePage () {
      this.queryForm.superAdmin = this.superAdmin
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
    // 监听 添加考点对话框关闭事件
    handleAddDialogClose () {
      // 清空字段
      this.$refs.addFormRef.resetFields()
      this.addForm.province = undefined
      this.addForm.city = undefined
      this.addForm.district = undefined
    },
    // 展示添加考点对话框
    showAddDialog () {
      this.addDialogVisible = true
    },
    // 添加考点
    addExamSite () {
      this.$refs.addFormRef.validate(async valid => {
        if (valid) {
          const { data: res } = await handleAddExamSite(this.addForm)
          if (res.status !== '2000') return this.$message.error(res.message)
          this.$message.success(res.message)
          this.addDialogVisible = false
          await this.getExamSitePage()
        }
      })
    },
    // 展示编辑考点对话框
    async showEditDialog (examSiteId) {
      const { data: res } = await handleExamSiteQuery(examSiteId)
      console.log(res)
      if (res.status !== '2000') {
        return this.$message.error(res.message)
      }
      // 查询出考点 在展示编辑框
      this.editDialogVisible = true
      this.editForm = res.data
    },
    // 监听 编辑考点对话框关闭事件
    handleEditDialogClose () {
      // 清空字段
      this.$refs.editFormRef.resetFields()
      this.editForm.province = undefined
      this.editForm.city = undefined
      this.editForm.district = undefined
    },
    // 确定编辑考点
    editExamSite () {
      this.$refs.editFormRef.validate(async valid => {
        if (valid) {
          const { data: res } = await handleUpdateExamSite(this.editForm)
          if (res.status !== '2000') return this.$message.error(res.message)
          this.$message.success(res.message)
          this.editDialogVisible = false
          await this.getExamSitePage()
        }
      })
    },
    // 删除考点
    deleteExamSite (examSiteId) {
      this.$confirm('此操作将删除该考点, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        const { data: res } = await handleDeleteExamSite(examSiteId)
        if (res.status !== '2000') {
          return this.$message.error(res.message)
        }
        this.$message({
          type: 'success',
          message: res.message
        })
        await this.getExamSitePage()
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
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
    // 跳转到教室列表
    showClassroomPage (examSiteId) {
      this.$router.push({
        path: 'classroomPage',
        query: {
          id: examSiteId
        }
      })
    }
  }
}
</script>

<style lang="less" scoped>
.date-dialog {
  width: 295px;
}
</style>
