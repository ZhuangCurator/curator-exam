<template>
  <div class="paper_container">
    <title-header></title-header>
    <div class="paper_div">
      <div class="time_div">
        距离考试结束还有：<span style="color: #ff0000;">{{ minutes }}分{{ seconds }}秒</span>
        <el-button style="float: right; margin-top: -10px" type="primary" icon="el-icon-plus" >我要交卷</el-button>
      </div>
      <div class="paper_detail_div">
        <div class="question_type_div" >
          <p class="title_desc">答题卡</p>
          <el-row :gutter="24" class="card-line" style="padding-left: 10px">
            <el-tag type="info" effect="dark">未作答</el-tag>
            <el-tag type="success" effect="dark">已作答</el-tag>
            <el-tag type="" effect="dark">当前题目</el-tag>
          </el-row>
          <div v-for="(item, index) in this.questionTypeList" :key="index">
            <p class="card-title">{{ item.questionTypeDesc }}</p>
            <el-row :gutter="24" class="card-line">
              <el-tag v-for="index of item.questionNumber" :key="index" type="info">{{ ++atomicSort }}</el-tag>
            </el-row>
          </div>
        </div>
        <div class="question_content_div">
          <h2>考试界面</h2>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import TitleHeader from '@/components/TitleHeader'
import { handleQuestionTypeAndNumQuery } from '@/apis/paper'
export default {
  name: 'paper',
  components: { TitleHeader },
  data () {
    return {
      minutes: '00',
      seconds: '00',
      // 试题自增序号
      atomicSort: 0,
      // 试题类型集合
      questionTypeList: []
    }
  },
  methods: {
    // 考试结束倒计时
    countdown () {
      // 剩余毫秒
      const milliSecond = this.$store.state.examDuration
      // 强制交卷
      if (milliSecond < 0) {
        console.log('强制交卷了')
        return
      }
      const min = parseInt((milliSecond / 1000) / 60 + '')
      const sec = (milliSecond / 1000) % 60
      // 赋值
      this.minutes = min > 9 ? min : '0' + min
      this.seconds = sec > 9 ? sec : '0' + sec
      const that = this
      // 时间自减一秒
      this.$store.commit('subExamDuration')
      setTimeout(function () {
        that.countdown()
      }, 1000)
    },
    // 试卷试题类型和个数
    async queryQuestionTypeAndNum () {
      const { data: res } = await handleQuestionTypeAndNumQuery(this.$store.state.generationRuleId)
      if (res.status !== '2000') {
        this.$message.error(res.message)
      } else {
        this.questionTypeList = res.data
      }
    }
  },
  mounted () {
    this.countdown()
    this.queryQuestionTypeAndNum()
  }
}
</script>

<style lang="less" scoped>
.paper_div {
  width: 70%;
  margin: 0 auto;
}
.paper_detail_div .question_type_div {
  float: left;
  width: 300px;
  margin-right: 20px;
}
.paper_detail_div .question_detail_div {
  float: left;
}
.title_desc{
  background: #eee;
  line-height: 35px;
  text-align: center;
  font-size: 14px;
}
.el-tag {
  margin-right: 3px;
}
</style>
