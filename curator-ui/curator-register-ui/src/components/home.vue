<template>
  <el-container class="home_container">
    <el-header class="header_container">
        <div class="home_title">
          <span>Curator-Cloud管理系统</span>
        </div>
    </el-header>
    <el-main></el-main>
<!--    <el-footer></el-footer>-->
  </el-container>
</template>

<script>

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
      userName: '',
      avatar: ''
    }
  },
  methods: {
    // 退出登录
    logout () {
      this.$confirm('确定注销并退出系统吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        window.sessionStorage.clear()
        this.$store.dispatch('handleStoreClean').then(() => {
          location.href = '/login'
        })
      })
    }
  }
}
</script>

<style lang="less">
.home_container {
  height: 100%;
}

.header_container {
  height: 300px !important;
  background-image: url('../assets/banner01.png');
  //background-color: #373d41;
  //padding-left: 0;
  //// 垂直居中
  //align-items: center;
  //// 字体颜色
  //color: #fff;
  //font-size: 20px;
  //.header_avatar {
  //  display: flex;
  //  float: right;
  //  align-items: center;
  //}
  .home_title {
    display: flex;
    align-items: center;

    span {
      margin-left: 20px;
    }
  }
}
</style>
