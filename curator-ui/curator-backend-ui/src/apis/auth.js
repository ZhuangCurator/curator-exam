import qs from 'qs'
import { get, post } from '@/http/axios'

// 获取图片验证码
export function getImageValidateCode () {
  return get('/examAuthCore/captcha/image/number?width=123&height=40')
}

// 获取短信验证码
export function getSmsValidateCode (params) {
  return get('/examAuthCore/captcha/sms?mobile=' + params)
}

// 处理帐号密码登录
export function handleLogin (params) {
  return post('/examAuthCore/login/account', qs.stringify(params), {
    'Content-Type': 'application/x-www-form-urlencoded'
  })
}

// 处理用户短信验证码登录
export function handleMobileLogin (params) {
  return post('/auth/login/mobile', qs.stringify(params), {
    'Content-Type': 'application/x-www-form-urlencoded'
  })
}
