import Vue from 'vue'
import App from './App.vue'
import router from './router'
import './plugins/element.js'
import VDistpicker from 'v-distpicker'

// 引入全局样式
import './assets/css/global.css'

Vue.config.productionTip = false
Vue.component('v-distpicker', VDistpicker)

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
