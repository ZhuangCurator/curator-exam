<template>
  <div class="notice_container">
    <div class="notice_div">
      <h2>考试须知</h2>
    </div>
  </div>
</template>

<script>
import { getImageValidateCode, handleLogin } from '@/apis/auth/auth'

export default {
  name: 'Notice',
  data () {
    return {
      // 登录表单数据绑定对象
      noticeForm: {
        accountName: undefined,
        accountPassword: undefined,
        captcha: undefined
      },
      // 登录表单验证规则对象
      noticeFormRules: {
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
    notice () {
      // 首先进行表单的预验证
      this.$refs.noticeFormRef.validate(async valid => {
        if (valid) {
          const data = this.noticeForm
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

</style>
