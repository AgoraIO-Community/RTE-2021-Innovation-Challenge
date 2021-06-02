<template>
	<view class="page">
		<uni-nav-bar :status-bar="true" backgroundColor="#07C193" color="#ffffff" left-icon="back" :border="false"
			@clickLeft="goBackAction">
		</uni-nav-bar>
		<view class="docdetail_header">
			<view class="docdetail_head_bg">
				<view class="bg_square_01"></view>
				<view class="bg_square_02"></view>
			</view>
			<view class="docdetail_head_body">
				<view class="headerbody_card">
					<view class="docdet-item-row">
						<image :src="detailData.docavatar" class="docdet-item-avatar"></image>
						<view class="docdet-item-right">
							<view class="hei-48"><text class="font-34 text-bold">{{detailData.docname}}</text>
								<text class="font-24 ml-20">{{detailData.doclevel}}</text>
							</view>
							<view class="mt-16"><text
									class="font-24">{{detailData.dochospital}}&nbsp;&nbsp;&nbsp;&nbsp;{{detailData.docpartment}}</text>
							</view>
							<view class="docdet-item-docexpert">
								{{detailData.docexpert}}
							</view>
							<view class="docdet-item-bottom">
								<view class="docdet-item-cell">
									<text class="hei-36">问诊量</text><text
										class="font-40 text-cyan font-weight-bolder">{{detailData.docconsultation}}</text>
								</view>
								<view class="docdet-item-cell">
									<text class="hei-36">平均回复时间</text><text
										class="font-40 text-cyan font-weight-bolder">{{detailData.docresponse}}分钟</text>
								</view>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>
		<view class="uni-flex">
			<text class="docdet_headtag text-gray-normal">问诊方式</text>
		</view>
		<view class="flex align-center">
			<uni-list class="text-left" :border="false">
				<uni-list-item showArrow="true" showExtraIcon="true" to="../../consultation/picconsultation01">
					<view slot="body" class="slot-box">
						<view class="docdet-item__content">
							<text class="docdet-item__content-title">图文问诊</text>
							<text class="docdet-item__content-price">¥79</text>
							<text class="docdet-item__content-note">通过文字、图片向医生提问，含2次免费追问机会，医生未回复退全款。</text>
						</view>
					</view>
					<template slot="header">
						<image class="slot-image" src="@/static/iconfont/icon_docdet01.png"></image>
					</template>
				</uni-list-item>
				<uni-list-item showArrow="true" showExtraIcon="true" to="../../consultation/picconsultation10">
					<view slot="body" class="slot-box">
						<view class="docdet-item__content">
							<view><text class="docdet-item__content-title">电话问诊</text><text
									class="docdet-item__content-tag">最早今天16:00可约</text></view>
							<text class="docdet-item__content-price">¥99</text>
							<text class="docdet-item__content-note">通过文字、图片向医生提问，含2次免费追问机会，医生未回复退全款。</text>
						</view>
					</view>
					<template slot="header">
						<image class="slot-image" src="@/static/iconfont/icon_docdet02.png"></image>
					</template>
				</uni-list-item>
				<uni-list-item showArrow="true" showExtraIcon="true" to="../../consultation/picconsultation10">
					<view slot="body" class="slot-box">
						<view class="docdet-item__content">
							<view><text class="docdet-item__content-title">视频问诊</text><text
									class="docdet-item__content-tag">最早今天16:00可约</text></view>
							<text class="docdet-item__content-price">¥99</text>
							<text class="docdet-item__content-note">通过文字、图片向医生提问，含2次免费追问机会，医生未回复退全款。</text>
						</view>
					</view>
					<template slot="header">
						<image class="slot-image" src="@/static/iconfont/icon_docdet03.png"></image>
					</template>
				</uni-list-item>
			</uni-list>
		</view>
	</view>
</template>

<script>
	import uniNavBar from '@/components/uni-nav-bar/uni-nav-bar.vue';
	export default {
		components: {
			uniNavBar
		},
		data() {
			return {
				detailData: {}
			}
		},
		onLoad(e) {
			// 初始化
			this.detailData = JSON.parse(e.detailData);
			console.log(this.detailData);
		},
		methods: {
			// 返回上一步
			goBackAction() {
				uni.navigateBack({
					delta: 1
				});
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

	.docdetail_header {
		display: flex;
		width: 100%;
		height: 540rpx;

	}

	.docdetail_head_bg {
		width: 100%;
		display: flex;
		flex-direction: column;
		height: 480rpx;
	}

	.bg_square_01 {
		width: 100%;
		flex: 3;
		background-image: linear-gradient(#07C193, #3EAAB4);
	}

	.bg_square_02 {
		width: 100%;
		flex: 1;
	}

	.docdetail_head_body {
		position: absolute;
		width: 100%;
		display: flex;
		flex-direction: column;
		height: 480rpx;
	}

	.headerbody_card {
		background-color: #FFFFFF;
		width: 92%;
		display: flex;
		flex-direction: column;
		height: 480rpx;
		min-height: 480rpx;
		margin: 32rpx 32rpx 32rpx;
		border-radius: 8rpx;
	}

	.docdet-item-row {
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: row;
	}

	.docdet-item-avatar {
		margin: 48rpx 32rpx;
		width: 128rpx;
		height: 128rpx;
		min-width: 128rpx;
	}

	.docdet-item-right {
		flex-direction: column;
		text-align: left;
		margin: 48rpx 24rpx 24rpx 4rpx;
	}

	.docdet-item-docexpert {
		display: flex;
		margin-top: 24rpx;
		color: #9FA4AE;
		font-size: 24rpx;
		flex-direction: row;
	}

	.docdet-item-bottom {
		display: flex;
		margin-top: 24rpx;
		color: #9FA4AE;
		font-size: 24rpx;
		flex-direction: row;
	}

	.docdet-item-cell {
		display: flex;
		flex-direction: column;
		flex: 1;
	}

	.docdet_headtag {
		color: #535568;
		font-size: 32rpx;
		font-weight: 600;
		margin: 32rpx;
	}

	.slot-image {
		/* #ifndef APP-NVUE */
		display: block;
		/* #endif */
		margin-right: 32rpx;
		margin-top: 8rpx;
		min-width: 84rpx;
		width: 84rpx;
		height: 84rpx;
	}

	.docdet-item__content {
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		padding-right: 8px;
		flex: 1;
		color: #3b4144;
		// overflow: hidden;
		flex-direction: column;
		justify-content: space-between;
		overflow: hidden;
	}

	.docdet-item__content--center {
		justify-content: center;
	}

	.docdet-item__content-title {
		font-size: 34rpx;
		color: #535568;
		overflow: hidden;
	}

	.docdet-item__content-tag {
		border-radius: 8rpx;
		padding: 4rpx 16rpx;
		color: #25B4A5;
		margin-left: 20rpx;
		font-size: 20rpx;
		;
		background-color: #E8F7F5;
	}

	.docdet-item__content-price {
		font-size: 32rpx;
		color: #25B4A5;
		font-weight: 600;
		overflow: hidden;
	}

	.docdet-item__content-note {
		margin-top: 6rpx;
		color: #9FA4AE;
		font-size: 24rpx;
		overflow: hidden;
	}
</style>
