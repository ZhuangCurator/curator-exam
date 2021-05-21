// 路由改造方法
// 遍历后台传来的路由集合，转换为组件对象
export function filterAsyncRouter (asyncRouterMap) {
  return asyncRouterMap.filter(route => {
    if (route.component) {
      route.component = loadView(route.component)
    }
    if (route.children != null && route.children && route.children.length) {
      route.children = filterAsyncRouter(route.children)
    } else {
      delete route.children
    }
    return true
  })
}

export const loadView = (view) => { // 路由懒加载
  return (resolve) => require([`@/components/${view}`], resolve)
}
