import { get } from '@/http/axios'

// 获取考试公告分页数据
export function handleExamNoticePage (params) {
  return get('/examRegisterCore/examNotice/page', params)
}

// 根据ID获取考试公告
export function handleExamNoticeQuery (params) {
  return get('/examRegisterCore/examNotice/' + params)
}
