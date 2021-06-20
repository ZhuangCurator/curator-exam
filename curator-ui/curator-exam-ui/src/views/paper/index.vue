<template>
  <div class="paper_container">
    <title-header :dividerWidth="80"></title-header>
    <div class="paper_div">
      <div class="time_div">
        距离考试结束还有：<span style="color: #ff0000;">{{ minutes }}分{{ seconds }}秒</span>
        <el-button style="float: right; margin-top: -10px" type="primary" icon="el-icon-plus" @click="handIn()">我要交卷</el-button>
      </div>
      <div class="paper_detail_div">
        <div class="question_type_div" >
          <p class="title_desc">答题卡</p>
          <el-row :gutter="24" class="row-line" >
            <el-tag type="info" >未作答</el-tag>
            <el-tag type="success" >已作答</el-tag>
            <el-tag type="" >当前题目</el-tag>
          </el-row>
          <div v-for="(item, index) in this.paperQuestionTypeList" :key="index">
            <p class="title_desc">{{ item.questionTypeDesc }}</p>
            <el-row :gutter="24" class="row-line">
              <el-tag  v-for="item in item.paperQuestionList" :key="item.questionSort" :type="handleTagType(item)" class="tag_num" @click="handleTagClick(item.questionSort)">
                {{ item.questionSort &lt; 10 ? '0' + item.questionSort : item.questionSort }}
              </el-tag>
            </el-row>
          </div>
        </div>
        <div class="question_content_div">
          <p class="title_desc">试题内容</p>
          <!--  题干 -->
          <p v-html="this.currentTagIndex + '. ' + this.currentQuestion.questionStem" style="font-size: 20px"></p>
          <div v-if="this.currentQuestion.questionType === 1  || this.currentQuestion.questionType === 3">
            <el-radio-group v-model="currentRadioAnswer" @change="handleRadioChange">
              <el-radio v-for="(item, index) in this.currentQuestion.questionAnswerList" :label="choseOptions[index]" :key="index">{{ item.questionAnswerContent }}</el-radio>
            </el-radio-group>
          </div>
          <div v-if="this.currentQuestion.questionType === 2">
            <el-checkbox-group v-model="currentCheckboxAnswer" @change="handleCheckboxChange">
              <el-checkbox v-for="(item, index) in this.currentQuestion.questionAnswerList" :label="choseOptions[index]" :key="index">{{ item.questionAnswerContent }}</el-checkbox>
            </el-checkbox-group>
          </div>
          <div v-if="this.currentQuestion.questionType === 4">
<!--            <el-input v-for="(item, index) in this.currentQuestion.questionAnswerList" :key="index" v-model="currentFillBlankAnswer[index].questionAnswerContent" style="margin-top: 10px">-->
<!--              <template slot="prepend">答案 {{ index + 1}} :</template>-->
<!--            </el-input>-->
            <el-input v-for="(item, index) in this.currentQuestion.questionAnswerList" :key="index"
                      v-model="currentFillBlankAnswer[index]" style="margin-top: 10px" @change="handleInputChange">
              <template slot="prepend">答案 {{ index + 1}} :</template>
            </el-input>
          </div>
          <div style="margin-top: 40px">
            <el-button v-if="showPrevious" type="primary"  size="small" icon="el-icon-back" @click="showPreviousQuestion()">上一题</el-button>
            <el-button v-if="showNext" type="warning"  size="small" icon="el-icon-right" @click="showNextQuestion()">下一题</el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import TitleHeader from '@/components/TitleHeader'
