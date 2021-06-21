<template>
  <div class="examNotice_container">
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>考试公告管理</el-breadcrumb-item>
      <el-breadcrumb-item>考试公告列表</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图区域-->
    <el-card class="box-card">
      <!--  搜索与添加区域 -->
      <el-form :model="queryForm" ref="queryFormRef" :inline="true" label-width="100px">
        <el-form-item label="考试公告名称">
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
          <el-button type="info" icon="el-icon-search" size="small" @click="getExamNoticePage">搜索</el-button>
          <el-button type="warning" icon="el-icon-refresh" size="small" @click="resetQueryForm">重置</el-button>
        </el-form-item>
      </el-form>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-button type="primary" size="mini" v-has-perm="['register:notice:add']" @click="showAddDialog">添加考试公告</el-button>
        </el-col>
      </el-row>

      <!--  考试公告列表区域 -->
      <el-table :data="examNoticeList" border stripe>
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
        <el-table-column label="公告名称" prop="examNoticeName" align="center"></el-table-column>
        <el-table-column label="公告内容" align="center" width="200px">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-document" size="mini" @click="showPreviewDialog(scope.row.content)">内容预览</el-button>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="300px;" align="center" v-if="columnShow">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" size="mini" v-has-perm="['register:notice:update']" @click="showEditDialog(scope.row.examNoticeId)">编辑</el-button>
            <el-button type="danger" icon="el-icon-delete" size="mini" v-has-perm="['register:notice:delete']" @click="deleteExamNotice(scope.row.examNoticeId)">删除</el-button>
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

    <!-- 添加考试公告对话框 -->
    <el-dialog title="添加考试公告" :visible.sync="addDialogVisible" width="66%" @close="handleAddDialogClose">
      <!-- 对话框主题区域 -->
      <el-form ref="addFormRef" :model="addForm" :rules="dialogFormRules" label-width="120px">
        <el-form-item label="考试公告名" prop="examNoticeName">
          <el-input v-model="addForm.examNoticeName"></el-input>
        </el-form-item>
        <el-form-item label="公告内容" prop="content" v-if="addDialogVisible">
          <editor :content="addForm.content" :isClear="isClear" @change="changeContentWithAdd"></editor>
        </el-form-item>
      </el-form>
      <!-- 底部按钮区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addExamNotice">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 修改考试公告对话框 -->
    <el-dialog title="编辑考试公告" :visible.sync="editDialogVisible" width="66%" @close="handleEditDialogClose">
      <!-- 对话框主题区域 -->
      <el-form ref="editFormRef" :model="editForm" :rules="dialogFormRules" label-width="120px">
        <el-form-item label="考试公告名" prop="examNoticeName">
          <el-input v-model="editForm.examNoticeName"></el-input>
        </el-form-item>
        <el-form-item label="公告内容" prop="content"  v-if="editDialogVisible">
          <editor :content="editForm.content" :isClear="isClear" @change="changeContentWithEdit"></editor>
        </el-form-item>
      </el-form>
      <!-- 底部按钮区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="editExamNotice">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 预览考试公告对话框 -->
    <el-dialog title="预览考试公告" :visible.sync="previewDialogVisible" width="1020px">
      <!-- 对话框主题区域 -->
      <div class="content_main" style="width: 980px; padding: 0 10px;" v-html="previewContent"></div>
    </el-dialog>
  </div>
</template>

<script>
import {
  handleAddExamNotice,
  handleDeleteExamNotice,
  handleUpdateExamNotice,
  handleExamNoticePage,
  handleExamNoticeQuery
} from '@/apis/register/examNotice'
import { showElement } from '@/utils/show'

