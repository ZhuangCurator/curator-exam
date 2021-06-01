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

// 存储账户权限
export function setPermissions (permissions) {
  window.sessionStorage.setItem('permissions', permissions)
}

// 获取账户权限
export function getPermissions () {
  const permissions = window.sessionStorage.getItem('permissions')
  if (!permissions) return ''
  return permissions
}

// 存储账户名
export function setAccountName (username) {
  window.sessionStorage.setItem('username', username)
}

// 获取账户名
export function getAccountName () {
  const username = window.sessionStorage.getItem('username')
  if (!username) return ''
  return username
}

// 存储是否是超级管理员
export function setSuperAdmin (roleType) {
  const superAdmin = roleType === 1 ? 1 : 0
  window.sessionStorage.setItem('superAdmin', superAdmin.toString())
}

// 获取是否是超级管理员
export function getSuperAdmin () {
  const superAdmin = window.sessionStorage.getItem('superAdmin')
  if (!superAdmin) return 0
  return superAdmin
}
