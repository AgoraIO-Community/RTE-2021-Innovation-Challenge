<template>
	<view>
		<uni-list class="text-left">
			<uni-list-item title="会话消息通知">
				<view slot="footer">
					<switch checked color="#25B4A5"/>
				</view>
			</uni-list-item>
			<uni-list-item showArrow title="修改密码" />
			<uni-list-item showArrow title="注销账号" />
		</uni-list>

		<view class="fixed-bottom m-32">
			<button class="bg-main text-white" style="border-radius: 50rpx;border: 0;" type="primary"
				@tap="userQuite()">退出登录</button>
		</view>

	</view>
</template>

<script>
	import store from '@/store/index.js';
	export default {
		components: {

		},
		data() {
			return {
				currentSize: 0
			}
		},
		onLoad() {
			this.getStorageInfo()
		},
		filters: {
			format(value) {
				return value > 1024 ? (value / 1024).toFixed(2) + 'MB' : value.toFixed(2) + 'KB';
			}
		},
		methods: {
			getStorageInfo() {
				let res = uni.getStorageInfoSync()
				this.currentSize = res.currentSize
			},
			open(path) {
				uni.navigateTo({
					url: `../${path}/${path}`
				});
			},
			// 清除缓存
			clear() {
				uni.showModal({
					title: '提示',
					content: '是否要清除所有缓存？',
					cancelText: '不清除',
					confirmText: '清除',
					success: res => {
						if (res.confirm) {
							uni.clearStorageSync()
							this.getStorageInfo()
							uni.showToast({
								title: '清除成功',
								icon: 'none'
							});
						}
					},
				});
			},
			userQuite() {
				uni.showModal({
					title: "提示",
					content: "是否确认退出",
					showCancel: true,
					cancelText: '取消',
					confirmText: '退出',
					success: res => {
						if (res.confirm) {
							uni.removeStorage({
								key: 'user-info'
							});
							store.commit('logout');
							uni.navigateBack({
								delta: 1
							});
						} else {

						}
					}
				})
			},
		}
	}
</script>

<style>
	page {
		display: flex;
		flex-direction: column;
		box-sizing: border-box;
		background-color: #F7F8FA;
		min-height: 100%;
		height: auto;
	}
</style>
