import { get } from '@/http/axios'

// 获取考试类别列表数据
export function handleExamCategoryList (params) {
  return get('/examRegisterCore/examCategory/list', params)
}
