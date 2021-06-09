import { get, put, post, deleted } from '@/http/axios'

// 获取报名信息分页数据
export function handleExamRegisterInfoPage (params) {
  return get('/examRegisterBackend/examRegisterInfo/page', params)
}

// 获取报名信息列表
export function handleExamRegisterInfoList (params) {
  return get('/examRegisterBackend/examRegisterInfo/list', params)
}
// 根据ID获取报名信息
export function handleExamRegisterInfoQuery (params) {
  return get('/examRegisterBackend/examRegisterInfo/' + params)
}

// 修改报名信息信息
export function handleUpdateExamRegisterInfo (params) {
  return put('/examRegisterBackend/examRegisterInfo', params)
}

// 删除报名信息
export function handleDeleteExamRegisterInfo (params) {
  return deleted('/examRegisterBackend/examRegisterInfo/' + params)
}
