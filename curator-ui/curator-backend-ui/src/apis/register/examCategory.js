import { get, put, post, deleted } from '@/http/axios'

// 获取考试类别分页数据
export function handleExamCategoryPage (params) {
  return get('/examRegisterBackend/examCategory/page', params)
}
// 获取考试类别列表
export function handleExamCategoryList (params) {
  return get('/examRegisterBackend/examCategory/list', params)
}
// 根据ID获取考试类别
export function handleExamCategoryQuery (params) {
  return get('/examRegisterBackend/examCategory/' + params)
}

// 修改考试类别信息
export function handleUpdateExamCategory (params) {
  return put('/examRegisterBackend/examCategory', params)
}

// 添加考试类别信息
export function handleAddExamCategory (params) {
  return post('/examRegisterBackend/examCategory', params)
}

// 删除考试类别
export function handleDeleteExamCategory (params) {
  return deleted('/examRegisterBackend/examCategory/' + params)
}
