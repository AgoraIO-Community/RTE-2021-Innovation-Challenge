<template>
	<view class="grid-item-box border-bottom-gray-light" style="background-color: #fff;" @click="open">
		<image :src="item.picurl" class="image rounded16" mode="aspectFill" :style="'width:'+gridWidth+'px;height:'+itemHeight+'px;'"/>
		<view class="grid-bottom-box">
			<view class="m-2 iconfont iconchangyongicon-"><text class="font-sm">{{item.view_count}}</text></view>
			<text class="m-2">{{item.newstime}}</text>
		</view>
		<text class="font-sm mx-2 mt-1 text-left">{{ item.title }}</text>
	</view>
</template>

<script>
	export default {
		props: {
			item:Object,
			index:Number,
			gridWidth:Number
		},
		data() {
			return {
				itemWidth: 100,
				itemHeight: 120
				
			}
		},
		mounted() {
			this.itemWidth = this.gridWidth;
			this.itemHeight = this.gridWidth/4*5;
		},
		methods: {
			open() {
				uni.navigateTo({
					// url: '../../pages/topic-detail/topic-detail?detail='+JSON.stringify(this.item),
				});
			}
		},
	}
</script>

<style>
	.grid-item-box {
		flex: 1;
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 16rpx;
	}
	
	.grid-bottom-box {
		width: 100%;
		bottom: 100rpx;
		position: absolute;
		color: white;
		display: flex;
		flex-wrap: wrap;
		justify-content: space-around;
		align-items: center;
		font-size: 25rpx;
	}
</style>
