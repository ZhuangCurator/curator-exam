import store from '../../store'
export default {
  inserted (el, binding) {
    const { value } = binding
    const superPermission = '*:*:*'
    const storePer = store.getters.permissions
    // 此自定义权限指令的值应为数组
    if (value && value instanceof Array && value.length > 0) {
      const permissionFlag = value

      const hasPermissions = storePer.some(permission => {
        // 账户为超级管理员或者说含有所需权限 返回true
        return superPermission === permission || permissionFlag.includes(permission)
      })

      if (!hasPermissions) {
        el.parentNode.removeChild(el)
      }
    } else {
      throw new Error('请设置操作权限标签值')
    }
  }
}
