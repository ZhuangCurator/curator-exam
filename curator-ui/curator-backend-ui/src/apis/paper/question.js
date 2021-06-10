import { get, put, post, deleted } from '@/http/axios'

// 获取试题分页数据
export function handleQuestionPage (params) {
  return get('/examPaperBackend/question/page', params)
}
// 获取试题列表
export function handleQuestionList (params) {
  return get('/examPaperBackend/question/list', params)
}
// 根据ID获取试题
export function handleQuestionQuery (params) {
  return get('/examPaperBackend/question/' + params)
}

// 修改试题信息
export function handleUpdateQuestion (params) {
  return put('/examPaperBackend/question', params)
}

// 添加试题信息
export function handleAddQuestion (params) {
  return post('/examPaperBackend/question', params)
}

// 删除试题
export function handleDeleteQuestion (params) {
  return deleted('/examPaperBackend/question/' + params)
}
