import hasPermission from './permission/hasPermission'

const install = function (Vue) {
  Vue.directive('has-perm', hasPermission)
}

export default install
