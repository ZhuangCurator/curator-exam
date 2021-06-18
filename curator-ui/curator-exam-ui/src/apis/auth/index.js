import { get } from '@/http/axios'

// 获取图片验证码
export function getImageValidateCode () {
  return get('/examAuthCore/captcha/image/number?width=123&height=40')
}

// 校验验证码
export function handleCheckCaptcha (params) {
  return get('/examAuthCore/check/captcha', params)
}
