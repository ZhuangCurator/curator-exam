<!-- 手机验证码登录表单 -->
<template>
  <!-- 手机验证码登录表单 -->
  <el-form  ref="loginMobileFormRef" :model="loginMobileForm" :rules="loginMobileFormRules">
    <!-- 手机号 -->
    <el-form-item prop="mobilePhone">
      <el-input placeholder="请输入手机号" v-model="loginMobileForm.mobilePhone" prefix-icon="iconfont icon-mima"></el-input>
    </el-form-item>
    <!-- 图片验证码 -->
    <el-form-item prop="smsCode">
      <el-input placeholder="请输入短信验证码" v-model="loginMobileForm.smsCode" class="input-with-select">
        <el-button slot="append" @click="getSmsCode">获取验证码</el-button>
      </el-input>
    </el-form-item>
    <!-- 按钮区域  -->
    <el-form-item class="btn_item">
      <el-button type="primary" @click="mobileLogin">登录</el-button>
      <el-button type="info" @click="resetLoginMobileForm">重置</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { getSmsValidateCode, handleMobileLogin } from '@/apis/auth'
import { setAvatar, setPermissions, setToken, setUsername } from '@/utils/storage'

export default {
  name: 'loginMobile',
  data () {
    return {
      // 登录表单数据绑定对象
      loginMobileForm: {
        mobilePhone: undefined,
        smsCode: undefined
      },
      // 登录表单验证规则对象
      loginMobileFormRules: {
        mobilePhone: [
          {
            required: true,
            message: '请输入手机号',
            trigger: 'blur'
          }
        ],
        smsCode: [
          {
            required: true,
            message: '请输入短信验证码',
            trigger: 'blur'
          }
        ]
      },
      uuid: ''
    }
  },
  methods: {
    // 点击重置按钮，登录表单重置方法
    resetLoginMobileForm () {
      this.$refs.loginMobileFormRef.resetFields()
    },
    // 点击登录按钮，发起登录请求
    mobileLogin () {
      // 首先进行表单的预验证
      this.$refs.loginMobileFormRef.validate(async valid => {
        if (valid) {
          const data = this.loginMobileForm
          data.uuid = this.uuid
          const { data: res } = await handleMobileLogin(data)
          console.log(res)
          if (res.status !== '2000') {
            this.$message.error(res.message)
          } else {
            this.$message.success(res.message)
            setToken(res.data.token)
            setUsername(res.data.userName)
            setAvatar(res.data.avatar)
            setPermissions(JSON.stringify(res.data.permissions))
            await this.$router.push('/home')
          }
        }
      })
    },
    // 获取短信验证码
    async getSmsCode () {
      if (!this.loginMobileForm.mobilePhone) return this.$message.error('请先输入手机号')
      const { data: res } = await getSmsValidateCode(this.loginMobileForm.mobilePhone)
      console.log(res)
      if (res.status === '2000') {
        this.uuid = res.data.uuid
      } else {
        this.$message.error(res.message)
      }
    }
  }
}
</script>

<style lang="less" scoped>
.btn_item {
  // 弹性布局
  display: flex;
  // 设置弹性盒子元素在主轴（横轴）方向上的对齐方式, flex-end: 位于容器的结尾
  justify-content: flex-end;
}
</style>
