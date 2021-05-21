import { deleted, get, post, put } from '@/http/axios'

// 获取部门列表
export function handleDepartmentPage (params) {
  return get('/system/sysDept/list', params)
}

// 获取部门数据
export function handleDepartmentQuery (params) {
  return get('/system/sysDept/' + params)
}

// 更新部门数据
export function handleUpdateDepartment (params) {
  return put('/system/sysDept', params)
}

// 更新部门数
export function handleUpdateDepartmentStatus (params) {
  return put('/system/sysDept/status', params)
}

// 添加部门数据
export function handleAddDepartment (params) {
  return post('/system/sysDept', params)
}

// 删除部门数据
export function handleDeleteDepartment (params) {
  return deleted('/system/sysDept/' + params)
}
