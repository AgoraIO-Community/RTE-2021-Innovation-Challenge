import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    dialogVisible: false,
  },
  mutations: {
    showLogin() {
      this.state.dialogVisible = true;
    },
    hideLogin() {
      this.state.dialogVisible = false;
    },
  },
  actions: {},
  modules: {},
});
