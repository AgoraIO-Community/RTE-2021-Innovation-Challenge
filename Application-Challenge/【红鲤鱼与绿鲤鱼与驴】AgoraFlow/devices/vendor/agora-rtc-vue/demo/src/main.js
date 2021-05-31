import Vue from 'vue'
import App from './App.vue'
import AgoraRTC from "../agora-rtc-vue"
import "../agora-rtc-vue/lib/agora-rtc-vue.css"

Vue.use(AgoraRTC)
Vue.config.productionTip = false

new Vue({
  render: h => h(App),
}).$mount('#app')
