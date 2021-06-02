<template>
		<!-- 显示信息层 -->
		<view class="uni-popup-share">
			<view style="height:40rpx;"></view>
			<text class="example-info">当前定位</text>
			<view class="uni-share-button-box">
				<view class="tag-radius-background mt-2" @click="selectAddress()">
					<text class="text-main">{{localDic.area_name}}<uni-icons color="#6BBBFF" size="8" class="iconfont iconguojiaquhaoxialaanniu mt-2 ml-2"/></text>
				</view>
			</view>
			<view class="part mx-2">
				<view class="text-left mx-2 mt-4">
					<text class="mr-2">价格范围</text>
					<text class="text-main font-md">￥{{ rangeValues[0] }}</text>
					<text class="text-main font-md">~</text>
					<text class="text-main font-md">￥{{ rangeValues[1] }}</text>
				</view>
				<view class="rowBox m-2">
					<view class="sliderBox">
						<RangeSlider :width="slideWidth" :height="slideHeight" :blockSize="slideBlockSize" :min="slideMin" :max="slideMax"
						 :values="rangeValues" :step="step" :liveMode="isLiveMode" :activeColor="'#25B4A5'" @rangechange="onRangeChange">
							<view slot="minBlock" class="range-slider-block"></view>
							<!-- 左边滑块的内容 -->
							<view slot="maxBlock" class="range-slider-block"></view>
							<!-- 右边滑块的内容 -->
						</RangeSlider>
					</view>
					<view style="height: 64rpx;">
						<text class="position-absolute left-4 text-gray-light">￥0</text>
						<text class="position-absolute right-4 text-gray-light">￥200+</text>
					</view>
				</view>

			</view>
			<text class="example-info mt-4">擅长领域</text>
			<view class="example-body">
				<view class="tag-view" v-for="(item,index) in expertlist" :key="index">
					<uni-tag :size="tagSize" :text="item.name" :circle="true" @click="setInverted(item,index)" :inverted="item.checked"
					 type="indigo" />
				</view>
			</view>
			<view class="uni-share-button-box">
				<button class="uni-share-button bg-main mb-4" @click="closeAction()">确定</button>
			</view>
		</view>
</template>

<script>
	import RangeSlider from '../../components/range-slider/range-slider.vue';
	export default {
		name: 'UniPopupShare',
		props: {
			localDic: {
				type: Object,
				default:  {
					areaCode: "310000",
					area_name: "上海"
				}
			},
			expertlist: Array,
			//点击遮罩层关闭弹窗

			//禁止页面滚动（H5生效）
			scroll: {
				type: Boolean,
				default: true
			},
		},
		components: {
			RangeSlider
		},
		data() {
			return {
				rangeValues: [10, 180],
				slideWidth: 320,
				slideHeight: 80,
				slideBlockSize: 50,
				slideMin: 0,
				slideMax: 200,
				isLiveMode: true,
				tagSize: "small",
				step: 1
			}
		},
		created() {
			uni.getSystemInfo({
				success: res => {
					this.slideWidth = res.windowWidth * 2 - 128;
					if (res.windowWidth > 320) {
						this.tagSize = "normal";
					}
					// console.log(res.windowWidth);
				},
			});
		},
		methods: {
			onTouchMove: function(event) {
				!this.scroll && event.preventDefault();
			},
			/**
			 * 选择内容
			 */
			select(item, index) {
				this.$emit('selectTag', {
					item,
					index
				}, () => {
					
				})
			},
			// 
			selectAddress() {
				uni.navigateTo({
					url: "../../pages/address-select/address-select"
				})
			},
			// 确认关闭窗口
			closeAction() {
				 this.$emit('showpopup',false);//select事件触发后，自动触发showCityName事件
			},
			onRangeChange: function(e) {
				this.rangeValues = [e.minValue, e.maxValue];

				console.log(this.rangeValues);
				console.log(JSON.stringify(e));
			},
			test: function() {
				this.rangeValues = [4.2, 6.6];
			},
			setInverted(item, index) {
				console.log(item);
				console.log(index);
				this.expertlist[index].checked = !item.checked;
			},
		}
	}
