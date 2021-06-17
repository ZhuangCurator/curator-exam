<template>
  <div class="login_container">
    <h3 class="header_title">馆长全国综合考试系统</h3>
    <el-divider></el-divider>
    <div class="login_div">
      <el-form class="login_item" ref="loginFormRef" :model="loginForm" :rules="loginFormRules">
        <!-- 账户名 -->
        <el-form-item prop="accountName">
          <el-input v-model="loginForm.accountName" placeholder="请输入姓名" prefix-icon="iconfont icon-user"></el-input>
        </el-form-item>
        <!-- 密码 -->
        <el-form-item prop="accountPassword">
          <el-input v-model="loginForm.accountPassword" placeholder="请输入准考证号" type="password" prefix-icon="iconfont icon-mima"></el-input>
        </el-form-item>
        <!-- 图片验证码 -->
        <el-form-item prop="captcha">
          <el-input v-model="loginForm.captcha" prefix-icon="iconfont icon-yanzhengma" class="validate_input"
                    placeholder="请输入图片验证码"></el-input>
          <div class="validate_image_box" @click="refreshValidateCode">
            <img :src="validateCodeImage" alt="">
          </div>
        </el-form-item>
        <!-- 按钮区域  -->
        <el-form-item class="btn_item">
          <el-button type="primary" @click="login">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { getImageValidateCode, handleLogin } from '@/apis/auth/auth'

export default {
  name: 'Login',
  data () {
    return {
      // 登录表单数据绑定对象
      loginForm: {
        accountName: undefined,
        accountPassword: undefined,
        captcha: undefined
      },
      // 登录表单验证规则对象
      loginFormRules: {
        accountName: [
          {
            required: true,
            message: '请输入姓名',
            trigger: 'blur'
          }
        ],
        accountPassword: [
          {
            required: true,
            message: '请输入准考证号',
            trigger: 'blur'
          }
        ],
        captcha: [
          {
            required: true,
            message: '请输入图片验证码',
            trigger: 'blur'
          }
        ]
      },
      validateCodeImage: '',
      uuid: ''
    }
  },
  methods: {
    // 点击登录按钮，发起登录请求
    login () {
      // 首先进行表单的预验证
      this.$refs.loginFormRef.validate(async valid => {
        if (valid) {
          const data = this.loginForm
          data.uuid = this.uuid
          const { data: res } = await handleLogin(data)
          console.log(res)
          if (res.status !== '2000') {
            this.$message.error(res.message)
          } else {
            this.$message.success(res.message)
            // 保存token
            this.$store.commit('saveToken', res.data.accessToken)
            // 查询登录账户信息
            await this.$store.dispatch('queryLoginAccount')
            if (!this.$route.query.redirect) {
              this.$store.commit('setActiveMenu', 'subject')
            }
            // 则跳转至进入登录页前的路由
            await this.$router.push({
              path: this.$route.query.redirect ? this.$route.query.redirect : 'subject'
            })
          }
        }
      })
    },
    // 获取验证码图片
    async getValidateCodeImage () {
      const { data: res } = await getImageValidateCode()
      if (res.status === '2000') {
        this.validateCodeImage = res.data.imageUrl
        this.uuid = res.data.uuid
      }
    },
    // 刷新图片验证码
    refreshValidateCode () {
      this.getValidateCodeImage()
    }
  },
  created () {
    this.getValidateCodeImage()
  }
}
</script>

<style lang="less" scoped>
.header_title {
  font-size: 30px;
  color: #000;
  margin: 40px auto;
  text-align: center;
  font-weight: 700;
  letter-spacing: 17px;
}
.el-divider {
  background-color: #1be;
  width: 70%;
  height: 4px;
  margin: 40px auto;
}
.login_div {
  padding: 50px 0 0 0;
  margin: 40px auto;
  background-color: #fff;
  width: 450px;
  // 圆角边框
  border-radius: 3px;
}
.login_item {
  // 将登录表单置于底部
  margin: 0 auto;
  width: 450px;
}

.validate_input {
  width: 67.5%;
  margin-right: 20px;
  float: left;
}

.validate_image_box {
  float: left;
  width: 20%;

  img {
    border-radius: 5%;
  }
}
.el-button{
  width: 100%;
}
</style>
