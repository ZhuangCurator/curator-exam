import { deleted, get, post, put } from '@/http/axios'

// 获取账户所能访问的路由
export function handleRouterQuery () {
  return get('/examInfoBackend/infoPower/router')
}

// 权限列表查询
export function handleTreePower (params) {
  return get('/examInfoBackend/infoPower/tree', params)
}

// 个人权限查询
export function handlePowerList () {
  return get('/examInfoBackend/infoPower/list')
}

// 获取单个权限数据
export function handlePowerQuery (params) {
  return get('/examInfoBackend/infoPower/' + params)
}

// 更新菜单数据
export function handleUpdatePower (params) {
  return put('/examInfoBackend/infoPower', params)
}

// 更新菜单状态
export function handleUpdatePowerStatus (params) {
  return put('/examInfoBackend/infoPower/powerStatus', params)
}

// 添加菜单数据
export function handleAddPower (params) {
  return post('/examInfoBackend/infoPower', params)
}

// 删除菜单数据
export function handleDeletePower (params) {
  return deleted('/examInfoBackend/infoPower/' + params)
}
