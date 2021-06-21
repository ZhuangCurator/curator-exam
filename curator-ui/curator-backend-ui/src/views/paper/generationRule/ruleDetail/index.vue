<template>
  <div class="ruleDetail_container">
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>规则详情管理</el-breadcrumb-item>
      <el-breadcrumb-item>规则详情列表</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图区域-->
    <el-card class="box-card">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-button type="primary" size="mini" v-has-perm="['paper:ruleDetail:add']" @click="showAddDialog">添加规则详情</el-button>
        </el-col>
      </el-row>
      <!--  规则详情列表区域 -->
      <el-table :data="ruleDetailList" border stripe>
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
        <el-table-column label="试题类型" prop="questionTypeDesc" align="center"></el-table-column>
        <el-table-column label="试题难度" prop="questionDifficultyDesc" align="center"></el-table-column>
        <el-table-column label="试题个数" prop="questionNumber" align="center"></el-table-column>
        <el-table-column label="单个试题分数" prop="questionPoint" align="center"></el-table-column>
        <el-table-column label="组卷顺序" prop="detailSort" align="center"></el-table-column>
        <el-table-column label="简介" prop="summary" align="center"></el-table-column>
        <el-table-column label="操作" width="300px;" align="center" v-if="columnShow">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" size="mini" v-has-perm="['paper:ruleDetail:update']" @click="showEditDialog(scope.row.generationRuleDetailId)">编辑</el-button>
            <el-button type="danger" icon="el-icon-delete" size="mini" v-has-perm="['paper:ruleDetail:delete']" @click="deleteGenerationRuleDetail(scope.row.generationRuleDetailId)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加规则详情对话框 -->
    <el-dialog title="添加规则详情" :visible.sync="addDialogVisible" width="30%" @close="handleAddDialogClose">
      <!-- 对话框主题区域 -->
      <el-form ref="addFormRef" :model="addForm" :rules="dialogFormRules" label-width="80px">
        <el-form-item label="试题类型" prop="questionType">
          <el-select v-model="addForm.questionType" placeholder="请选择试题类型" size="small">
            <el-option :key="1" label="单选" :value="1"></el-option>
            <el-option :key="2" label="多选" :value="2"></el-option>
            <el-option :key="3" label="判断" :value="3"></el-option>
            <el-option :key="4" label="填空" :value="4"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="试题难度" prop="questionDifficulty">
          <el-select v-model="addForm.questionDifficulty" placeholder="请选择试题难度" size="small">
            <el-option :key="1" label="初级" :value="1"></el-option>
            <el-option :key="2" label="中级" :value="2"></el-option>
            <el-option :key="3" label="高级" :value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="试题分数" prop="questionPoint">
          <el-input v-model="addForm.questionPoint"></el-input>
        </el-form-item>
        <el-form-item label="试题个数" prop="questionNumber">
          <el-input v-model="addForm.questionNumber"></el-input>
        </el-form-item>
        <el-form-item label="组卷顺序" prop="detailSort">
          <el-input v-model="addForm.detailSort"></el-input>
        </el-form-item>
        <el-form-item label="简介" prop="summary">
          <el-input type="textarea" :rows="2" v-model="addForm.summary"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部按钮区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addGenerationRuleDetail">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 修改规则详情对话框 -->
    <el-dialog title="编辑规则详情" :visible.sync="editDialogVisible" width="30%" @close="handleEditDialogClose">
      <!-- 对话框主题区域 -->
      <el-form ref="editFormRef" :model="editForm" :rules="dialogFormRules" label-width="80px">
        <el-form-item label="试题类型" prop="questionType">
          <el-select v-model="editForm.questionType" placeholder="请选择试题类型" size="small">
            <el-option :key="1" label="单选" :value="1"></el-option>
            <el-option :key="2" label="多选" :value="2"></el-option>
            <el-option :key="3" label="判断" :value="3"></el-option>
            <el-option :key="4" label="填空" :value="4"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="试题难度" prop="questionDifficulty">
          <el-select v-model="editForm.questionDifficulty" placeholder="请选择试题难度" size="small">
            <el-option :key="1" label="初级" :value="1"></el-option>
            <el-option :key="2" label="中级" :value="2"></el-option>
            <el-option :key="3" label="高级" :value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="试题分数" prop="questionPoint">
          <el-input v-model="editForm.questionPoint"></el-input>
        </el-form-item>
        <el-form-item label="试题个数" prop="questionNumber">
          <el-input v-model="editForm.questionNumber"></el-input>
        </el-form-item>
        <el-form-item label="组卷顺序" prop="detailSort">
          <el-input v-model="editForm.detailSort"></el-input>
        </el-form-item>
        <el-form-item label="简介" prop="summary">
          <el-input type="textarea" :rows="2" v-model="editForm.summary"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部按钮区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="editGenerationRuleDetail">确 定</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
import {
  handleAddGenerationRuleDetail,
  handleDeleteGenerationRuleDetail,
  handleUpdateGenerationRuleDetail,
  handleGenerationRuleDetailList,
  handleGenerationRuleDetailQuery
} from '@/apis/paper/generationRuleDetail'
import { showElement } from '@/utils/show'

