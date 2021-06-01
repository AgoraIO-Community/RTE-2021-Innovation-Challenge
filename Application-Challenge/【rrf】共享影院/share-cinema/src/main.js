import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
import VideoPlayer from "vue-video-player";
require("video.js/dist/video-js.css");
require("vue-video-player/src/custom-theme.css");
import Axios from "axios";
import ChatView from "./components/chat/chat.vue";
import { io } from "socket.io-client";

let url = "http://localhost:5000/";
try {
  url = require("../server.js");
} catch (e) {
}

Vue.use(ElementUI);
Vue.use(VideoPlayer);
Vue.component("ChatView", ChatView);
Vue.config.productionTip = false;

var socket = io(url);
Vue.prototype.$socketio = socket;

new Vue({
  router,
  render: (h) => h(App),
}).$mount("#app");
