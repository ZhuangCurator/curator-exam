<template>
  <div class="paper_container">
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>考试试卷管理</el-breadcrumb-item>
      <el-breadcrumb-item>考试试卷列表</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图区域-->
    <el-card class="box-card">
      <div class="paper_div">
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
            <el-radio-group v-model="currentRadioAnswer" @change="handleRadioChange" disabled>
              <el-radio v-for="(item, index) in this.currentQuestion.questionAnswerList" :label="choseOptions[index]" :key="index">{{ item.questionAnswerContent }}</el-radio>
            </el-radio-group>
          </div>
          <div v-if="this.currentQuestion.questionType === 2">
            <el-checkbox-group v-model="currentCheckboxAnswer" @change="handleCheckboxChange" disabled>
              <el-checkbox v-for="(item, index) in this.currentQuestion.questionAnswerList" :label="choseOptions[index]" :key="index">{{ item.questionAnswerContent }}</el-checkbox>
            </el-checkbox-group>
          </div>
          <div v-if="this.currentQuestion.questionType === 4">
            <el-input v-for="(item, index) in this.currentQuestion.questionAnswerList" :key="index"
                      v-model="currentFillBlankAnswer[index]" style="margin-top: 10px" @change="handleInputChange" disabled>
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
    </el-card>
  </div>
</template>

<script>
import {
  handleQuestionTypeAndNumQuery,
  handleSingleQuestionQuery,
  handleUserAnswerSave,
  handlePaperMark
} from '@/apis/paper/paper'
export default {
  name: 'paper',
  data () {
    return {
      testPaperId: undefined,
      examRegisterInfoId: undefined,
      generationRuleId: undefined,
      // 倒计时默认分钟
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
      const param = { testPaperId: this.testPaperId, generationRuleId: this.generationRuleId }
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
        testPaperId: this.testPaperId,
        examRegisterInfoId: this.examRegisterInfoId,
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
  mounted () {
    this.testPaperId = this.$route.query.p
    this.examRegisterInfoId = this.$route.query.e
    this.generationRuleId = this.$route.query.g
    // 查询左侧的试题类型和序号
    this.queryQuestionTypeAndNum()
    // 默认查找第一题
    this.queryPaperSingleQuestion()
  }
}
</script>

<style lang="less" scoped>
.paper_container {
  height: 100%;
  width: 100%;
}
.box-card {
  height: 100%;
  align-items: center;
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
  width: 100%;
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
