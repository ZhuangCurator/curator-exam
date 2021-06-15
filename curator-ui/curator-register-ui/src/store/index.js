import Vue from 'vue'
import Vuex from 'vuex'
import { handleLoginAccountQuery } from '@/apis/auth/auth'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    token: '',
    // 账户ID
    accountId: '',
    // 账户名称
    accountName: ''
  },
  mutations: {
    saveAccount (state, account) {
      state.token = account.token
      state.accountId = account.accountId
      state.accountName = account.accountName
    }
  },
  actions: {
    // 查询登录账户
    queryLoginAccount ({ commit }) {
      return new Promise((resolve, reject) => {
        handleLoginAccountQuery().then(res => {
          console.log(res)
          const result = res.data
          commit('saveAccount', result)
          resolve(result.data)
        }).catch(error => {
          reject(error)
        })
      })
    }
  },
  modules: {
  }
})
