import { get } from '@/http/axios'

// 获取考试科目列表数据
export function handleExamSubjectPage (params) {
  return get('/examRegisterCore/examSubject/page', params)
}
