import { get, put, post, deleted } from '@/http/axios'

// 获取考试公告分页数据
export function handleExamNoticePage (params) {
  return get('/examRegisterBackend/examNotice/page', params)
}
// 获取考试公告列表
export function handleExamNoticeList (params) {
  return get('/examRegisterBackend/examNotice/list', params)
}
// 根据ID获取考试公告
export function handleExamNoticeQuery (params) {
  return get('/examRegisterBackend/examNotice/' + params)
}

// 修改考试公告信息
export function handleUpdateExamNotice (params) {
  return put('/examRegisterBackend/examNotice', params)
}

// 添加考试公告信息
export function handleAddExamNotice (params) {
  return post('/examRegisterBackend/examNotice', params)
}

// 删除考试公告
export function handleDeleteExamNotice (params) {
  return deleted('/examRegisterBackend/examNotice/' + params)
}