import Editor from '@/components/editor'
export default {
  name: 'ExamNoticePage',
  components: { Editor },
  data () {
    return {
      isClear: false,
      content: '',
      columnShow: true,
      // 是否是超级管理员
      superAdmin: 0,
      // 查询表单参数
      queryForm: {
        // 考试公告名
        examNoticeName: undefined,
        // 当前页
        current: 1,
        // 当前页大小
        pageSize: 20
      },
      // 考试公告列表
      examNoticeList: [],
      // 考试公告总数
      total: 0,
      // 控制添加考试公告对话框的显示
      addDialogVisible: false,
      // 添加考试公告表单数据
      addForm: {
        examNoticeName: undefined,
        content: undefined
      },
      // 控制预览考试公告对话框的显示
      previewDialogVisible: false,
      // 预览考试公告内容
      previewContent: undefined,
      // 控制修改考试公告对话框的显示
      editDialogVisible: false,
      // 修改考试公告表单数据
      editForm: {
        examNoticeId: undefined,
        examNoticeName: undefined,
        content: undefined
      },
      // 考试公告表单校验规则
      dialogFormRules: {
        examNoticeName: [
          {
            required: true,
            message: '请输入考试公告名',
            trigger: 'blur'
          }
        ],
        content: [
          {
            required: true,
            message: '请输入公告内容',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  created () {
    this.superAdmin = this.$store.state.superAdmin
    this.getExamNoticePage()
  },
  updated () {
    this.showTableColumn()
  },
  methods: {
    // 根据权限数据展示操作列
    showTableColumn () {
      this.columnShow = showElement(['register:notice:update', 'register:notice:delete'])
    },
    // 查询表单重置
    resetQueryForm () {
      this.queryForm.examNoticeName = undefined
      this.queryForm.current = 1
      this.queryForm.pageSize = 20
      this.getExamNoticePage()
    },
    // 得到考试公告分页数据
    async getExamNoticePage () {
      this.queryForm.superAdmin = this.superAdmin
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
    },
    // 监听 添加考试公告对话框关闭事件
    handleAddDialogClose () {
      // 清空字段
      this.$refs.addFormRef.resetFields()
    },
    // 展示添加考试公告对话框
    showAddDialog () {
      this.addDialogVisible = true
    },
    // 添加考试公告
    addExamNotice () {
      this.$refs.addFormRef.validate(async valid => {
        if (valid) {
          const { data: res } = await handleAddExamNotice(this.addForm)
          if (res.status !== '2000') return this.$message.error(res.message)
          this.$message.success(res.message)
          this.addDialogVisible = false
          await this.getExamNoticePage()
        }
      })
    },
    // 展示预览公告内容对话框
    showPreviewDialog (content) {
      this.previewContent = content
      this.previewDialogVisible = true
    },
    // 展示编辑考试公告对话框
    async showEditDialog (examNoticeId) {
      const { data: res } = await handleExamNoticeQuery(examNoticeId)
      console.log(res)
      if (res.status !== '2000') {
        return this.$message.error(res.message)
      }
      this.editForm = res.data
      // 查询出考试公告 在展示编辑框
      this.editDialogVisible = true
    },
    // 监听 编辑考试公告对话框关闭事件
    handleEditDialogClose () {
      // 清空字段
      this.$refs.editFormRef.resetFields()
    },
    // 确定编辑考试公告
    editExamNotice () {
      this.$refs.editFormRef.validate(async valid => {
        if (valid) {
          const { data: res } = await handleUpdateExamNotice(this.editForm)
          if (res.status !== '2000') return this.$message.error(res.message)
          this.$message.success(res.message)
          this.editDialogVisible = false
          await this.getExamNoticePage()
        }
      })
    },
    // 删除考试公告
    deleteExamNotice (examNoticeId) {
      this.$confirm('此操作将删除该考试公告, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        const { data: res } = await handleDeleteExamNotice(examNoticeId)
        if (res.status !== '2000') {
          return this.$message.error(res.message)
        }
        this.$message({
          type: 'success',
          message: res.message
        })
        await this.getExamNoticePage()
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    // 编辑对话框中 富文本框值
    changeContentWithEdit (val) {
      this.editForm.content = val
    },
    // 添加对话框中 富文本框值
    changeContentWithAdd (val) {
      this.addForm.content = val
    }
  }
}
</script>

<style lang="less" scoped>

</style>
