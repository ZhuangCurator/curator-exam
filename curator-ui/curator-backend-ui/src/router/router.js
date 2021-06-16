import Login from '../views/login/index'
import Home from '../views/home'
import Welcome from '@/views/welcome'

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
