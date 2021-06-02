import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
	state: {
		hasLogin: false,
		loginProvider: "",
		openid: null,
		testvuex:false,
	    colorIndex: 0,
	    colorList: ['#FF0000','#00FF00','#0000FF']
	},
	mutations: {
		login(state, provider) {
			state.hasLogin = true;
			state.loginProvider = provider;
		},
		logout(state) {
			state.hasLogin = false
			state.openid = null
		},
		setOpenid(state, openid) {
			state.openid = openid
		},
		setTestTrue(state){
			state.testvuex = true
		},
		setTestFalse(state){
			state.testvuex = false
		},
	    setColorIndex(state,index){
	        state.colorIndex = index
	    }
	},
	getters:{
	    currentState(state){
	        return state.hasLogin
	    }
	},
	actions: {
		// lazy loading openid
		
	}
})
