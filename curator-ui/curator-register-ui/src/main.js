import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import './plugins/element.js'
// 引入全局样式
import './assets/css/global.css'
// 引入地区选择器
import VDistpicker from 'v-distpicker'

Vue.config.productionTip = false
// 全局注册地区选择器
Vue.component('v-distpicker', VDistpicker)

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
