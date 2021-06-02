import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";

import axios from "axios";
import request from "../utils/request";
import Vant from "vant";
import "vant/lib/index.css";
Vue.use(Vant);

Vue.prototype.$axios = axios;
Vue.prototype.$request = request;
Vue.config.productionTip = false;

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount("#app");
