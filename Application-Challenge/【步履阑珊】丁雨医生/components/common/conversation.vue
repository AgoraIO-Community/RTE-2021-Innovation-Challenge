<template>
	<view class="p-2 animated fast fadeIn">
		<!-- 头像昵称 | 关注按钮 -->
		<view class="flex align-center justify-between">
			<view class="flex align-center">
				<!-- 头像 -->
				<image class="rounded-circle mr-2 ml-1" 
				:src="item.avatar" @click="openSpace"
				style="width: 74rpx;height: 74rpx;" 
				lazy-load></image>
				<!-- 昵称发布时间 -->
				<view>
					<view class="font text-left mt-2" style="line-height: 1.2;">
						{{item.username}}
					</view>
					<text class="font-sm text-light-muted" 
					style="line-height: 1.2;">
						{{item.newstime}} · {{item.location}}
					</text>
				</view>
			</view>
		</view>
		<!-- 标题 -->
		<view class="font text-left my-1 ml-100 mr-2" @click="openDetail">{{item.title}}</view>
		<!-- 帖子详情 -->
		<slot>
			<!-- 图片 -->
			<image v-if="item.picurl" :src="item.picurl" @click="openDetail" 
			style="height: 350rpx;" class="rounded w-82 ml-8" mode="aspectFill" ></image>
		</slot>
	</view>
</template>

<script>
	export default {
		props: {
			item: Object,
			index:Number
		},
		filters: {
			//参数过滤器
		},
		methods: {
			// 打开个人空间
			openSpace() {
				
			},
			// 进入详情页
			openDetail(){
				// 处于详情中
			},
		},
	}
</script>

<style>
	
</style>
