import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import './plugins/element.js'
import directive from '@/directive/index'
import VDistpicker from 'v-distpicker'

// 引入字体图标
import './assets/font/iconfont.css'
// 引入全局样式
import './assets/css/global.css'

Vue.config.productionTip = false
Vue.use(directive)
Vue.component('v-distpicker', VDistpicker)

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
