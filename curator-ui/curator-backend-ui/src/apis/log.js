import { get } from '@/http/axios'

// 获取日志列表
export function handleLogPage (params) {
  return get('/examLogCore/infoRequestLog/page', params)
}
