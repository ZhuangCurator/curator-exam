import { get } from '@/http/axios'

// 考生报名信息查询
export function handlePageWithRegisterInfo (params) {
  return get('/examRegisterCore/examRegisterInfo/pageWithRegisterInfo', params)
}
