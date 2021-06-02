import { get, put, post, deleted } from '@/http/axios'

// 获取角色分页数据
export function handleRolePage (params) {
  return get('/examInfoBackend/infoRole/page', params)
}
// 获取角色列表
export function handleRoleList (params) {
  return get('/examInfoBackend/infoRole/list', params)
}
// 根据ID获取角色
export function handleRoleQuery (params) {
  return get('/examInfoBackend/infoRole/' + params)
}

// 修改角色信息
export function handleUpdateRole (params) {
  return put('/examInfoBackend/infoRole', params)
}

// 添加角色信息
export function handleAddRole (params) {
  return post('/examInfoBackend/infoRole', params)
}

// 删除角色
export function handleDeleteRole (params) {
  return deleted('/examInfoBackend/infoRole/' + params)
}

// 角色绑定权限信息
export function handleBindMenuWithRole (params) {
  return post('/system/sysRole/bind/power', params)
}

// 获取角色类型列表
export function handleRoleTypeList () {
  return get('/examInfoBackend/infoRole/roleType/list')
}
