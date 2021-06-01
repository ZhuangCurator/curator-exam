<template>
  <div class="menu_container">
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>菜单管理</el-breadcrumb-item>
      <el-breadcrumb-item>菜单列表</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图区域-->
    <el-card class="box-card">
      <!--  搜索与添加区域 -->
      <el-form :model="queryForm" ref="queryFormRef" inline label-width="68px">
        <el-form-item label="菜单名称">
          <el-input
            v-model="queryForm.menuName"
            placeholder="菜单名称"
            clearable
            size="small"
            style="width: 240px"
            @keyup.enter.native="getMenuList"
          />
        </el-form-item>
        <el-form-item label="菜单状态">
          <el-select v-model="queryForm.status" placeholder="请选择菜单状态" size="small">
            <el-option :key="0" label="启用" :value="0"></el-option>
            <el-option :key="1" label="禁用" :value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item class="button_container">
          <el-button type="info" icon="el-icon-search" size="small" @click="getMenuList">搜索</el-button>
          <el-button type="warning" icon="el-icon-refresh" size="small" @click="resetQueryForm">重置</el-button>
        </el-form-item>
      </el-form>

      <!--  菜单列表区域 -->
      <el-table
        :data="tableDataList"
        style="width: 100%;margin-bottom: 20px;"
        row-key="menuId"
        border
        :tree-props="{children: 'children'}">
        <el-table-column prop="menuName" label="菜单名称" :show-overflow-tooltip="true" width="130px"></el-table-column>
        <el-table-column prop="menuTypeDesc" align="center" label="菜单类型" width="80px"></el-table-column>
        <el-table-column prop="perms" label="权限标识" width="150px"></el-table-column>
        <el-table-column prop="path" label="路由地址" width="80px"></el-table-column>
        <el-table-column prop="component" label="组件路径"></el-table-column>
        <el-table-column prop="icon" label="菜单图标" width="150px"></el-table-column>
        <el-table-column label="菜单状态" align="center" width="70px">
          <template slot-scope="scope">
            <el-switch v-model="scope.row.status" :active-value="0" :inactive-value="1"
                       @change="changeMenuStatus(scope.row)"></el-switch>
          </template>
        </el-table-column>
        <el-table-column prop="sort" align="center" label="排序" width="50px"></el-table-column>
        <el-table-column prop="createTime" align="center" label="创建时间" width="140px">
        </el-table-column>
        <el-table-column label="操作" align="center" width="300px" v-if="columnShow">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" size="mini" v-has-perm="['system:menu:add']" @click="showAddDialog(scope.row.menuId)">添加</el-button>
            <el-button type="info" icon="el-icon-edit" size="mini" v-has-perm="['system:menu:update']" @click="showEditDialog(scope.row.menuId)">编辑</el-button>
            <el-button type="danger" icon="el-icon-delete" size="mini" v-has-perm="['system:menu:deleted']" @click="deleteMenu(scope.row.menuId)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 修改菜单对话框 -->
    <el-dialog title="菜单编辑" :visible.sync="updateDialogVisible" width="30%" @close="handleUpdateDialogClose">
      <!-- 对话框主题区域 -->
      <el-form ref="updateDialogFormRef" :model="updateDialogForm" :rules="dialogFormRules" label-width="80px">
        <el-form-item label="上级菜单" prop="parentId">
          <tree-select
            :options="menuOptions"
            :normalizer="normalizer"
            placeholder="请选择上级菜单..."
            v-model="updateDialogForm.parentId"
          />
        </el-form-item>
        <el-form-item label="菜单名称" prop="menuName">
          <el-input v-model="updateDialogForm.menuName"></el-input>
        </el-form-item>
        <el-form-item label="菜单类型" prop="menuType">
          <el-radio-group v-model="updateDialogForm.menuType">
            <el-radio :label="1">菜单</el-radio>
            <el-radio :label="2">按钮</el-radio>
            <el-radio :label="3">目录</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="权限标识" prop="perms" v-if="updateDialogForm.menuType !== 3">
          <el-input v-model="updateDialogForm.perms"></el-input>
        </el-form-item>
        <el-form-item label="路由地址" prop="path" v-if="updateDialogForm.menuType === 1">
          <el-input v-model="updateDialogForm.path"></el-input>
        </el-form-item>
        <el-form-item label="组件路径" prop="component" v-if="updateDialogForm.menuType === 1">
          <el-input v-model="updateDialogForm.component"></el-input>
        </el-form-item>
        <el-form-item label="菜单排序" prop="sort">
          <el-input-number v-model="updateDialogForm.sort" controls-position="right" :min="1"></el-input-number>
        </el-form-item>
        <el-form-item label="菜单状态" prop="status">
          <el-radio-group v-model="updateDialogForm.status">
            <el-radio disabled :label="0">启用</el-radio>
            <el-radio disabled :label="1">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="菜单图标" prop="icon" v-if="updateDialogForm.menuType !== 2">
          <el-input v-model="updateDialogForm.icon"></el-input>
        </el-form-item>
        <el-form-item label="菜单备注">
          <el-input v-model="updateDialogForm.remark"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部按钮区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleUpdateDialogClose">取 消</el-button>
        <el-button type="primary" @click="editMenu">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 修改菜单对话框 -->
    <el-dialog title="菜单添加" :visible.sync="addDialogVisible" width="30%" @close="handleAddDialogClose">
      <!-- 对话框主题区域 -->
      <el-form ref="addDialogFormRef" :model="addDialogForm" :rules="dialogFormRules" label-width="80px">
        <el-form-item label="上级菜单" prop="parentId">
          <tree-select
            :options="menuOptions"
            :normalizer="normalizer"
            placeholder="请选择上级菜单..."
            v-model="addDialogForm.parentId"
          />
        </el-form-item>
        <el-form-item label="菜单名称" prop="menuName">
          <el-input v-model="addDialogForm.menuName"></el-input>
        </el-form-item>
        <el-form-item label="菜单类型" prop="menuType">
          <el-radio-group v-model="addDialogForm.menuType">
            <el-radio :label="1">菜单</el-radio>
            <el-radio :label="2">按钮</el-radio>
            <el-radio :label="3">目录</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="权限标识" prop="perms" v-if="addDialogForm.menuType !== 3">
          <el-input v-model="addDialogForm.perms"></el-input>
        </el-form-item>
        <el-form-item label="路由地址" prop="path" v-if="addDialogForm.menuType === 1">
          <el-input v-model="addDialogForm.path"></el-input>
        </el-form-item>
        <el-form-item label="组件路径" prop="component" v-if="addDialogForm.menuType === 1">
          <el-input v-model="addDialogForm.component"></el-input>
        </el-form-item>
        <el-form-item label="菜单排序" prop="sort">
          <el-input-number v-model="addDialogForm.sort" controls-position="right" :min="1"></el-input-number>
        </el-form-item>
        <el-form-item label="菜单状态" prop="status">
          <el-radio-group v-model="addDialogForm.status">
            <el-radio :label="0">启用</el-radio>
            <el-radio :label="1">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="菜单图标" prop="icon" v-if="addDialogForm.menuType !== 2">
          <el-input v-model="addDialogForm.icon"></el-input>
        </el-form-item>
        <el-form-item label="菜单备注" prop="remark">
          <el-input type="textarea" :rows="2" v-model="addDialogForm.remark"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部按钮区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleAddDialogClose">取 消</el-button>
        <el-button type="primary" @click="addMenu">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { handleMenuList, handleMenuQuery, handleAddMenu, handleUpdateMenu, handleDeleteMenu, handleUpdateMenuStatus } from '@/apis/menu'
