import { get } from '@/http/axios'

// 获取考试科目列表数据
export function handleExamSubjectList (params) {
  return get('/examRegisterCore/examSubject/list', params)
}
