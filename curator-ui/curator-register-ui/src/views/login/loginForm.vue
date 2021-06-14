<!-- 账号密码登录 -->
<template>
  <!-- 登录表单 -->
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
      <el-button type="info" @click="resetLoginForm">重置</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { getImageValidateCode, handleLogin } from '@/apis/info/auth'
import { setAvatar, setPermissions, setToken, setAccountName } from '@/utils/storage'

export default {
  name: 'loginForm',
  data () {
    return {
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
    // 点击重置按钮，登录表单重置方法
    resetLoginForm () {
      this.$refs.loginFormRef.resetFields()
    },
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
            setToken(res.data.accessToken)
            // setAccountName(res.data.userName)
            // setAvatar(res.data.avatar)
            // setPermissions(JSON.stringify(res.data.permissions))
            await this.$router.push('/home')
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
.login_item {
  // 将登录表单置于底部
  //position: absolute;
  //bottom: 0;

  width: 100%;
  padding: 0 20px;
  // element-ui的el-form-item默认是content-box模型,将之设置为border-box
  box-sizing: border-box;
}

.btn_item {
  // 弹性布局
  display: flex;
  // 设置弹性盒子元素在主轴（横轴）方向上的对齐方式, flex-end: 位于容器的结尾
  justify-content: flex-end;
}

.validate_input {
  width: 64%;
  margin-right: 10px;
  float: left;
}

.validate_image_box {
  float: left;
  width: 30%;

  img {
    border-radius: 3%;
  }
}
</style>
