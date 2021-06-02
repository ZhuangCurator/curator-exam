import { deleted, get, post, put } from '@/http/axios'

// 获取账户所能访问的路由
export function handleRouterQuery () {
  return get('/examInfoBackend/infoPower/router')
}

// 获取权限列表
export function handlePowerList (params) {
  return get('/examInfoBackend/infoPower/list', params)
}

// 获取单个权限数据
export function handlePowerQuery (params) {
  return get('/examInfoBackend/infoPower/' + params)
}

// 更新菜单数据
export function handleUpdatePower (params) {
  return put('/examInfoBackend/infoPower', params)
}

// 更新菜单数
export function handleUpdatePowerStatus (params) {
  return put('/examInfoBackend/infoPower', params)
}

// 添加菜单数据
export function handleAddPower (params) {
  return post('/examInfoBackend/infoPower', params)
}

// 删除菜单数据
export function handleDeletePower (params) {
  return deleted('/examInfoBackend/infoPower/' + params)
}
