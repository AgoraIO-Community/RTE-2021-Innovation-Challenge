<template>
	<view>
		<view class="flex align-center bg-white" @click="openMineData()">
			<image src="@/static/icons/icon_assistanceavatar.png" style="width: 128rpx;height: 128rpx;"
				class="rounded16 m-24"></image>
			<view class="flex flex-column flex-1 px-2 align-start">
				<text class="font-md font-weight-bold text-dark">郑文文</text>
			</view>
		</view>

		<view class="flex align-center mt-32">
			<uni-list class="text-left" :border="false">
				<uni-list-item showArrow="true" title="设置" showExtraIcon="true" to="../mine/usersetings">
					<template slot="header">
						<image class="slot-image" src="@/static/iconfont/icon_assetting1.png"></image>
					</template>
				</uni-list-item>
			</uni-list>
		</view>
		
		<view class="flex align-center mt-32">
			<uni-list class="text-left" :border="false">
				<uni-list-item showArrow="true" title="问诊指南" showExtraIcon="true">
					<template slot="header">
						<image class="slot-image" src="@/static/iconfont/icon_assetting2.png"></image>
					</template>
				</uni-list-item>
			</uni-list>
		</view>
	</view>
</template>

<script>
	import uniListItem from '@/components/uni-list-item/uni-list-item.vue';
	import store from '@/store/index.js';
	export default {
		components: {
			uniListItem
		},
		data() {
			return {

			}
		},
		onLoad() {
		if (!store.state.hasLogin) {
			return uni.navigateTo({
				url: '../login/login'
			});
		}
		},
		onReady() {

		},
		onShow: function() {
			if (!store.state.hasLogin) {
				uni.showToast({
					title: '请先登录',
					icon: 'none'
				});
			}
		},
		methods: {
			// 点击右上角设置
			openMineSetting() {
				uni.navigateTo({
					url: '../usersetings/usersetings'
				});
			},
			openMineData() {
				uni.navigateTo({
					url: '../mine/personal-data'
				});
			}
		}
	}
</script>

<style>
	.border-main {
		padding:0rpx 16rpx;
		border-radius: 16rpx;
		border-color: #25B4A5;
		border-width: 2rpx;
		border-style: solid;
	}

	.slot-image {
		/* #ifndef APP-NVUE */
		display: block;
		/* #endif */
		margin-right: 32rpx;
		margin-top: 4rpx;
		width: 48rpx;
		height: 51rpx;
	}
</style>
