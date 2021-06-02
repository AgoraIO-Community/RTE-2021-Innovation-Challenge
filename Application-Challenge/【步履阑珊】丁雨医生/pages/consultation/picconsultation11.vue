<template>
	<view>
		<view class="uni-flex">
			<text class="piccon_section">已预约以下时间</text>
		</view>
		<view class="uni-flex justify-around">
			<view class="piccon_flex">
				<text class="piccon_header">11月2日，周一，7:00-8:00</text>
				<text class="piccon_second">医生会在该时间段联系你，请保持手机畅通</text>
			</view>
			<text class="piccon_right">重新预约</text>
		</view>
		<view class="uni-flex mt-20">
			<text class="piccon_title">病情描述</text>
		</view>
		<textarea v-model="content" placeholder="请详细描述您的疾病或症状、是否用药、曾经做过的检查、治疗情况，以及想获得医生的帮助；

为了医生更准确诊断您的问题，请尽可能详细描述预约问题，如有检查报告等尽量上传（不少于20字）" class="piccon_textarea" />
		<view class="uni-flex flex-column text-left mt-48">
			<text class="piccon_title">图片资料 (非必填)</text>
			<text class="piccon_text">上传能展示病情的患处照片、检查资料、医院就诊病历记录等上传的内容仅对医生可见</text>
		</view>
		<!-- 多图上传 -->
		<view class="uni-uploader-body m-24">
			<view class="uni-uploader__files">
				<block v-for="(image,index) in imageList" :key="index">
					<view class="uni-uploader__file">
						<image class="uni-uploader__img" :src="image" :data-src="image" @tap="previewImage"></image>
					</view>
				</block>
				<view class="uni-uploader__input-box">
					<view class="uni-uploader__input" @tap="chooseImage"></view>
				</view>
			</view>
		</view>
		<view class="uni-flex">
			<button type="primary" class="piccon_button" @click="piconextAction11"><text
					class="font-md ml-2">下一步</text></button>
		</view>
	</view>
</template>

<script>
	import permision from "@/common/permission.js"
	var sourceType = [
		['camera'],
		['album'],
		['camera', 'album']
	]
	var sizeType = [
		['compressed'],
		['original'],
		['compressed', 'original']
	]
	export default {
		data() {
			return {
				content: "",
				imageList: [],
				sourceTypeIndex: 2,
				sourceType: ['拍照', '相册', '拍照或相册'],
				sizeTypeIndex: 2,
				sizeType: ['压缩', '原图', '压缩或原图'],
				countIndex: 8,
				count: [1, 2, 3, 4, 5, 6, 7, 8, 9],
				showBack: false
			}
		},
		onUnload() {
			this.imageList = [],
				this.sourceTypeIndex = 2,
				this.sourceType = ['拍照', '相册', '拍照或相册'],
				this.sizeTypeIndex = 2,
				this.sizeType = ['压缩', '原图', '压缩或原图'],
				this.countIndex = 8;
		},
		methods: {
			chooseImage: async function() {
				// #ifdef APP-PLUS
				// TODO 选择相机或相册时 需要弹出actionsheet，目前无法获得是相机还是相册，在失败回调中处理
				if (this.sourceTypeIndex !== 2) {
					let status = await this.checkPermission();
					if (status !== 1) {
						return;
					}
				}
				// #endif

				if (this.imageList.length === 9) {
					let isContinue = await this.isFullImg();
					console.log("是否继续?", isContinue);
					if (!isContinue) {
						return;
					}
				}
				uni.chooseImage({
					sourceType: sourceType[this.sourceTypeIndex],
					sizeType: sizeType[this.sizeTypeIndex],
					count: this.imageList.length + this.count[this.countIndex] > 9 ? 9 - this.imageList
						.length : this.count[this.countIndex],
					success: (res) => {
						this.imageList = this.imageList.concat(res.tempFilePaths);
					},
					fail: (err) => {
						// #ifdef APP-PLUS
						if (err['code'] && err.code !== 0 && this.sourceTypeIndex === 2) {
							this.checkPermission(err.code);
						}
						// #endif
						// #ifdef MP
						uni.getSetting({
							success: (res) => {
								let authStatus = false;
								switch (this.sourceTypeIndex) {
									case 0:
										authStatus = res.authSetting['scope.camera'];
										break;
									case 1:
										authStatus = res.authSetting['scope.album'];
										break;
									case 2:
										authStatus = res.authSetting['scope.album'] && res
											.authSetting['scope.camera'];
										break;
									default:
										break;
								}
								if (!authStatus) {
									uni.showModal({
										title: '授权失败',
										content: 'Hello uni-app需要从您的相机或相册获取图片，请在设置界面打开相关权限',
										success: (res) => {
											if (res.confirm) {
												uni.openSetting()
											}
										}
									})
								}
							}
						})
						// #endif
					}
				})
			},
			isFullImg: function() {
				return new Promise((res) => {
					uni.showModal({
						content: "已经有9张图片了,是否清空现有图片？",
						success: (e) => {
							if (e.confirm) {
								this.imageList = [];
								res(true);
							} else {
								res(false)
							}
						},
						fail: () => {
							res(false)
						}
					})
				})
			},
			previewImage: function(e) {
				var current = e.target.dataset.src
				uni.previewImage({
					current: current,
					urls: this.imageList
				})
			},
			async checkPermission(code) {
				let type = code ? code - 1 : this.sourceTypeIndex;
				let status = permision.isIOS ? await permision.requestIOS(sourceType[type][0]) :
					await permision.requestAndroid(type === 0 ? 'android.permission.CAMERA' :
						'android.permission.READ_EXTERNAL_STORAGE');

				if (status === null || status === 1) {
					status = 1;
				} else {
					uni.showModal({
						content: "没有开启权限",
						confirmText: "设置",
						success: function(res) {
							if (res.confirm) {
								permision.gotoAppSetting();
							}
						}
					})
				}

				return status;
			},
			piconextAction11() {
				console.log("piconextAction11");
				uni.navigateTo({
					url: 'picconsultation12?userid=12',
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
		background-color: #FFFFFF;
		min-height: 100%;
		height: auto;
	}

	.piccon_section {
		margin: 24rpx 32rpx;
		font-size: 24rpx;
		color: #BFC0C8;
		letter-spacing: 2rpx;
		font-family: "SimHei";
	}
	
	.piccon_flex {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: flex-start;
	}
	
	.piccon_header {
		margin: 4rpx;
		font-size: 32rpx;
		color: #535568;
		line-height: 40rpx;
	}
	
	.piccon_second {
		margin: 4rpx;
		font-size: 24rpx;
		color: #BFC0C8;
	}
	
	.piccon_right {
		margin:16rpx;
		font-size: 32rpx;
		color: #BFC0C8;
	}

	.piccon_title {
		margin: 24rpx 32rpx;
		font-size: 32rpx;
		color: #535568;
		font-weight: bold;
		letter-spacing: 2rpx;
		font-family: "SimHei";
	}

	.piccon_text {
		margin: 24rpx 32rpx;
		font-size: 28rpx;
		color: #9FA4AE;
	}

	.piccon_textarea {
		width: 88%;
		text-align: left;
		margin: 4rpx 3%;
		padding: 3%;
		background-color: #F7F8FA;
		font-size: 28rpx !important;
	}

	.piccon_button {
		width: 92%;
		padding: 0 72rpx;
		margin: 64rpx 32rpx;
		font-size: 48rpx;
		line-height: 1.8;
		border-radius: 8rpx;
		border-width: 0rpx;
		background-image: linear-gradient(90deg, #07C193, #3EAAB4);
	}
</style>
