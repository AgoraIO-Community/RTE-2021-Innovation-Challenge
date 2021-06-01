import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    loginStatus: false,
  },
  mutations: {
    showLogin() {
      this.state.loginStatus = true;
    },
    hideLogin() {
      this.state.loginStatus = false;
    },
  },
  actions: {},
  modules: {},
});
