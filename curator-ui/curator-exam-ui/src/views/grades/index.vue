<template>
  <div class="grades_container">
    <title-header></title-header>
    <div class="grades_div">
      <h2>考生姓名: {{ this.$store.state.accountName }}</h2>
      <h2>准考证号: {{ this.$store.state.admissionNumber }}</h2>
      <h2>考试成绩: <span style="color: red;">{{ this.$store.state.grades }}</span></h2>
    </div>
  </div>
</template>

<script>
import TitleHeader from '@/components/TitleHeader'
export default {
  name: 'Grades',
  data () {
    return {
      // 倒计时秒
      seconds: 30
    }
  },
  components: { TitleHeader },
  methods: {
    // 20秒 倒计时
    countdown () {
      this.timer = setInterval(() => {
        this.seconds--
        if (this.seconds === 0) {
          // this.$store.commit('resetState')
          clearInterval(this.timer)
          // 则跳转至登录页
          this.$router.push({
            path: 'login'
          })
          window.location.reload()
        }
      }, 1000)
    }
  },
  mounted () {
    // 开始倒计时
    this.countdown()
  }
}
</script>

<style lang="less" scoped>
.grades_container, .grades_div {
  height: 100%;
}
.grades_div {
  width: 600px;
  margin: 0 auto;
  text-align: left;
}
</style>
