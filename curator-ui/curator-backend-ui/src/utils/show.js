import store from '../store'

// 判断账户的权限是否包含所传入的权限值，若含有，返回 true,若不含，返回false
export function showElement (permissionArray) {
  const superPermission = '*:*:*'
  const storePer = store.getters.permissions
  // console.log('show.js: ' + storePer)
  // const permsArray = storePer.split(',')
  // console.log(permsArray)
  // 此自定义权限指令的值应为数组
  return storePer.some(perms => {
    // 账户为超级管理员或者说含有所需权限 返回true
    return superPermission === perms || permissionArray.includes(perms)
  })
}
