import { deleted, get, post, put } from '@/http/axios'

// 获取账户所能访问的路由
export function handleRouterQuery () {
  return get('/examInfoBackend/infoPower/router')
}

// 获取菜单列表
export function handleMenuList (params) {
  return get('/system/sysMenu/list', params)
}

// 获取菜单数据
export function handleMenuQuery (params) {
  return get('/system/sysMenu/' + params)
}

// 更新菜单数据
export function handleUpdateMenu (params) {
  return put('/system/sysMenu', params)
}

// 更新菜单数
export function handleUpdateMenuStatus (params) {
  return put('/system/sysMenu/status', params)
}

// 添加菜单数据
export function handleAddMenu (params) {
  return post('/system/sysMenu', params)
}

// 删除菜单数据
export function handleDeleteMenu (params) {
  return deleted('/system/sysMenu/' + params)
}
