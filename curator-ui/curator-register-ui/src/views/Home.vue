<template>
  <div class="home">
    <el-header style="height: 300px;">
      <div class="header-div" v-if="this.$store.state.accountName === ''">
        <el-button type="success" icon="el-icon-setting" size="mini" @click="handleLogin()">登录/注册</el-button>
      </div>
      <div class="header-div" v-else>
        <el-button  icon="el-icon-user" size="mini">{{ this.$store.state.accountName }}</el-button>
        <el-button  icon="el-icon-setting" size="mini" @click="handleLogout()">退出</el-button>
      </div>
      <div class="header-title">
        <img src="../assets/header-logo.png">
      </div>
    </el-header>
    <el-main>
      <div class="menu-div">
        <el-menu :default-active="this.$store.state.activeMenu" router mode="horizontal" @select="handleSelect">
          <el-menu-item index="notice" key="1">考试公告</el-menu-item>
          <el-menu-item index="subject" key="2">考试报名</el-menu-item>
          <el-menu-item index="score" key="3">成绩查询</el-menu-item>
        </el-menu>
      </div>
        <!-- 路由占位符 -->
        <router-view/>
    </el-main>
    <el-footer style="height: 100px;">
      <div class="info-div">
        <span>版权所有：馆长开发有限公司</span>
        <span>信息维护：馆长开发研究中心</span>
        <span>备案编号：京XXX备XXXXXXXXX号-1</span>
      </div>
    </el-footer>
  </div>
</template>

<script>

export default {
  name: 'Home',
  data () {
    return {
      activeMenu: this.$store.state.activeMenu
    }
  },
  methods: {
    handleLogin () {
      // 没有登录则跳转到登录页
      this.$router.push({
        path: 'login'
      })
    },
    handleLogout () {
      // 清空 vuex
      this.$store.commit('resetState')
      // 清空 sessionStorage
      sessionStorage.clear()
      this.$store.commit('setActiveMenu', 'notice')
      // 没有登录则跳转到登录页
      this.$router.push({
        path: 'notice'
      })
    },
    handleSelect (key) {
      this.activeMenu = key
      this.$store.commit('setActiveMenu', key)
    }
  }
}
</script>

<style lang="less">
.el-header {
  background-image: url('../assets/banner01.png');
}
.el-header .header-div {
  float: right;
  margin-top: 15px;
}
.el-header .header-title {
  float: left;
  text-align: center;
  width:60%;
  margin-top: 150px;
  margin-left: 20%;
  font-size: 35px;
  font-weight: 500;
  color: darkblue;
}
.el-main {
  width: 70%;
  min-height: 500px;
  margin: 0 auto;
}
.el-footer {
  background: #00061b;
  width: 100%;
  margin: 0 auto;
  color: #999;
  text-align: center;
  line-height: 30px;
  > .info-div {
    padding: 38px 0 60px;
    > span {
      display: inline-block;
      width: 300px;
    }
  }
}

.el-menu-item {
  padding-right: 50px !important;
}
.menu-div {
  padding-bottom: 10px;
}
</style>