export default {
  name: 'GenerationRuleDetailPage',
  data () {
    return {
      columnShow: true,
      generationRuleId: undefined,
      // 规则详情列表
      ruleDetailList: [],
      // 控制添加规则详情对话框的显示
      addDialogVisible: false,
      // 添加规则详情表单数据
      addForm: {
        generationRuleId: undefined,
        questionType: undefined,
        questionDifficulty: undefined,
        questionNumber: undefined,
        questionPoint: undefined,
        detailSort: undefined,
        summary: undefined
      },
      // 控制修改规则详情对话框的显示
      editDialogVisible: false,
      // 修改规则详情表单数据
      editForm: {
        generationRuleDetailId: undefined,
        generationRuleId: undefined,
        questionType: undefined,
        questionDifficulty: undefined,
        questionNumber: undefined,
        questionPoint: undefined,
        detailSort: undefined,
        summary: undefined
      },
      // 规则详情表单校验规则
      dialogFormRules: {
        questionType: [
          {
            required: true,
            message: '请选择试题类型',
            trigger: 'blur'
          }
        ],
        questionDifficulty: [
          {
            required: true,
            message: '请选择试题难度',
            trigger: 'blur'
          }
        ],
        questionNumber: [
          {
            pattern: /^[0-9]+$/,
            message: '请输入正整数',
            trigger: 'blur'
          },
          {
            required: true,
            message: '请输入试题个数',
            trigger: 'blur'
          }
        ],
        questionPoint: [
          {
            pattern: /^[0-9]+$/,
            message: '请输入正整数',
            trigger: 'blur'
          },
          {
            required: true,
            message: '请输入单个试题分数',
            trigger: 'blur'
          }
        ],
        detailSort: [
          {
            pattern: /^[0-9]+$/,
            message: '请输入正整数',
            trigger: 'blur'
          },
          {
            required: true,
            message: '请输入组卷顺序',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  updated () {
    this.showTableColumn()
  },
  methods: {
    // 根据权限数据展示操作列
    showTableColumn () {
      this.columnShow = showElement(['paper:ruleDetail:update', 'paper:ruleDetail:delete'])
    },
    // 查询表单重置
    resetQueryForm () {
      this.queryForm.ruleDetailName = undefined
      this.queryForm.current = 1
      this.queryForm.pageSize = 20
      this.getGenerationRuleDetailList()
    },
    // 得到规则详情分页数据
    async getGenerationRuleDetailList () {
      const { data: res } = await handleGenerationRuleDetailList(this.generationRuleId)
      console.log(res.data)
      if (res.status !== '2000') return this.$message.error(res.message)
      this.ruleDetailList = res.data
    },
    // 监听 添加规则详情对话框关闭事件
    handleAddDialogClose () {
      // 清空字段
      this.$refs.addFormRef.resetFields()
    },
    // 展示添加规则详情对话框
    showAddDialog () {
      this.addDialogVisible = true
    },
    // 添加规则详情
    addGenerationRuleDetail () {
      this.$refs.addFormRef.validate(async valid => {
        if (valid) {
          this.addForm.generationRuleId = this.generationRuleId
          const { data: res } = await handleAddGenerationRuleDetail(this.addForm)
          if (res.status !== '2000') return this.$message.error(res.message)
          this.$message.success(res.message)
          this.addDialogVisible = false
          await this.getGenerationRuleDetailList()
        }
      })
    },
    // 展示编辑规则详情对话框
    async showEditDialog (ruleDetailId) {
      const { data: res } = await handleGenerationRuleDetailQuery(ruleDetailId)
      console.log(res)
      if (res.status !== '2000') {
        return this.$message.error(res.message)
      }
      // 查询出规则详情 在展示编辑框
      this.editDialogVisible = true
      this.editForm = res.data
    },
    // 监听 编辑规则详情对话框关闭事件
    handleEditDialogClose () {
      // 清空字段
      this.$refs.editFormRef.resetFields()
    },
    // 确定编辑规则详情
    editGenerationRuleDetail () {
      this.$refs.editFormRef.validate(async valid => {
        if (valid) {
          const { data: res } = await handleUpdateGenerationRuleDetail(this.editForm)
          if (res.status !== '2000') return this.$message.error(res.message)
          this.$message.success(res.message)
          this.editDialogVisible = false
          await this.getGenerationRuleDetailList()
        }
      })
    },
    // 删除规则详情
    deleteGenerationRuleDetail (ruleDetailId) {
      this.$confirm('此操作将删除该规则详情, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        const { data: res } = await handleDeleteGenerationRuleDetail(ruleDetailId)
        if (res.status !== '2000') {
          return this.$message.error(res.message)
        }
        this.$message({
          type: 'success',
          message: res.message
        })
        await this.getGenerationRuleDetailList()
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    }
  },
  mounted () {
    this.generationRuleId = this.$route.query.id
    this.getGenerationRuleDetailList()
  }
}
</script>

<style lang="less" scoped>
.el-tree {
  border: solid 1px #e7e1cd;
}
.el-select {
  width: 100%;
}
</style>
