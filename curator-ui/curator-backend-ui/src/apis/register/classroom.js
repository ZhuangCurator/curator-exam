import { get, put, post, deleted } from '@/http/axios'

// 获取教室分页数据
export function handleExamClassroomPage (params) {
  return get('/examRegisterBackend/examClassroom/page', params)
}
// 获取教室列表
export function handleExamClassroomList (params) {
  return get('/examRegisterBackend/examClassroom/list', params)
}
// 根据ID获取教室
export function handleExamClassroomQuery (params) {
  return get('/examRegisterBackend/examClassroom/' + params)
}

// 修改教室信息
export function handleUpdateExamClassroom (params) {
  return put('/examRegisterBackend/examClassroom', params)
}

// 添加教室信息
export function handleAddExamClassroom (params) {
  return post('/examRegisterBackend/examClassroom', params)
}

// 删除教室
export function handleDeleteExamClassroom (params) {
  return deleted('/examRegisterBackend/examClassroom/' + params)
}
