import axios from 'axios'

axios.defaults.baseURL = 'http://localhost:9010'

// 设置请求超时时间
axios.defaults.timeout = 10000

// get请求
export function get (url, params) {
  return new Promise((resolve, reject) => {
    axios({
      method: 'get',
      url: url,
      params: params
    }).then(res => {
      resolve(res)
    }).catch(err => {
      reject(err)
    })
  })
}

// post请求
export function post (url, params, headers) {
  return new Promise((resolve, reject) => {
    axios({
      method: 'post',
      url: url,
      data: params,
      headers: headers || {
        'Content-Type': 'application/json'
      }
    }).then(res => {
      resolve(res)
    }).catch(err => {
      reject(err)
    })
  })
}

// put请求
export function put (url, params) {
  return new Promise((resolve, reject) => {
    axios({
      method: 'put',
      url: url,
      data: params
    }).then(res => {
      resolve(res)
    }).catch(err => {
      reject(err)
    })
  })
}

// deleted请求
export function deleted (url, params) {
  return new Promise((resolve, reject) => {
    axios({
      method: 'delete',
      url: url,
      data: params
    }).then(res => {
      resolve(res)
    }).catch(err => {
      reject(err)
    })
  })
}
