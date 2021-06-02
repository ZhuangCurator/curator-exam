import { get, put, post, deleted } from '@/http/axios'

// 获取权限组分页数据
export function handlePowerGroupPage (params) {
  return get('/examInfoBackend/infoPowerGroup/page', params)
}
// 获取权限组列表
export function handlePowerGroupList (params) {
  return get('/examInfoBackend/infoPowerGroup/list', params)
}
// 根据ID获取权限组
export function handlePowerGroupQuery (params) {
  return get('/examInfoBackend/infoPowerGroup/' + params)
}

// 修改权限组信息
export function handleUpdatePowerGroup (params) {
  return put('/examInfoBackend/infoPowerGroup', params)
}

// 添加权限组信息
export function handleAddPowerGroup (params) {
  return post('/examInfoBackend/infoPowerGroup', params)
}

// 删除权限组
export function handleDeletePowerGroup (params) {
  return deleted('/examInfoBackend/infoPowerGroup/' + params)
}

// 权限组绑定权限信息
export function handleAddPowerToPowerGroup (params) {
  return post('/examInfoBackend/infoPowerGroup/add/power', params)
}
