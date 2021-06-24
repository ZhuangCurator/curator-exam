import { get } from '@/http/axios'

// 获取考试类别列表数据
export function handleExamCategoryPage (params) {
  return get('/examRegisterCore/examCategory/page', params)
}
