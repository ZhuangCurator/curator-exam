import { get, put, post, deleted } from '@/http/axios'

// 获取考试科目分页数据
export function handleExamSubjectPage (params) {
  return get('/examRegisterBackend/examSubject/page', params)
}
// 获取考试科目列表
export function handleExamSubjectList (params) {
  return get('/examRegisterBackend/examSubject/list', params)
}
// 根据ID获取考试科目
export function handleExamSubjectQuery (params) {
  return get('/examRegisterBackend/examSubject/' + params)
}

// 修改考试科目信息
export function handleUpdateExamSubject (params) {
  return put('/examRegisterBackend/examSubject', params)
}

// 添加考试科目信息
export function handleAddExamSubject (params) {
  return post('/examRegisterBackend/examSubject', params)
}

// 删除考试科目
export function handleDeleteExamSubject (params) {
  return deleted('/examRegisterBackend/examSubject/' + params)
}

// 获取抽题规则列表
export function handleGenerationRuleList (params) {
  return get('/examRegisterBackend/examSubject/generationRule/list', params)
}

// 添加考点至考试科目
export function handleAddExamSiteToSubject (params) {
  return post('/examRegisterBackend/examSubject/add/examSite', params)
}

// 考试科目下考点分页查询
export function handleExamSitePageWithSubject (params) {
  return get('/examRegisterBackend/examSubject/examSite/page', params)
}
