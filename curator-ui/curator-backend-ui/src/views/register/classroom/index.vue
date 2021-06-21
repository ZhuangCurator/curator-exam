<template>
  <div class="examClassroom_container">
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>教室管理</el-breadcrumb-item>
      <el-breadcrumb-item>教室列表</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图区域-->
    <el-card class="box-card">
      <!--  搜索与添加区域 -->
      <el-form :model="queryForm" ref="queryFormRef" :inline="true" label-width="100px">
        <el-form-item label="教室名称">
          <el-input
            v-model="queryForm.examClassroomName"
            placeholder="请输入教室名称"
            clearable
            size="small"
            style="width: 240px"
            @keyup.enter.native="getExamClassroomPage"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="info" icon="el-icon-search" size="small" @click="getExamClassroomPage">搜索</el-button>
          <el-button type="warning" icon="el-icon-refresh" size="small" @click="resetQueryForm">重置</el-button>
        </el-form-item>
      </el-form>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-button type="primary" size="mini" v-has-perm="['register:examClassroom:add']" @click="showAddDialog">添加教室</el-button>
        </el-col>
      </el-row>

      <!--  教室列表区域 -->
      <el-table :data="examClassroomList" border stripe>
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
        <el-table-column label="教室名" prop="examClassroomName" align="center"></el-table-column>
        <el-table-column label="人数限制" prop="numberLimit" align="center"></el-table-column>
        <el-table-column label="详细地址" prop="address" align="center"></el-table-column>
        <el-table-column label="操作" width="300px;" align="center" v-if="columnShow">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" size="mini" v-has-perm="['register:examClassroom:update']" @click="showEditDialog(scope.row.examClassroomId)">编辑</el-button>
            <el-button type="danger" icon="el-icon-delete" size="mini" v-has-perm="['register:examClassroom:delete']" @click="deleteExamClassroom(scope.row.examClassroomId)">删除</el-button>
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

    <!-- 添加教室对话框 -->
    <el-dialog title="添加教室" :visible.sync="addDialogVisible" width="30%" @close="handleAddDialogClose">
      <!-- 对话框主题区域 -->
      <el-form ref="addFormRef" :model="addForm" :rules="dialogFormRules" label-width="120px">
        <el-form-item label="教室名" prop="examClassroomName">
          <el-input v-model="addForm.examClassroomName"></el-input>
        </el-form-item>
        <el-form-item label="人数限制" prop="numberLimit">
          <el-input v-model="addForm.numberLimit"></el-input>
        </el-form-item>
        <el-form-item label="详细地址" prop="address">
          <el-input v-model="addForm.address"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部按钮区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addExamClassroom">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 修改教室对话框 -->
    <el-dialog title="编辑教室" :visible.sync="editDialogVisible" width="30%" @close="handleEditDialogClose">
      <!-- 对话框主题区域 -->
      <el-form ref="editFormRef" :model="editForm" :rules="dialogFormRules" label-width="120px">
        <el-form-item label="教室名" prop="examClassroomName">
          <el-input v-model="editForm.examClassroomName"></el-input>
        </el-form-item>
        <el-form-item label="人数限制" prop="numberLimit">
          <el-input v-model="editForm.numberLimit"></el-input>
        </el-form-item>
        <el-form-item label="详细地址" prop="address">
          <el-input v-model="editForm.address"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部按钮区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="editExamClassroom">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  handleAddExamClassroom,
  handleDeleteExamClassroom,
  handleUpdateExamClassroom,
  handleExamClassroomPage,
  handleExamClassroomQuery
} from '@/apis/register/classroom'
import { showElement } from '@/utils/show'

