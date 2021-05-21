import { get, put, post, deleted } from '@/http/axios'

// 获取用户列表
export function handleUserPage (params) {
  return get('/system/sysUser/page', params)
}
// 根据ID获取用户
export function handleUserQuery (params) {
  return get('/system/sysUser/' + params)
}

// 修改用户信息
export function handleUpdateUser (params) {
  return put('/system/sysUser', params)
}

// 添加用户信息
export function handleAddUser (params) {
  return post('/system/sysUser', params)
}

// 删除用户
export function handleDeleteUser (params) {
  return deleted('/system/sysUser/' + params)
}

// 用户绑定角色
export function handleBindRoleWithUser (params) {
  return post('/system/sysUser/bind/role', params)
}
