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
    // 省
    province: '',
    // 市
    city: '',
    // 区县
    district: '',
    // 当前菜单
    activeMenu: 'notice'
  },
  mutations: {
    saveAccount (state, account) {
      state.accountId = account.accountId
      state.accountName = account.accountName
      state.province = account.province
      state.city = account.city
      state.district = account.district
    },
    resetState (state) {
      state.accountId = ''
      state.accountName = ''
      state.province = ''
      state.city = ''
      state.district = ''
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
