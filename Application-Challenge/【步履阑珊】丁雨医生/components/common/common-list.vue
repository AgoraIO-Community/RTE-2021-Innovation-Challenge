<template>
	<view class="p-2 animated fast fadeIn">
		<!-- 头像昵称 | 关注按钮 -->
		<view class="flex align-center justify-between">
			<view class="flex align-center">
				<!-- 头像 -->
				<image class="rounded-circle mr-2" 
				:src="item.userpic" @click="openSpace"
				style="width: 65rpx;height: 65rpx;" 
				lazy-load></image>
				<!-- 昵称发布时间 -->
				<view>
					<view class="font" style="line-height: 1.5;">
						{{item.username}}
					</view>
					<text class="font-sm text-light-muted" 
					style="line-height: 1.5;">
						{{item.newstime|formatTime}}
					</text>
				</view>
			</view>
			<!-- 按钮 -->
			<view @click="follow" v-if="!item.isFollow"
			class="flex align-center justify-center rounded bg-main text-white animated faster" 
			style="width: 90rpx;height: 50rpx;"
			hover-class="jello">
				关注
			</view>
		</view>
		<!-- 标题 -->
		<view class="font-md my-1" @click="openDetail">{{item.title}}</view>
		<!-- 帖子详情 -->
		<slot>
			<!-- 图片 -->
			<image v-if="item.titlepic" :src="item.titlepic" @click="openDetail" 
			style="height: 350rpx;" class="rounded w-100"></image>
		</slot>
		<!-- 图标按钮 -->
		<view class="flex align-center">
			<!-- 顶 -->
			<view class="flex align-center justify-center flex-1 animated faster"
			hover-class="jello text-main" @click="doSupport('support')"
			:class="item.support.type === 'support' ? 'support-active' : ''">
				<text class="iconfont icon-dianzan2 mr-2"></text>
				<text>{{item.support.support_count > 0 ? item.support.support_count : '支持'}}</text>
			</view>
			<!-- 踩 -->
			<view class="flex align-center justify-center flex-1 animated faster"
			hover-class="jello text-main" @click="doSupport('unsupport')"
			:class="item.support.type === 'unsupport' ? 'support-active' : ''">
				<text class="iconfont icon-cai mr-2"></text>
				<text>{{item.support.unsupport_count > 0 ? item.support.unsupport_count : '反对'}}</text>
			</view>
			<view class="flex align-center justify-center flex-1 animated faster"
			hover-class="jello text-main" @click="doComment">
				<text class="iconfont icon-pinglun2 mr-2"></text>
				<text>{{item.comment_count > 0 ? item.comment_count : '评论'}}</text>
			</view>
			<view class="flex align-center justify-center flex-1 animated faster"
			hover-class="jello text-main" @click="doShare">
				<text class="iconfont icon-fenxiang mr-2"></text>
				<text>{{item.share_num > 0 ? item.share_num : '分享'}}</text>
			</view>
		</view>
	</view>
</template>

<script>
	import $T from '@/common/time.js';
	export default {
		props: {
			item: Object,
			index:{
				type:Number,
				default:-1
			},
			isdetail:{
				type:Boolean,
				default:false
			}
		},
		filters: {
			formatTime(value) {
				return $T.gettime(value);
			}
		},
		methods: {
			// 打开个人空间
			openSpace() {
				uni.navigateTo({
					url: '/pages/user-space/user-space',
				});
			},
			// 关注
			follow(){
				this.checkAuth(()=>{
					// 通知父组件
					this.$emit('follow',this.index)
				})
			},
			// 进入详情页
			openDetail(){
				// 处于详情中
				if (this.isdetail) return;
				uni.navigateTo({
					url: '../../pages/detail/detail?detail='+JSON.stringify(this.item),
				});
			},
			// 顶踩操作
			doSupport(type){
				this.checkAuth(()=>{
					// 通知父组件
					this.$emit('doSupport',{
						type:type,
						index:this.index
					})
				})
			},
			// 评论
			doComment(){
				this.checkAuth(()=>{
					if (!this.isdetail) {
						return this.openDetail()
					}
					this.$emit('doComment')
				})
			},
			// 分享
			doShare(){
				if (!this.isdetail) {
					return this.openDetail()
				}
				this.$emit('doShare')
			}
		},
	}
</script>

<style>
	.support-active{
		color: #FF4A6A;
	}
</style>
