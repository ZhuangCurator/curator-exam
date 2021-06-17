import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
// import Welcome from '@/components/welcome'
import Notice from '@/views/notice'
import NoticeContent from '@/views/notice/content'
import Subject from '@/views/subject'
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
  // if (to.meta.requireAuth) {
  //   let token = Cookies.get('access_token');
  //   let anonymous = Cookies.get('user_name');
  //   if (token) {
  //
  //     next({
  //       path: '/login',
  //       query: {
  //         redirect: to.fullPath
  //       }
  //     })
  //
  //   }
  // }
  next()
})

export default router
