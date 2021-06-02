<template>
	<view class="p-2">
		<!-- 头像昵称 | 关注按钮 -->
		<view class="flex align-center justify-between">
			<view class="flex align-center">
				<!-- 头像 -->
				<image class="rounded-circle m-2" 
				:src="item.avatar" @click="openSpace(item)"
				style="width: 96rpx;height: 96rpx;" 
				lazy-load></image>
				<!-- 昵称 -->
				<view>
					<view class="font-md text-black text-bold ml-1" style="line-height: 1.5;">
						{{item.username}}
					</view>
				</view>
			</view>
			<!-- 按钮 -->
			<view @click.stop="follow" v-if="!item.isFollow"
			class="flex align-center justify-center rounded28 text-main border-main animated faster mr-2" 
			style="width: 120rpx;height: 56rpx;"
			hover-class="jello">
				+&nbsp;关注
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		props: {
			item: Object,
			position:Number
		},
		methods: {
			// 打开个人空间
			openSpace(detail) {
				uni.navigateTo({
					url: '../lawyer-homepage/lawyer-homepage?detailData=' + encodeURIComponent(JSON.stringify(detail))
				});
			},
			// 关注
			follow(){
				// 通知父组件
				this.$emit('follow',{
					index:this.index,
					position:this.position
				})
			},
			// 进入详情页
			openDetail(){
				console.log('进入详情页');
			},
		},
	}
</script>

<style>
	
</style>
