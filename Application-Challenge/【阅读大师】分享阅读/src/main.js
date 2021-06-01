import Vue from 'vue';
import App from './App.vue';
import router from './router';
import ElementUI from 'element-ui';
import VueSocketIO from 'vue-socket.io';
import 'element-ui/lib/theme-chalk/index.css';
import './assets/styles/common.less';

Vue.config.productionTip = false;

Vue.use(ElementUI);
Vue.use(
  new VueSocketIO({
    debug: true, // debug调试，生产建议关闭
    connection: '127.0.0.1:3000'
  })
);

new Vue({
  router,
  render: h => h(App)
}).$mount('#app');
