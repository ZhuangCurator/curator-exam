import { get, put, post, deleted } from '@/http/axios'

// 获取角色列表
export function handleRolePage (params) {
  return get('/system/sysRole/page', params)
}
// 根据ID获取角色
export function handleRoleQuery (params) {
  return get('/system/sysRole/' + params)
}

// 修改角色信息
export function handleUpdateRole (params) {
  return put('/system/sysRole', params)
}

// 添加角色信息
export function handleAddRole (params) {
  return post('/system/sysRole', params)
}

// 删除角色
export function handleDeleteRole (params) {
  return deleted('/system/sysRole/' + params)
}

// 角色绑定权限信息
export function handleBindMenuWithRole (params) {
  return post('/system/sysRole/bind/menu', params)
}
