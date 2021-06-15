<template>
  <div class="login_container">
      <el-card class="login_box">
      <el-tabs v-model="activeName">
        <el-tab-pane name="loginForm">
          <span slot="label"><i class="icon-slot el-icon-user"></i>用户登录</span>
          <div class="login_div">
            <el-form class="login_item" ref="loginFormRef" :model="loginForm" :rules="loginFormRules">
              <!-- 账户名 -->
              <el-form-item prop="accountName">
                <el-input v-model="loginForm.accountName" placeholder="请输入账户名" prefix-icon="iconfont icon-user"></el-input>
              </el-form-item>
              <!-- 密码 -->
              <el-form-item prop="accountPassword">
                <el-input v-model="loginForm.accountPassword" placeholder="请输入账户密码" type="password" prefix-icon="iconfont icon-mima"></el-input>
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
        </el-tab-pane>
        <el-tab-pane name="registerForm">
          <span slot="label"><i class="icon-slot el-icon-collection"></i>用户注册</span>
          用户注册
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import { getImageValidateCode, handleLogin } from '@/apis/auth/auth'

export default {
  name: 'Login',
  data () {
    return {
      activeName: 'loginForm',
      // 登录表单数据绑定对象
      loginForm: {
        accountName: 'SUPER_ADMIN',
        accountPassword: '123456',
        captcha: ''
      },
      // 登录表单验证规则对象
      loginFormRules: {
        accountName: [
          {
            required: true,
            message: '请输入账户名称',
            trigger: 'blur'
          }
        ],
        accountPassword: [
          {
            required: true,
            message: '请输入账户密码',
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
            // setToken(res.data.accessToken)
            // setAccountName(res.data.userName)
            // setAvatar(res.data.avatar)
            // setPermissions(JSON.stringify(res.data.permissions))
            // await this.$router.push('site')
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

.el-tabs {
  width: 460px;
  margin: 0 auto;
}

.login_div {
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

<style>

.login_box .el-card__body {
  text-align: center;
}
.login_box .el-tabs__nav{
  width: 450px;
}
.login_box .el-tabs__item {
  width: 220px;
  text-align: center;
  font-size: 16px;
  font-weight: 550;
}
.login_box .icon-slot {
 padding-right: 5px;
}
</style>
