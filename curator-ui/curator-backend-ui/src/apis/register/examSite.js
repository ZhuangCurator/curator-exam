import { get, put, post, deleted } from '@/http/axios'

// 获取考点分页数据
export function handleExamSitePage (params) {
  return get('/examRegisterBackend/examSite/page', params)
}
// 获取考点列表
export function handleExamSiteList (params) {
  return get('/examRegisterBackend/examSite/list', params)
}
// 根据ID获取考点
export function handleExamSiteQuery (params) {
  return get('/examRegisterBackend/examSite/' + params)
}

// 修改考点信息
export function handleUpdateExamSite (params) {
  return put('/examRegisterBackend/examSite', params)
}

// 添加考点信息
export function handleAddExamSite (params) {
  return post('/examRegisterBackend/examSite', params)
}

// 删除考点
export function handleDeleteExamSite (params) {
  return deleted('/examRegisterBackend/examSite/' + params)
}
