import { get, post } from '@/http/axios'

// 考生报名信息查询
export function handlePageWithRegisterInfo (params) {
  return get('/examRegisterCore/examRegisterInfo/pageWithRegisterInfo', params)
}

// 考生报名
export function handleAccountRegister (params) {
  return post('/examRegisterCore/examRegisterInfo/register', params)
}
