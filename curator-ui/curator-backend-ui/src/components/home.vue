<template>
  <el-container class="home_container">
    <el-header>
      <el-row :gutter="10">
        <el-col :span="8">
          <div class="home_title">
            <img src="../assets/222.png" alt="">
            <span>Curator-Cloud管理系统</span>
          </div>
        </el-col>
        <el-col :span="16">
          <el-dropdown class="header_name">
            <span class="el-dropdown-link">
              <span>{{ userName }}</span>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item >个人中心</el-dropdown-item>
              <el-dropdown-item divided @click.native="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-col>
      </el-row>
    </el-header>
    <el-container>
      <el-aside :width="isCollapse ? '64px' : '200px'">
        <!-- 折叠按钮 -->
        <div class="toggle-button" @click="toggleCollapse">|||</div>
        <!-- 菜单栏 -->
        <el-menu background-color="#333744" text-color="#fff" active-text-color="#409EFF"
                 unique-opened :collapse="isCollapse" :collapse-transition="false" router :default-active="activePath">
          <template v-for="item in routers" >
            <!-- 含有子菜单的一级菜单 -->
            <el-submenu :index="item.path" :key="item.path" v-if="item.children">
              <template slot="title">
                <i :class="item.meta.icon"></i>
                <span>{{ item.meta.title }}</span>
              </template>
              <!-- 二级菜单 -->
              <el-menu-item :index="item.path + '/' + subItem.path"  v-for="subItem in item.children" :key="subItem.path" @click="saveActivePath(item.path + '/' + subItem.path)">
                <template slot="title">
                  <!-- 菜单图标 -->
                  <i class="el-icon-menu"></i>
                  <!-- 菜单文本 -->
                  <span slot="title">{{ subItem.meta.title }}</span>
                </template>
              </el-menu-item>
            </el-submenu>
<!--            &lt;!&ndash; 没有子菜单的一级菜单 &ndash;&gt;-->
<!--            <el-power-item :index="item.path" :key="item.path" v-else>-->
<!--              <i :class="item.meta.icon"></i>-->
<!--              <span>{{ item.meta.title }}</span>-->
<!--            </el-power-item>-->
          </template>
        </el-menu>
      </el-aside>
      <el-main>
        <!-- 路由占位符 -->
        <router-view/>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import { mapGetters } from 'vuex'
import { getAccountName } from '@/utils/storage'

export default {
  name: 'Home',
  data () {
    return {
      // 菜单列表
      menuList: [],
      // 菜单栏是否折叠
      isCollapse: false,
      // 菜单栏被激活的二级菜单
      activePath: '',
      userName: ''
    }
  },
  computed: {
    ...mapGetters([
      'routers'
    ])
  },
  methods: {
    // 退出登录
    logout () {
      window.sessionStorage.clear()
      this.$store.dispatch('handleStoreClean')
      this.$router.push('/login')
    },

    // 获取菜单列表
    // async getMenuList () {
    //   const params = {}
    //   params.status = 0
    //   params.menuType = 1
    //   const { data: res } = await handleMenuList(params)
    //   if (res.status === '2000') {
    //     this.menuList = res.data
    //   } else {
    //     this.$message.error(res.message)
    //   }
    // },
    // 点击按钮，切换菜单栏的折叠与展开
    toggleCollapse () {
      this.isCollapse = !this.isCollapse
    },
    saveActivePath (activePath) {
      this.activePath = activePath
      window.sessionStorage.setItem('activePath', activePath)
    }
  },
  created () {
    this.activePath = window.sessionStorage.getItem('activePath')
    this.userName = getAccountName()
  }
}
</script>

<style lang="less">
.home_container {
  height: 100%;

  img {
    height: 60px;
  }
}

.el-header {
  background-color: #373d41;
  // 使容器弹性布局
  //display: flex;
  // justify-content: 内容对齐属性， space-between： 两端对齐，中间平均分配距离
  //justify-content: space-between;
  padding-left: 0;
  // 垂直居中
  align-items: center;
  // 字体颜色
  color: #fff;
  font-size: 20px;
  .header_name {
    height: 55px;
    margin-top: 5px;
    display: flex;
    float: right;
    align-items: center;
    // 字体颜色
    color: #fff;
    font-size: 20px;
  }

  .home_title {
    display: flex;
    align-items: center;

    span {
      margin-left: 20px;
    }
  }
}

.el-aside {
  background-color: #333744;
  .el-menu {
    // 解决左侧菜单边框不齐整的问题
    border-right: none;
  }
}

.el-main {
  background-color: #eaedf1;
}

.iconfont {
  margin-right: 10px;
}

.toggle-button {
  background-color: #4a5064;
  font-size: 10px;
  line-height: 24px;
  color: #fff;
  text-align: center;
  // 增加或减少字符间的空白
  letter-spacing: 0.2em;
  // 光标呈现手的形状
  cursor: pointer;
}
</style>
