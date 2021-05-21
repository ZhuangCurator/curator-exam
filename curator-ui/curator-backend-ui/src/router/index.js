import Vue from 'vue'
import VueRouter from 'vue-router'

import { getToken } from '@/utils/storage'
import store from '@/store'
import routes from './router'

Vue.use(VueRouter)

const router = new VueRouter({
  routes
})

// 挂载路由导航守卫
router.beforeEach((to, from, next) => {
  // to: 代表将要访问的路径 from: 代表从哪个路径而来  next: 是一个函数，表示放行  next() 或next('/login')
  if (to.path === '/login') return next()
  // 如果没有token或者token为空 则跳转登录页
  if (!getToken()) {
    return next('/login')
  } else {
    if (store.getters.routers.length === 0) {
      // 路由为空,获取动态菜单
      store.dispatch('queryMenuTree').then(accessRoutes => {
        // 根据roles权限生成可访问的路由表
        console.log('accessRoutes: ' + JSON.stringify(accessRoutes))
        router.addRoutes(accessRoutes) // 动态添加可访问路由表
        next()
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
