import { getPermissions } from '@/utils/storage'

// 判断用户的权限是否包含所传入的权限值，若含有，返回 true,若不含，返回false
export function showElement (permissionArray) {
  const superPermission = '*:*:*'
  const permissions = JSON.parse(getPermissions())
  // 此自定义权限指令的值应为数组
  return permissions.some(permission => {
    // 用户为超级管理员或者说含有所需权限 返回true
    return superPermission === permission || permissionArray.includes(permission)
  })
}
