<template>
	<view class="list-item-column" @click="open">
		<view class="list-item-row">
			<image :src="item.docavatar" class="list-item-avatar"></image>
			<view class="flex-column text-left m-16">
				<view class="hei-48"><text class="font-34 text-bold">{{item.docname}}</text>
					<text class="font-24 ml-20">{{item.doclevel}}</text>
				</view>
				<view><text
						class="font-24">{{item.dochospital}}&nbsp;&nbsp;|&nbsp;&nbsp;{{item.docpartment}}</text><text
						class="list-item-tag">三甲</text></view>
				<view class="list-item-docexpert mt-16">
					{{item.docexpert}}
				</view>
				<view class="list-item-docexpert">
					问诊量：{{item.docconsultation}}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;平均回复时间：{{item.docresponse}}分钟
				</view>
				<view class="list-item-footprice m-20">
					在线咨询 <text class="ml-10 text-through">￥{{item.docfootprice}}</text><text class="text-green">￥1</text>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		props: {
			item: Object,
			index: Number
		},
		data() {
			return {}
		},
		mounted() {

		},
		methods: {
			open() {
				uni.navigateTo({
					url: '../../pages/index/doc_detail/doc_detail?detailData='+JSON.stringify(this.item),
				});
			}
		},
	}
</script>

<style>
	.list-item-column {
		width: 96%;
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: column;
		margin: 16rpx;
		border-bottom-width: 4rpx;
		border-bottom-style: solid;
		border-bottom-color: #F7F8FA;
	}

	.list-item-row {
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: row;
	}

	.list-item-avatar {
		margin: 16rpx;
		width: 104rpx;
		min-width: 104rpx;
		height: 104rpx;
		border-radius: 64rpx;
	}
	
	.list-item-tag {
		border-radius: 8rpx;
		padding: 4rpx 16rpx;
		color: #FFFFFF;
		margin-left: 20rpx;
		font-size: 20rpx;;
		background-color: #25B4A5;
	}
	
	.list-item-docexpert {
		color: #9FA4AE;
		font-size: 24rpx;
	}
	
	.list-item-footprice {
		color: #535568;
		font-size: 28rpx;
		text-align: center;
		border-width: 2rpx;
		border-style: solid;
		float: right;
		border-color: #25B4A5;
		border-radius: 8rpx;
		width: 256rpx;
		padding: 2rpx 8rpx;
	}
</style>
