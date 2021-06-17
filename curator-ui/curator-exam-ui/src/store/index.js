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
    accountName: '',
    // 当前菜单
    activeMenu: 'notice'
  },
  mutations: {
    saveAccount (state, account) {
      state.accountId = account.accountId
      state.accountName = account.accountName
    },
    saveToken (state, token) {
      state.token = token
    },
    resetState (state) {
      state.token = ''
      state.accountId = ''
      state.accountName = ''
    },
    setActiveMenu (state, activeMenu) {
      state.activeMenu = activeMenu
    },
    getActiveMenu (state) {
      return state.activeMenu
    }
  },
  actions: {
    // 查询登录账户
    queryLoginAccount ({ commit }) {
      return new Promise((resolve, reject) => {
        handleLoginAccountQuery().then(res => {
          console.log(res)
          const result = res.data
          commit('saveAccount', result.data)
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
