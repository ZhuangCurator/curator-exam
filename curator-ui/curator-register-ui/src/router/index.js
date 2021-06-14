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
        component: Notice
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
        component: Login
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
        component: NoticeContent
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
        component: Subject
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
        component: Site
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
        component: Score
      }
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
