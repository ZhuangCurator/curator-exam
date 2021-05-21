import Login from '../components/login/index'
import Home from '../components/home'
import Welcome from '@/components/welcome'

// 默认的路由组件
export default [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    component: Login
  },
  {
    path: '/home',
    component: Home,
    redirect: '/welcome',
    children: [
      {
        path: '/welcome',
        component: Welcome
      }
    ]
  }
]
