import { get, post, put } from '@/http/axios'

// 考生登录
export function handlePaperLogin (params) {
  return get('/examPaperCore/testPaper/login', params)
}

// 初始化试卷
export function handlePaperInit (params) {
  return post('/examPaperCore/testPaper/init', params)
}

// 校验考试口令
export function handleVerifyPassword (params) {
  return get('/examPaperCore/testPaper/verifyPassword', params)
}

// 试卷中试题类型和数量
export function handleQuestionTypeAndNumQuery (params) {
  return get('/examPaperCore/testPaper/paperQuestion/typeAndNum', params)
}

// 试卷中单个试题
export function handleSingleQuestionQuery (params) {
  return get('/examPaperCore/testPaper/single/question', params)
}

// 保存用户答案
export function handleUserAnswerSave (params) {
  return put('/examPaperCore/testPaper/save/userAnswer', params)
}

// 阅卷
export function handlePaperMark (params) {
  return put('/examPaperCore/testPaper/mark', params)
}
