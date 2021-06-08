import Vue from 'vue'
import Vuex from 'vuex'
import { handleRouterQuery } from '@/apis/info/power'
import { handleLoginAccountQuery } from '@/apis/info/auth'
import { filterAsyncRouter } from '@/utils/routerDecorate'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    routers: []
  },
  getters: {
    routers: state => state.routers
  },
  mutations: {
    setRouters (state, routers) {
      localStorage.setItem('routers', JSON.stringify(routers))
      state.routers = routers
    },
    cleanStore (state) {
      state.routers = []
    }
  },
  actions: {
    queryLoginAccount ({ commit }) {
      return new Promise((resolve, reject) => {
        handleLoginAccountQuery().then(res => {
          console.log(res)
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
          console.log('handleRouterQuery#result', JSON.stringify(result))
          const routers = filterAsyncRouter(result.data)
          commit('setRouters', routers)
          console.log('handleRouterQuery#routers', JSON.stringify(routers))
          resolve(routers)
        }).catch(error => {
          reject(error)
        })
      })
    },
    handleStoreClean (context) {
      context.commit('cleanStore')
    }
  }
})