export default {
  name: 'ExamClassroomPage',
  data () {
    return {
      columnShow: true,
      // 是否是超级管理员
      superAdmin: 0,
      // 考点id
      examSiteId: undefined,
      // 启用的权限列表
      powerOptions: [],
      defaultProps: {
        children: 'children',
        label: 'label',
        isLeaf: 'leaf'
      },
      // 查询表单参数
      queryForm: {
        // 教室名
        examClassroomName: undefined,
        // 当前页
        current: 1,
        // 当前页大小
        pageSize: 20
      },
      // 教室列表
      examClassroomList: [],
      // 教室总数
      total: 0,
      // 控制添加教室对话框的显示
      addDialogVisible: false,
      // 添加教室表单数据
      addForm: {
        examClassroomName: undefined,
        numberLimit: undefined,
        address: undefined
      },
      // 控制修改教室对话框的显示
      editDialogVisible: false,
      // 修改教室表单数据
      editForm: {
        examClassroomId: undefined,
        examClassroomName: undefined,
        numberLimit: undefined,
        address: undefined
      },
      // 教室表单校验规则
      dialogFormRules: {
        examClassroomName: [
          {
            required: true,
            message: '请输入教室名',
            trigger: 'blur'
          },
          {
            min: 3,
            max: 20,
            message: '教室名长度在3~20字符之间',
            trigger: 'blur'
          }
        ],
        address: [
          {
            required: true,
            message: '请输入教室地址',
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
            message: '请输入教室人数限制',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  created () {
    this.superAdmin = this.$store.state.superAdmin
    // this.getExamClassroomPage()
  },
  updated () {
    this.showTableColumn()
  },
  methods: {
    // 根据权限数据展示操作列
    showTableColumn () {
      this.columnShow = showElement(['register:examClassroom:update', 'register:examClassroom:delete'])
    },
    // 查询表单重置
    resetQueryForm () {
      this.queryForm.examClassroomName = undefined
      this.queryForm.examStartTime = undefined
      this.queryForm.examEndTime = undefined
      this.queryForm.current = 1
      this.queryForm.pageSize = 20
      this.getExamClassroomPage()
    },
    // 得到教室分页数据
    async getExamClassroomPage () {
      this.queryForm.superAdmin = this.superAdmin
      this.queryForm.examSiteId = this.examSiteId
      const { data: res } = await handleExamClassroomPage(this.queryForm)
      console.log(res.data)
      if (res.status !== '2000') return this.$message.error(res.message)
      this.examClassroomList = res.data.records
      this.total = res.data.total
    },
    // 处理当前页大小改变
    handleSizeChange (newSize) {
      this.queryForm.pageSize = newSize
      this.getExamClassroomPage()
    },
    // 处理当前页码改变
    handleCurrentChange (newCurrent) {
      this.queryForm.current = newCurrent
      this.getExamClassroomPage()
    },
    // 监听 添加教室对话框关闭事件
    handleAddDialogClose () {
      // 清空字段
      this.$refs.addFormRef.resetFields()
    },
    // 展示添加教室对话框
    showAddDialog () {
      this.addDialogVisible = true
    },
    // 添加教室
    addExamClassroom () {
      this.$refs.addFormRef.validate(async valid => {
        if (valid) {
          this.addForm.examSiteId = this.examSiteId
          const { data: res } = await handleAddExamClassroom(this.addForm)
          if (res.status !== '2000') return this.$message.error(res.message)
          this.$message.success(res.message)
          this.addDialogVisible = false
          await this.getExamClassroomPage()
        }
      })
    },
    // 展示编辑教室对话框
    async showEditDialog (examClassroomId) {
      const { data: res } = await handleExamClassroomQuery(examClassroomId)
      console.log(res)
      if (res.status !== '2000') {
        return this.$message.error(res.message)
      }
      // 查询出教室 在展示编辑框
      this.editDialogVisible = true
      this.editForm = res.data
    },
    // 监听 编辑教室对话框关闭事件
    handleEditDialogClose () {
      // 清空字段
      this.$refs.editFormRef.resetFields()
    },
    // 确定编辑教室
    editExamClassroom () {
      this.$refs.editFormRef.validate(async valid => {
        if (valid) {
          const { data: res } = await handleUpdateExamClassroom(this.editForm)
          if (res.status !== '2000') return this.$message.error(res.message)
          this.$message.success(res.message)
          this.editDialogVisible = false
          await this.getExamClassroomPage()
        }
      })
    },
    // 删除教室
    deleteExamClassroom (examClassroomId) {
      this.$confirm('此操作将删除该教室, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        const { data: res } = await handleDeleteExamClassroom(examClassroomId)
        if (res.status !== '2000') {
          return this.$message.error(res.message)
        }
        this.$message({
          type: 'success',
          message: res.message
        })
        await this.getExamClassroomPage()
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    }
  },
  mounted () {
    this.examSiteId = this.$route.query.id
    this.getExamClassroomPage()
  }
}
</script>

<style lang="less" scoped>
.date-dialog {
  width: 295px;
}
</style>
