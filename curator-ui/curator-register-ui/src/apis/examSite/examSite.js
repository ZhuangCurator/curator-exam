import { get } from '@/http/axios'

// 获取考点分页数据
export function handleExamSitePage (params) {
  return get('/examRegisterCore/examSite/page', params)
}
