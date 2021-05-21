import Vue from 'vue'
import Vuex from 'vuex'
import { handleMenuListByUser } from '@/apis/menu'
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
    queryMenuTree ({ commit }) {
      return new Promise((resolve, reject) => {
        handleMenuListByUser().then(res => {
          const result = res.data
          console.log('queryMenuTree#result', JSON.stringify(result))
          const routers = filterAsyncRouter(result.data)
          commit('setRouters', routers)
          console.log('queryMenuTree#routers', JSON.stringify(routers))
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
