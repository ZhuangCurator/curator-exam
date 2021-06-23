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
                <el-input v-model="loginForm.accountName" placeholder="请输入姓名" prefix-icon="iconfont icon-user"></el-input>
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
          <div class="register_div">
            <!-- 对话框主题区域 -->
            <el-form ref="addFormRef" :model="addForm" :rules="addFormRules" label-width="80px">
              <el-form-item label="姓名" prop="accountName">
                <el-input v-model="addForm.accountName"></el-input>
              </el-form-item>
              <el-form-item label="身份证号" prop="idCard">
                <el-input v-model="addForm.idCard"></el-input>
              </el-form-item>
              <el-form-item label="省市区" prop="email">
                <el-cascader size="medium" :options="options" v-model="selectedOptions" @change="handleChange"></el-cascader>
              </el-form-item><el-form-item label="邮箱" prop="email">
                <el-input v-model="addForm.email"></el-input>
              </el-form-item>
              <el-form-item label="电话" prop="phone">
                <el-input v-model="addForm.phone"></el-input>
              </el-form-item>
              <!-- 按钮区域  -->
              <el-form-item class="btn_item">
                <el-button type="primary" @click="register">确定</el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import { getImageValidateCode, handleLogin, handleRegister } from '@/apis/auth/auth'
import { regionData } from 'element-china-area-data'
export default {
  name: 'Login',
  data () {
    // 自定义邮箱校验规则
    const checkIdCard = (rule, value, callback) => {
      const regIdCard = /^[1-9]\d{5}(18|19|20|(3\d))\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/
      if (regIdCard.test(value)) {
        return callback()
      }
      callback(new Error('请输入合法的身份证号'))
    }
    // 自定义邮箱校验规则
    const checkEmail = (rule, value, callback) => {
      const regEmail = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/
      if (regEmail.test(value)) {
        return callback()
      }
      callback(new Error('请输入合法的邮箱账号'))
    }

    // 自定义手机号码校验规则
    const checkPhone = (rule, value, callback) => {
      const regEmail = /^1[3-9]\d{9}$/
      if (regEmail.test(value)) {
        return callback()
      }
      callback(new Error('请输入合法的手机号'))
    }
    return {
      activeName: 'loginForm',
      options: regionData,
      selectedOptions: [],
      // 登录表单数据绑定对象
      loginForm: {
        accountName: '',
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
      uuid: '',
      // 添加账户表单数据
      addForm: {
        accountName: undefined,
        email: undefined,
        phone: undefined,
        idCard: undefined
      },
      // 添加账户表单校验规则
      addFormRules: {
        accountName: [
          {
            required: true,
            message: '请输入姓名',
            trigger: 'blur'
          }
        ],
        email: [
          {
            required: true,
            message: '请输入邮箱帐号',
            trigger: 'blur'
          },
          {
            validator: checkEmail,
            trigger: 'blur'
          }
        ],
        phone: [
          {
            required: true,
            message: '请输入联系电话',
            trigger: 'blur'
          },
          {
            validator: checkPhone,
            trigger: 'blur'
          }
        ],
        idCard: [
          {
            required: true,
            message: '请输入身份证号',
            trigger: 'blur'
          },
          {
            validator: checkIdCard,
            trigger: 'blur'
          }
        ]
      }
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
    },
    handleChange (value) {
      console.log(value)
      console.log(this.selectedOptions)
    },
    // 点击确定按钮，发起注册请求
    register () {
      // 首先进行表单的预验证
      this.$refs.addFormRef.validate(async valid => {
        if (valid) {
          const { data: res } = await handleRegister(this.addForm)
          if (res.status !== '2000') {
            this.$message.error(res.message)
          } else {
            // 注册成功 跳转登录标签
            this.$message.success(res.message)
            this.activeName = 'loginForm'
          }
        }
      })
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
