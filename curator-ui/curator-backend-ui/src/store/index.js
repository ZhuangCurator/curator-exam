import Vue from 'vue'
import Vuex from 'vuex'
import { handleRouterQuery } from '@/apis/info/power'
import { handleLoginAccountQuery } from '@/apis/info/auth'
import { filterAsyncRouter } from '@/utils/routerDecorate'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    routers: [],
    userName: '',
    permissions: [],
    superAdmin: 0
  },
  getters: {
    routers: state => state.routers,
    permissions: state => state.permissions
  },
  mutations: {
    setRouters (state, routers) {
      state.routers = routers
    },
    saveAccount (state, account) {
      state.userName = account.accountName
      state.permissions = account.perms
      state.superAdmin = account.superAdmin
    },
    cleanStore (state) {
      state.routers = []
      state.accountName = ''
      state.permissions = []
      state.superAdmin = 0
    }
  },
  actions: {
    queryLoginAccount ({ commit }) {
      return new Promise((resolve, reject) => {
        handleLoginAccountQuery().then(res => {
          const result = res.data
          resolve(result.data)
        }).catch(error => {
          reject(error)
        })
      })
    },
    queryRouter ({ commit }) {
      return new Promise((resolve, reject) => {
        handleRouterQuery().then(res => {
          const result = res.data
          const routers = filterAsyncRouter(result.data)
          commit('setRouters', routers)
          resolve(routers)
        }).catch(error => {
          reject(error)
        })
      })
    },
    handleSaveAccount ({ commit }, account) {
      commit('saveAccount', account)
    },
    handleStoreClean ({ commit }) {
      commit('cleanStore')
    }
  }
})
