import { get } from '@/http/axios'

// 获取日志业务类型列表
export function handleBusinessType () {
  return get('/log/operLog/businessType')
}

// 获取日志列表
export function handleLogPage (params) {
  return get('/log/operLog/page', params)
}
