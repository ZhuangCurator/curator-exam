// 存储token
export function setToken (token) {
  window.sessionStorage.setItem('token', token)
}

// 获取token
export function getToken () {
  const token = window.sessionStorage.getItem('token')
  if (!token) return ''
  return token
}

// 存储用户权限
export function setPermissions (permissions) {
  window.sessionStorage.setItem('permissions', permissions)
}

// 获取用户权限
export function getPermissions () {
  const permissions = window.sessionStorage.getItem('permissions')
  if (!permissions) return ''
  return permissions
}

// 存储用户名
export function setUsername (username) {
  window.sessionStorage.setItem('username', username)
}

// 获取用户名
export function getUsername () {
  const username = window.sessionStorage.getItem('username')
  if (!username) return ''
  return username
}

// 存储用户头像
export function setAvatar (avatar) {
  window.sessionStorage.setItem('avatar', avatar)
}

// 获取用户头像
export function getAvatar () {
  const avatar = window.sessionStorage.getItem('avatar')
  if (!avatar) return ''
  return avatar
}
