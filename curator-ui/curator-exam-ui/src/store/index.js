import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)

// 声明这两个方便直接清空state
const getDefaultState = () => {
  return {
    // 最终的考试成绩
    grades: '',
    // 账户ID
    accountId: '',
    // 账户名称
    accountName: '',
    // 准考证号
    admissionNumber: '',
    // 报名信息ID
    examRegisterInfoId: '',
    // 组卷规则ID
    generationRuleId: '',
    // 试卷ID
    testPaperId: '',
    // 考试时长
    examDuration: 0
  }
}
const state = getDefaultState()

export default new Vuex.Store({
  state: state,
  mutations: {
    setGrades (state, grades) {
      state.grades = grades
    },
    saveLoginInfo (state, account) {
      state.accountId = account.accountId
      state.accountName = account.accountName
      state.admissionNumber = account.admissionNumber
      state.examRegisterInfoId = account.examRegisterInfoId
      state.generationRuleId = account.generationRuleId
    },
    setTestPaperId (state, testPaperId) {
      state.testPaperId = testPaperId
    },
    setExamDuration (state, examDuration) {
      state.examDuration = examDuration
    },
    resetState (state) {
      state = getDefaultState()
    },
    subExamDuration (state) {
      // 剩余时间减去一秒
      state.examDuration -= 1000
    }
  },
  actions: {
    // 保存登录返回信息
    setLoginInfo (context, info) {
      context.commit('saveLoginInfo', info)
    },
    // 保存初始化完成的试卷ID
    saveTestPaperId (context, testPaperId) {
      context.commit('setTestPaperId', testPaperId)
    }
  },
  modules: {
  }
})
