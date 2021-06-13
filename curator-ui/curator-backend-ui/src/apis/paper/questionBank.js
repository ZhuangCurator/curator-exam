import { get, put, post, deleted } from '@/http/axios'

// 获取试题库分页数据
export function handleQuestionBankPage (params) {
  return get('/examPaperBackend/questionBank/page', params)
}
// 获取试题库列表
export function handleQuestionBankList (params) {
  return get('/examPaperBackend/questionBank/list', params)
}
// 根据ID获取试题库
export function handleQuestionBankQuery (params) {
  return get('/examPaperBackend/questionBank/' + params)
}

// 修改试题库信息
export function handleUpdateQuestionBank (params) {
  return put('/examPaperBackend/questionBank', params)
}

// 添加试题库信息
export function handleAddQuestionBank (params) {
  return post('/examPaperBackend/questionBank', params)
}

// 删除试题库
export function handleDeleteQuestionBank (params) {
  return deleted('/examPaperBackend/questionBank/' + params)
}

// 获取试题库试题分页数据
export function handleBankQuestionPage (params) {
  return get('/examPaperBackend/questionBank/page/question', params)
}

// 删除试题库中试题
export function handleDeleteBankQuestion (params) {
  return post('/examPaperBackend/questionBank/remove/question', params)
}

// 添加试题至试题库
export function handleAddQuestionToQuestionBank (params) {
  return post('/examPaperBackend/questionBank/bind/question', params)
}
