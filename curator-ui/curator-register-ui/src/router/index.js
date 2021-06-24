import Vue from 'vue'
import VueRouter from 'vue-router'
import { getToken } from '@/utils/storage'

import Home from '../views/Home.vue'
import Notice from '@/views/notice'
import NoticeContent from '@/views/notice/content'
import Subject from '@/views/subject'
import Category from '@/views/category'
import Site from '@/views/site'
import Score from '@/views/score'
import Login from '@/views/login'

Vue.use(VueRouter)

const routes = [
  {
    path: '/home',
    name: 'Home',
    component: Home
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
          title: '公告页'
        }
      }
    ]
  },
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
    redirect: '/noticeContent',
    children: [
      {
        path: '/noticeContent',
        component: NoticeContent,
        meta: {
          title: '公告内容页'
        }
      }
    ]
  },
  {
    path: '/',
    component: Home,
    redirect: '/category',
    children: [
      {
        path: '/category',
        component: Category,
        meta: {
          title: '考试类别页'
        }
      }
    ]
  },
  {
    path: '/',
    component: Home,
    redirect: '/subject',
    children: [
      {
        path: '/subject',
        component: Subject,
        meta: {
          title: '考试科目页'
        }
      }
    ]
  },
  {
    path: '/',
    component: Home,
    redirect: '/site',
    children: [
      {
        path: '/site',
        component: Site,
        meta: {
          title: '考点页'
        }
      }
    ]
  },
  {
    path: '/',
    component: Home,
    redirect: '/score',
    children: [
      {
        path: '/score',
        component: Score,
        meta: {
          title: '报名信息页'
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
  if (to.path === '/notice' || to.path === '/login') return next()
  if (!getToken()) {
    next({
      path: '/login',
      query: {
        redirect: to.fullPath
      }
    })
  }
  next()
})

const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => err)
}

export default router
