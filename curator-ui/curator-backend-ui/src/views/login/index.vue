<template>
  <div class="login_container">
    <el-card class="login_box">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="帐号密码登录" name="loginForm">
          <login-form />
        </el-tab-pane>
        <el-tab-pane label="手机免密登录" name="loginMobile">
          <login-mobile />
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import loginForm from './loginForm'
import loginMobile from './loginMobile'

export default {
  name: 'Login',
  components: { loginForm, loginMobile },
  data () {
    return {
      activeTab: 'loginForm'
    }
  }
}
</script>

<style lang="less">
.login_container {
  background-image: url('../../assets/background.jpg');
  height: 100%;
  .el-card__body {
    padding-bottom: 0;
  }
}

.login_box {
  background-color: #fff;
  width: 450px;
  // 圆角边框
  border-radius: 3px;
  // 绝对位置
  position: absolute;
  // 以左上角为原点，左边框 距离浏览器左边50%，上边框 距离浏览器上边50%
  left: 50%;
  top: 50%;
  // translate(-50%,-50%) 作用是，往上（x轴）,左（y轴）移动自身长宽的 50%，以使其居于中心位置
  transform: translate(-50%, -50%);
}
</style>
