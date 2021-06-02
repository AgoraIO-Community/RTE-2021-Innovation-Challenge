<template>
	<view class="flex align-center p-2" @click="open">
		<image :src="item.cover"
		style="width: 150rpx;height: 150rpx;"
		class="rounded mr-2"></image>
		<view class="flex flex-column flex-1">
			<text class="font-md text-dark">#{{item.title}}#</text>
			<text class="font text-secondary">{{item.desc}}</text>
			<view class="flex align-center font text-secondary">
				<text class="mr-2">动态：{{item.news_count}}</text>
				<text>今日：{{item.today_count}}</text>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		props: {
			item:Object,
			index:Number
		},
		methods: {
			open() {
				uni.navigateTo({
					url: '../../pages/topic-detail/topic-detail?detail='+JSON.stringify(this.item),
				});
			}
		},
	}
</script>

<style>
</style>
