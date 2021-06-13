import qs from 'qs'
import { get, post } from '@/http/axios'

// 获取图片验证码
export function getImageValidateCode () {
  return get('/auth/validateCode/image?width=123&height=40')
}

// 获取短信验证码
export function getSmsValidateCode (params) {
  return get('/auth/validateCode/sms?mobilePhone=' + params)
}

// 处理用户帐号密码登录
export function handleLogin (params) {
  return post('/auth/login/form', qs.stringify(params), {
    'Content-Type': 'application/x-www-form-urlencoded'
  })
}

// 处理用户短信验证码登录
export function handleMobileLogin (params) {
  return post('/auth/login/mobile', qs.stringify(params), {
    'Content-Type': 'application/x-www-form-urlencoded'
  })
}