import TreeSelect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import { showElement } from '@/utils/show'

export default {
  name: 'MenuPage',
  components: { TreeSelect },
  data () {
    return {
      columnShow: true,
      // 查询表单数据
      queryForm: {
        menuName: undefined,
        status: undefined
      },
      // 菜单树选项
      menuOptions: [],
      // 表格数据列表
      tableDataList: [],
      // 控制添加对话框的展示隐藏
      addDialogVisible: false,
      // 控制编辑对话框的展示隐藏
      updateDialogVisible: false,
      // 添加对话框表单数据
      addDialogForm: {
        parentId: undefined,
        menuName: undefined,
        sort: undefined,
        status: undefined,
        menuType: undefined,
        path: undefined,
        perms: undefined,
        component: undefined,
        icon: undefined,
        remark: undefined
      },
      // 编辑对话框表单数据
      updateDialogForm: {
        parentId: undefined,
        menuId: undefined,
        menuName: undefined,
        sort: undefined,
        status: undefined,
        menuType: undefined,
        path: undefined,
        perms: undefined,
        component: undefined,
        icon: undefined,
        remark: undefined
      },
      dialogFormRules: {
        parentId: [
          {
            required: true,
            message: '请选择上级菜单',
            trigger: 'blur'
          }
        ],
        menuName: [
          {
            required: true,
            message: '请输入账户名',
            trigger: 'blur'
          }
        ],
        menuType: [
          {
            required: true,
            message: '请选择菜单类型',
            trigger: 'change'
          }
        ],
        status: [
          {
            required: true,
            message: '请选择菜单状态',
            trigger: 'change'
          }
        ],
        sort: [
          {
            required: true,
            message: '请输入菜单排序',
            trigger: 'blur'
          }
        ],
        path: [
          {
            required: true,
            message: '请输入路由地址',
            trigger: 'blur'
          }
        ],
        perms: [
          {
            required: true,
            message: '请输入权限标识',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  created () {
    this.getMenuList()
  },
  updated () {
    this.showTableColumn()
  },
  methods: {
    // 根据权限数据展示操作列
    showTableColumn () {
      this.columnShow = showElement(['system:menu:update', 'system:menu:deleted', 'system:menu:add'])
    },
    // 查询表单重置
    resetQueryForm () {
      this.queryForm.menuName = undefined
      this.queryForm.status = undefined
      this.getMenuList()
    },
    // 获取菜单数据列表
    async getMenuList () {
      const { data: res } = await handleMenuList(this.queryForm)
      console.log(res.data)
      this.tableDataList = res.data
    },
    // 转换菜单数据结构
    normalizer (node) {
      if (!node.children) {
        delete node.children
      }
      return {
        id: node.menuId,
        label: node.menuName,
        children: node.children
      }
    },
    // 展示编辑对话框
    async showEditDialog (menuId) {
      const { data: res } = await handleMenuQuery(menuId)
      if (res.status !== '2000') return this.$message.error(res.message())
      // 获取所有的菜单数据
      const { data: result } = await handleMenuList()
      if (result.status !== '2000') return this.$message.error(result.message)
      const topMenu = { menuId: 0, menuName: '顶级菜单', children: [] }
      topMenu.children = result.data
      this.menuOptions.push(topMenu)
      // 展示对话框
      this.updateDialogVisible = true
      this.updateDialogForm = res.data
    },
    // 展示添加对话框
    async showAddDialog (menuId) {
      // 获取所有的菜单数据
      const { data: result } = await handleMenuList()
      if (result.status !== '2000') return this.$message.error(result.message)
      const topMenu = { menuId: 0, menuName: '顶级菜单', children: [] }
      topMenu.children = result.data
      this.menuOptions.push(topMenu)
      // 展示对话框
      this.addDialogVisible = true
      this.addDialogForm.parentId = menuId
      this.addDialogForm.sort = 1
      this.addDialogForm.status = 0
      this.addDialogForm.menuType = 1
    },
    // 监听 添加对话框关闭事件
    handleAddDialogClose () {
      // 清空字段
      this.$refs.addDialogFormRef.resetFields()
      this.addDialogVisible = false
      this.menuOptions = []
    },
    // 监听 编辑对话框关闭事件
    handleUpdateDialogClose () {
      // 清空字段
      this.$refs.updateDialogFormRef.resetFields()
      this.updateDialogVisible = false
      this.menuOptions = []
    },
    // 确定编辑菜单
    editMenu () {
      this.$refs.updateDialogFormRef.validate(async valid => {
        if (valid) {
          const { data: res } = await handleUpdateMenu(this.updateDialogForm)
          if (res.status !== '2000') return this.$message.error(res.message)
          this.$message.success(res.message)
          this.updateDialogVisible = false
          await this.getMenuList()
        }
      })
    },
    // 确定添加菜单
    addMenu () {
      this.$refs.addDialogFormRef.validate(async valid => {
        if (valid) {
          const { data: res } = await handleAddMenu(this.addDialogForm)
          if (res.status !== '2000') return this.$message.error(res.message)
          this.$message.success(res.message)
          this.addDialogVisible = false
          await this.getMenuList()
        }
      })
    },
    // 删除菜单
    deleteMenu (menuId) {
      this.$confirm('此操作将删除该菜单, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        const { data: res } = await handleDeleteMenu(menuId)
        if (res.status !== '2000') {
          return this.$message.error(res.message)
        }
        this.$message({
          type: 'success',
          message: res.message
        })
        await this.getMenuList()
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    // 监听switch变化，改变菜单状态
    async changeMenuStatus (menuInfo) {
      const str = menuInfo.status === 0 ? '启用' : '禁用'
      this.$confirm('此操作将' + str + '该菜单及其子菜单, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        const param = {}
        param.menuId = menuInfo.menuId
        param.status = menuInfo.status

        const { data: res } = await handleUpdateMenuStatus(param)
        if (res.status !== '2000') {
          // 更新失败的话，将页面的状态修改回去
          menuInfo.status = str === '启用' ? 1 : 0
          return this.$message.error(res.message)
        }
        await this.getMenuList()
        this.$message.success('该菜单及其子菜单' + str + '成功!')
      }).catch(() => {
        // 取消修改的话，将页面的状态修改回去
        menuInfo.status = str === '启用' ? 1 : 0
        this.$message({
          type: 'info',
          message: '已取消' + str + '该菜单及其子菜单'
        })
      })
    }
  }
}
</script>

<style lang="less" scoped>
.el-input-number {
  width: 100%;
}
</style>
