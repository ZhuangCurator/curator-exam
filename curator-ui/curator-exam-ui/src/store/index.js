import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    // 账户ID
    accountId: '',
    // 账户名称
    accountName: '',
    // 报名信息ID
    examRegisterInfoId: '',
    // 组卷规则ID
    generationRuleId: '',
    // 试卷ID
    testPaperId: '',
    // 考试时长
    examDuration: 0
  },
  mutations: {
    saveLoginInfo (state, account) {
      state.accountId = account.accountId
      state.accountName = account.accountName
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
      state.accountId = ''
      state.accountName = ''
      state.examRegisterInfoId = ''
      state.generationRuleId = ''
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
