<script>
	// #ifdef APP-PLUS
	import APPUpdate from '@/plugins/APPUpdate';
	// #endif
	export default {
		onLaunch: function() {
			console.log('App Launch');
			// #ifdef APP-PLUS
			let platform = uni.getSystemInfoSync().platform;
			if(platform==='android'){
				APPUpdate();
				console.log(platform);
			}
			// #endif
		},
		onShow: function() {
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		}
	}
</script>

<style>
	/* #ifndef APP-PLUS-NVUE */
	/* uni.css - 通用组件、模板样式库，可以当作一套ui库应用 */
	/* @import "./common/main.css"; */
	@import "./common/icon.css";
	/*每个页面公共css */
	@import './common/common.css';
	/*每个页面公共css */
	/* 官方css库 */
	@import "./common/uni.css";
	/* 自定义图标库 */
	@import "./common/iconfont.css";
	/* 动画库 */
	@import "./common/animate.css";
	/* 自定义样式库 */
	@import "./common/free.css";
	/* #endif*/
</style>
