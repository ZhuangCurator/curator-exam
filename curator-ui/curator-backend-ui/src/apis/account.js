import { get, put, post, deleted } from '@/http/axios'

// 获取账户分页数据
export function handleAccountPage (params) {
  return get('/examInfoBackend/infoAccount/page', params)
}
// 根据ID获取账户
export function handleAccountQuery (params) {
  return get('/examInfoBackend/infoAccount/' + params)
}

// 修改账户信息
export function handleUpdateAccount (params) {
  return put('/examInfoBackend/infoAccount/', params)
}

// 添加账户信息
export function handleAddAccount (params) {
  return post('/examInfoBackend/infoAccount/', params)
}

// 删除账户
export function handleDeleteAccount (params) {
  return deleted('/examInfoBackend/infoAccount/' + params)
}

// 账户绑定角色
export function handleBindRoleWithAccount (params) {
  return post('/system/sysAccount/bind/role', params)
}
