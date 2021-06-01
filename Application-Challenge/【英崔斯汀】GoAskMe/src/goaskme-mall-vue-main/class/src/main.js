import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
import axios from "axios";
import request from "../utils/request";
import Web3 from 'web3'
Vue.prototype.Web3 = Web3;

Vue.use(ElementUI);
Vue.prototype.$axios = axios;
Vue.prototype.$request = request;
Vue.config.productionTip = false;

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount("#app");
