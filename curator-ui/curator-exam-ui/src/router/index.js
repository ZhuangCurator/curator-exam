import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'

import Login from '@/views/login'
import Notice from '@/views/notice'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    component: Home,
    redirect: '/login',
    children: [
      {
        path: '/login',
        component: Login,
        meta: {
          title: '登录页'
        }
      }
    ]
  },
  {
    path: '/',
    component: Home,
    redirect: '/notice',
    children: [
      {
        path: '/notice',
        component: Notice,
        meta: {
          title: '考试须知页'
        }
      }
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) => {
  window.document.title = to.meta.title === undefined ? '默认标题' : to.meta.title
  next()
})

export default router
