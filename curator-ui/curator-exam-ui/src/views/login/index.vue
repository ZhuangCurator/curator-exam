<template>
  <div class="login_container">
    <title-header></title-header>
    <div class="login_div">
      <el-form class="login_item" ref="loginFormRef" :model="loginForm" :rules="loginFormRules">
        <!-- 账户名 -->
        <el-form-item prop="accountName">
          <el-input v-model="loginForm.accountName" placeholder="请输入姓名" prefix-icon="el-icon-user"></el-input>
        </el-form-item>
        <!-- 密码 -->
        <el-form-item prop="admissionNumber">
          <el-input v-model="loginForm.admissionNumber" placeholder="请输入准考证号" prefix-icon="el-icon-lock"></el-input>
        </el-form-item>
        <!-- 图片验证码 -->
        <el-form-item prop="captcha">
          <el-input v-model="loginForm.captcha" prefix-icon="el-icon-picture-outline" class="validate_input"
                    placeholder="请输入图片验证码"></el-input>
          <div class="validate_image_box" @click="refreshValidateCode">
            <img :src="validateCodeImage" alt="">
          </div>
        </el-form-item>
        <!-- 按钮区域  -->
        <el-form-item class="btn_item">
          <el-button type="primary" @click="checkCaptcha">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import TitleHeader from '@/components/TitleHeader'
import { getImageValidateCode, handleCheckCaptcha } from '@/apis/auth'
import { handlePaperLogin } from '@/apis/paper'

export default {
  name: 'Login',
  components: { TitleHeader },
  data () {
    return {
      // 登录表单数据绑定对象
      loginForm: {
        accountName: undefined,
        admissionNumber: undefined,
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
        admissionNumber: [
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
    // 检验验证码
    async checkCaptcha () {
      this.refreshValidateCode()
      const data = { uuid: this.uuid, captcha: this.loginForm.captcha }
      const { data: res } = await handleCheckCaptcha(data)
      console.log(res)
      if (res.status !== '2000') {
        this.$message.error(res.message)
      } else {
        // 验证码验证成功,调用 登录方法
        this.login()
      }
    },
    // 点击登录按钮，发起登录请求
    login () {
      // 首先进行表单的预验证
      this.$refs.loginFormRef.validate(async valid => {
        if (valid) {
          const data = this.loginForm
          const { data: res } = await handlePaperLogin(data)
          console.log(res)
          if (res.status !== '2000') {
            this.$message.error(res.message)
          } else {
            // this.$message.success(res.message)
            // 保存登录信息
            await this.$store.dispatch('setLoginInfo', res.data)
            // 则跳转至考试须知页
            await this.$router.push({
              path: 'notice'
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
