<template>
	<view class="login">
		<view class="content">
			<!-- 头部logo -->
			<view class="header">
				<image src="@/static/icons/icon_showlog.png"></image>
				<text class="header_line"></text>
				<text class="header_text">医疗 Demo</text>
			</view>
			<!-- 主体表单 -->
			<view class="login_main">
				<text class="header_tips">用户名/room</text>
				<view :class="'login_user ' + nameFocus">
					<input type="text" v-model="username" placeholder="用户名" placeholder-style="color:rgb(173,185,193)"
						@focus="onFocusName" @blur="onBlurName"/>
				</view>
				<text class="header_tips">密码/Key</text>
				<view :class="'login_pwd ' + psdFocus">
					<!-- 文本框 -->
					<input type="text" v-model="userpass" placeholder="密码" placeholder-style="color:rgb(173,185,193)"
						@focus="onFocusPsd" @blur="onBlurPsd" :password="!showPassword" maxlength="16"/>
					<!-- 是否可见密码 -->
					<uni-icons class="pass_icon" :type="showPassword?'eye':'eye-slash'" color="#92939F" size="24" @tap="showPassAction"/>
				</view>
			</view>


			<view class="login_btn">
				<button hover-class="btn_hover" @tap="startLogin">登录</button>
			</view>

			<!-- 注册信息 -->
			<view class="footer">
				<navigator url="register" open-type="navigate">注册账号</navigator>
			</view>

			<!-- 底部信息 -->
			<view class="login_bottom">
				<text>本产品由环信提供 当前版本：1.4.1</text>
			</view>
		</view>
	</view>
</template>

<script>
	let _this;

	export default {
		data() {
			return {
				showPassword: false, //是否显示明文
				//logo图片 base64
				logoImage: '/static/icons/icon_showlog.png',
				phoneData: '', //用户/电话
				passData: '', //密码
				isRotate: false, //是否加载旋转
				isFocus: true, // 是否聚焦
				username: "",
				userpass: "",
				psdFocus: "",
				nameFocus: "",
			};
		},
		components: {

		},
		mounted() {
			_this = this;
			//this.isLogin();
		},
		methods: {
			onFocusPsd: function() {
				this.setData({
					psdFocus: 'psdFocus'
				});
			},
			onBlurPsd: function() {
				this.setData({
					psdFocus: ''
				});
			},
			onFocusName: function() {
				this.setData({
					nameFocus: 'nameFocus'
				});
			},
			onBlurName: function() {
				this.setData({
					nameFocus: ''
				});
			},
			showPassAction() {
				//是否显示密码
				this.showPassword = !this.showPassword
			},
			isLogin() {
				//判断缓存中是否登录过，直接登录
				// try {
				// 	const value = uni.getStorageSync('setUserData');
				// 	if (value) {
				// 		//有登录信息
				// 		console.log("已登录用户：",value);
				// 		_this.$store.dispatch("setUserData",value); //存入状态
				// 		uni.reLaunch({
				// 			url: '../../../pages/index',
				// 		});
				// 	}
				// } catch (e) {
				// 	// error
				// }
			},
			startLogin(e) {
				console.log(e)
				//登录
				if (this.isRotate) {
					//判断是否加载中，避免重复点击请求
					return false;
				}
				if (this.phoneData.length == "") {
					uni.showToast({
						icon: 'none',
						position: 'bottom',
						title: '用户名不能为空'
					});
					return;
				}
				if (this.passData.length < 5) {
					uni.showToast({
						icon: 'none',
						position: 'bottom',
						title: '密码不正确'
					});
					return;
				}

				console.log("登录成功")

				_this.isRotate = true
				setTimeout(function() {
					_this.isRotate = false
				}, 3000)
				// uni.showLoading({
				// 	title: '登录中'
				// });
				// getLogin()
				// .then(res => {
				// 	//console.log(res)
				// 	//简单验证下登录（不安全）
				// 	if(_this.phoneData==res.data.username && _this.passData==res.data.password){
				// 		let userdata={
				// 			"username":res.data.username,
				// 			"nickname":res.data.nickname,
				// 			"accesstoken":res.data.accesstoken,
				// 		} //保存用户信息和accesstoken
				// 		_this.$store.dispatch("setUserData",userdata); //存入状态
				// 		try {
				// 			uni.setStorageSync('setUserData', userdata); //存入缓存
				// 		} catch (e) {
				// 			// error
				// 		}
				// 		uni.showToast({
				// 			icon: 'success',
				// 			position: 'bottom',
				// 			title: '登录成功'
				// 		});
				// 		uni.reLaunch({
				// 			url: '../../../pages/index',
				// 		});
				// 	}else{
				// 		_this.passData=""
				// 		uni.showToast({
				// 			icon: 'error',
				// 			position: 'bottom',
				// 			title: '账号或密码错误，账号admin密码admin'
				// 		});
				// 	}
				// 	uni.hideLoading();
				// }).catch(err => {
				// 	uni.hideLoading();
				// })

			},
			login_weixin() {
				//微信登录
				uni.showToast({
					icon: 'none',
					position: 'bottom',
					title: '...'
				});
			},
			login_weibo() {
				//微博登录
				uni.showToast({
					icon: 'none',
					position: 'bottom',
					title: '...'
				});
			},
			login_github() {
				//github登录
				uni.showToast({
					icon: 'none',
					position: 'bottom',
					title: '...'
				});
			}
		}
	}
</script>

<style>
	@import url("./css/main.css");
</style>