import {
  handleQuestionTypeAndNumQuery,
  handleSingleQuestionQuery,
  handleUserAnswerSave,
  handlePaperMark
} from '@/apis/paper'
export default {
  name: 'paper',
  components: { TitleHeader },
  data () {
    return {
      // 倒计时默认分钟
      minutes: '00',
      // 倒计时默认秒数
      seconds: '00',
      // 是否展示上一题
      showPrevious: true,
      // 是否展示下一题
      showNext: true,
      // 选择题或判断答案内容
      choseOptions: ['A', 'B', 'C', 'D', 'E'],
      // 已作试题序号集合
      handledQuestionArray: new Set(),
      // 试卷试题类型集合
      paperQuestionTypeList: [],
      // 试题最大序号(用于判断下一题按钮的出现与否)
      maxTagIndex: undefined,
      // 当前点击的tag序号
      currentTagIndex: 1,
      // 当前试题
      currentQuestion: '',
      // 当前单选判断题答案
      currentRadioAnswer: '',
      // 当前多选题答案
      currentCheckboxAnswer: [],
      // 当前填空题答案
      currentFillBlankAnswer: [],
      currentFillBlankAnswerLength: 0
    }
  },
  methods: {
    // 试卷试题类型和个数
    async queryQuestionTypeAndNum () {
      const param = { testPaperId: this.$store.state.testPaperId, generationRuleId: this.$store.state.generationRuleId }
      const { data: res } = await handleQuestionTypeAndNumQuery(param)
      if (res.status !== '2000') {
        this.$message.error(res.message)
      } else {
        console.log(res.data)
        this.paperQuestionTypeList = res.data
        const lastQuestionType = res.data[res.data.length - 1]
        const lastQuestion = (lastQuestionType.paperQuestionList)[lastQuestionType.paperQuestionList.length - 1]
        this.maxTagIndex = lastQuestion.questionSort
      }
    },
    // 查询试卷单个试题
    async queryPaperSingleQuestion () {
      this.showPrevious = this.currentTagIndex !== 1
      this.showNext = this.currentTagIndex !== this.maxTagIndex
      const param = {
        testPaperId: this.$store.state.testPaperId,
        examRegisterInfoId: this.$store.state.examRegisterInfoId,
        paperQuestionSort: this.currentTagIndex
      }
      const { data: res } = await handleSingleQuestionQuery(param)
      if (res.status !== '2000') {
        this.$message.error(res.message)
      } else {
        console.log(res.data)
        this.currentQuestion = res.data
        if (this.currentQuestion.questionType === 4) {
          this.currentFillBlankAnswerLength = this.currentQuestion.questionAnswerList.length
        }
        if (this.currentQuestion.userAnswerList !== null) {
          if (this.currentQuestion.questionType === 1 || this.currentQuestion.questionType === 3) {
            // 单选或判断
            this.currentRadioAnswer = this.currentQuestion.userAnswerList[0]
          } else if (this.currentQuestion.questionType === 2) {
            // 多选
            this.currentCheckboxAnswer = this.currentQuestion.userAnswerList
          } else if (this.currentQuestion.questionType === 4) {
            // 填空
            const userAnswer = this.currentQuestion.userAnswerList
            for (let i = 0; i < this.currentFillBlankAnswerLength; i++) {
              if (userAnswer[i] === 'blankEmpty') {
                this.currentFillBlankAnswer[i] = ''
              } else {
                this.currentFillBlankAnswer[i] = userAnswer[i]
              }
            }
          }
        }
      }
    },
    // 保存试题答案
    async saveSingleQuestionAnswer (currentQuestionAnswer) {
      const param = {
        testPaperId: this.$store.state.testPaperId,
        examRegisterInfoId: this.$store.state.examRegisterInfoId,
        paperQuestionSort: this.currentTagIndex,
        userAnswer: currentQuestionAnswer
      }
      const { data: res } = await handleUserAnswerSave(param)
      if (res.status !== '2000') {
        this.$message.error(res.message)
      } else {
        console.log(res)
      }
    },
    // 主动交卷
    handIn () {
      this.$confirm('是否确认交卷?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        // 手动交卷
        await this.markPaper(1)
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消交卷!'
        })
      })
    },
    // 交卷
    async markPaper (handInReason) {
      const param = {
        testPaperId: this.$store.state.testPaperId,
        examRegisterInfoId: this.$store.state.examRegisterInfoId,
        handInReason: handInReason
      }
      const { data: res } = await handlePaperMark(param)
      if (res.status !== '2000') {
        this.$message.error(res.message)
      } else {
        // 保存成绩
        this.$store.commit('setGrades', res.data)
        // 则跳转至考试成绩页
        await this.$router.push({
          path: 'grades'
        })
      }
    },
    // 动态生成试题序号标签的 type
    handleTagType (item) {
      if (item.questionSort === this.currentTagIndex) {
        return ''
      } else if (item.handled === 1 || this.handledQuestionArray.has(item.questionSort)) {
        return 'success'
      } else {
        return 'info'
      }
    },
    // 处理 试题序号标签点击
    handleTagClick (index) {
      console.log('currentTagIndex: ' + index)
      this.currentTagIndex = index
      // 重置答案
      this.currentRadioAnswer = ''
      this.currentCheckboxAnswer = []
      this.currentFillBlankAnswer = []
      this.currentFillBlankAnswerLength = 0
      // 查询当前序号试题
      this.queryPaperSingleQuestion()
    },
    // 处理单选框选中值
    handleRadioChange (val) {
      console.log('currentTagIndex: ' + this.currentTagIndex + ', val: ' + val)
      // 保存已作试题序号
      this.handledQuestionArray.add(this.currentTagIndex)
      // 保存答案到数据库
      this.saveSingleQuestionAnswer(val)
    },
    // 处理多选框选中值
    handleCheckboxChange (val) {
      console.log('currentTagIndex: ' + this.currentTagIndex + ', val: ' + val)
      if (!val || val.length === 0) {
        this.handledQuestionArray.delete(this.currentTagIndex)
      } else {
        // 保存已作试题序号
        this.handledQuestionArray.add(this.currentTagIndex)
      }
      // 保存答案到数据库
      this.saveSingleQuestionAnswer(val.join('$:$'))
    },
    // 处理输入框输入值
    handleInputChange (val) {
      console.log('val: ' + val)
      if (!val || val.length === 0) {
        this.handledQuestionArray.delete(this.currentTagIndex)
      } else {
        // 保存已作试题序号
        this.handledQuestionArray.add(this.currentTagIndex)
      }
      const answer = []
      for (let i = 0; i < this.currentFillBlankAnswerLength; i++) {
        if (!this.currentFillBlankAnswer[i]) {
          answer[i] = 'blankEmpty'
        } else {
          answer[i] = this.currentFillBlankAnswer[i].trim()
        }
      }
      // 保存答案到数据库
      this.saveSingleQuestionAnswer(answer.join('$:$'))
    },
    // 展示上一题
    showPreviousQuestion () {
      this.handleTagClick(this.currentTagIndex - 1)
    },
    // 展示下一题
    showNextQuestion () {
      this.handleTagClick(this.currentTagIndex + 1)
    }
  },
  created () {
    // 查询左侧的试题类型和序号
    this.queryQuestionTypeAndNum()
    // 默认查找第一题
    this.queryPaperSingleQuestion()
  },
  mounted () {
    // 考试结束倒计时
    const timer = setInterval(() => {
      // 剩余毫秒
      const milliSecond = this.$store.state.examDuration
      if (milliSecond < 0) {
        clearInterval(timer)
        console.log('强制交卷了')
      }
      // 剩余毫秒
      const min = parseInt((milliSecond / 1000) / 60 + '')
      const sec = (milliSecond / 1000) % 60
      // 赋值
      this.minutes = min > 9 ? min : '0' + min
      this.seconds = sec > 9 ? sec : '0' + sec
      // 时间自减一秒
      this.$store.commit('subExamDuration')
    }, 1000)
    // 通过$once来监听定时器，在beforeDestroy钩子可以被清除。
    this.$once('hook:beforeDestroy', () => {
      clearInterval(timer)
    })
  }
}
</script>

<style lang="less" scoped>
.paper_container {
  height: 100%;
  width: 100%;
}
.time_div {
  height: 50px;
}
.paper_detail_div {
  width: 100%;
  height: 100%;
}
.paper_div {
  height: 100%;
  width: 70%;
  margin: 0 auto;
}
.paper_detail_div .question_type_div {
  float: left;
  width: 310px;
  height: 100%;
  margin-right: 20px;
}
.paper_detail_div .question_content_div {
  float: left;
  width: 700px;
  height: 100%;
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
  margin-bottom: 3px;
}
.tag_num {
  padding: 0 6px;
}
.row-line {
  padding-left: 10px;
}
// 单选框 分行排列
/deep/ .el-radio, .el-checkbox{
  display: block;
  line-height: 55px;
  white-space: normal;
  margin-right: 0;
}
//  单选框 文本大小
/deep/ .el-radio__label {
  font-size: 20px;
}

/deep/ .el-checkbox__label {
  font-size: 20px;
}

/deep/ .el-input-group__prepend {
  padding: 0 7px;
}
</style>
