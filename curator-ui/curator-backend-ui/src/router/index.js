import Vue from 'vue'
import VueRouter from 'vue-router'

import store from '@/store'
import routes from './router'

Vue.use(VueRouter)

const router = new VueRouter({
  routes
})

// 挂载路由导航守卫
router.beforeEach((to, from, next) => {
  console.log('store: ' + store.state.routers)
  // to: 代表将要访问的路径 from: 代表从哪个路径而来  next: 是一个函数，表示放行  next() 或next('/login')
  if (to.path === '/login') return next()
  // 如果没有token或者token为空 则跳转登录页
  if (!store.state.token) {
    console.log('to path: ' + to.path + ', token: ' + store.state.token)
    // 尝试从sessionsStorage中恢复数据
    console.log('sessionStorage: ' + sessionStorage.getItem('store'))
    store.replaceState(Object.assign({}, store.state, JSON.parse(sessionStorage.getItem('store'))))
    console.log('store: ' + store.state.token)
    console.log('store: ' + store.state.routers)
    if (!store.state.token) {
      return next('/login')
    }
  } else {
    if (store.getters.routers.length === 0) {
      // 首先查询登录帐号
      store.dispatch('queryLoginAccount').then(loginAccount => {
        // 设置是不是超级管理员
        loginAccount.superAdmin = loginAccount.roleType === 1 ? 1 : 0
        store.commit('saveAccount', loginAccount)
        // 获取动态路由
        store.dispatch('queryRouter').then(accessRoutes => {
          // 根据roles权限生成可访问的路由表
          router.addRoutes(accessRoutes) // 动态添加可访问路由表
          next()
        })
      })
    } else {
      next()
    }
  }
  // token存在  则正常跳转
  next()
})

const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => err)
}

export default router
