import { get, post } from '@/http/axios'

// 考生报名信息查询
export function handlePageWithRegisterInfo (params) {
  return get('/examRegisterCore/examRegisterInfo/pageWithRegisterInfo', params)
}

// 考生报名
export function handleAccountRegister (params) {
  return post('/examRegisterCore/examRegisterInfo/register', params)
}

// 预校验准考证信息
export function handlePreviewAdmissionTicket (params) {
  return get('/examRegisterCore/examRegisterInfo/preview/admissionTicket', params)
}

// 打印准考证
export function handlePrintAdmissionTicket (params) {
  return get('/examRegisterCore/examRegisterInfo/pdf', params, 'blob')
}
