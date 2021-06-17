import Vue from 'vue'
import VueRouter from 'vue-router'

import store from '@/store'
import routes from './router'
import { getToken } from '@/utils/storage'

Vue.use(VueRouter)

const router = new VueRouter({
  routes
})

// 挂载路由导航守卫
router.beforeEach((to, from, next) => {
  if (to.path === '/login') return next()
  // 如果没有token或者token为空 则跳转登录页
  if (!getToken()) {
    return next('/login')
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