</script>
<style lang="scss" scoped>
	/*遮罩层*/
	.mask {
		position: fixed;
		z-index: 500;
		top: 0;
		right: 0;
		left: 0;
		bottom: 0;
		background: rgba(0, 0, 0, 0.5);
		transition: all 0.4s;
	}
	.uni-popup-share {
		background-color: #fff;
	}

	.uni-share-title {
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: row;
		align-items: center;
		justify-content: center;
		height: 40px;
	}

	.uni-share-title-text {
		font-size: 14px;
		color: #666;
	}

	.uni-share-content {
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: row;
		justify-content: center;
		padding-top: 10px;
	}

	.uni-share-content-box {
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: row;
		flex-wrap: wrap;
		width: 360px;
	}

	.uni-share-content-item {
		width: 90px;
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: column;
		justify-content: center;
		padding: 10px 0;
		align-items: center;
	}

	.uni-share-content-item:active {
		background-color: #f5f5f5;
	}

	.uni-share-image {
		width: 30px;
		height: 30px;
	}

	.uni-share-text {
		margin-top: 10px;
		font-size: 14px;
		color: #3B4144;
	}

	.uni-share-button-box {
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: row;
		padding: 10px 15px;
	}

	.uni-share-button {
		flex: 1;
		border-radius: 50px;
		color: #fff;
		font-size: 16px;
	}

	.uni-location-button {
		flex: 1;
		border-radius: 50px;
		color: #25B4A5;
		font-size: 16px;
		background-color: #F5F5F5;
	}

	.uni-share-button::after {
		border-radius: 50px;
	}

	.content {
		justify-content: center;
		flex-direction: column;
	}

	.sliderBox {
		justify-content: center;
		margin-right: 50rpx;
	}

	.text-center {
		justify-content: center;
	}

	.rowBox {
		flex-direction: row;
		align-items: center;
		justify-content: center;
	}

	.mrg10T {
		margin-top: 10rpx;
	}

	.tips {
		color: #999;
		font-size: 24rpx;
		text-align: center;
		margin-top: 100rpx;
	}

	.testBtn {
		margin-top: 50rpx;
	}

	.part {
		flex-direction: column;
		justify-content: center;
	}

	/* #ifndef APP-NVUE */
	page {
		display: flex;
		flex-direction: column;
		box-sizing: border-box;
		background-color: #efeff4;
		min-height: 100%;
		height: auto;
	}

	view {
		font-size: 14px;
		line-height: inherit;
	}

	.example-body {
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: row;
		flex-wrap: wrap;
		justify-content: center;
		padding: 0;
		font-size: 14px;
		background-color: #ffffff;
	}

	/* #endif */
	.example {
		padding: 0 15px;
	}

	.example-info {
		text-align: left;
		/* #ifndef APP-NVUE */
		display: block;
		/* #endif */
		padding: 0 15px;
		color: #3b4144;
		background-color: #ffffff;
		font-size: 14px;
		line-height: 20px;
	}

	.example-info-text {
		font-size: 14px;
		line-height: 20px;
		color: #3b4144;
	}

	.example-body {
		flex-direction: column;
		padding: 15px;
		background-color: #ffffff;
	}

	.word-btn-white {
		font-size: 18px;
		color: #FFFFFF;
	}

	.word-btn {
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: row;
		align-items: center;
		justify-content: center;
		border-radius: 6px;
		height: 48px;
		margin: 15px;
		background-color: #007AFF;
	}

	.word-btn--hover {
		background-color: #4ca2ff;
	}

	.example-body {
		/* #ifndef APP-PLUS-NVUE */
		display: flex;
		/* #endif */
		flex-direction: row;
		justify-content: flex-start;
		flex-wrap: wrap;
		padding: 20rpx;
	}

	.tag-view {
		/* #ifndef APP-PLUS-NVUE */
		display: flex;
		/* #endif */
		flex-direction: column;
		margin: 10rpx 15rpx;
		justify-content: center;
	}
</style>
