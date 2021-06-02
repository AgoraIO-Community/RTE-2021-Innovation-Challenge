<template>
	<view class="page">
		<view class="search-header">
			<uni-search-bar @confirm="search" @input="input" @cancel="cancel" class="search-body" bgColor="#F7F8FA" />
		</view>
		<view class="uni-list margintop-bar">
			<block v-for="(value, index) in lawyerlist" :key="index">
				<main-item :item="value" :index="index"></main-item>
			</block>
		</view>
		<uni-load-more :status="status" :icon-size="16" :content-text="contentText" />
	</view>
</template>

<script>
	// 测试数据
	const lawyerdemo = [{
			docname: "王娟",
			docavatar: "/static/icons/icon-avatarimg.png",
			doclevel: "主治医师",
			dochospital: "上海市第一人民医院",
			docpartment: "耳鼻喉科",
			doctag: "三甲",
			docexpert: "擅长：过敏性鼻炎；慢性鼻炎；过敏性鼻炎，慢性鼻炎，过敏性鼻炎，慢性鼻炎，过敏性鼻炎，慢性炎；",
			docconsultation: 945,
			docresponse: 45,
			docfootprice: 35
		},
		{
			docname: "王娟",
			docavatar: "/static/icons/icon-doctarimg.png",
			doclevel: "主治医师",
			dochospital: "上海市第一人民医院",
			docpartment: "耳鼻喉科",
			doctag: "三甲",
			docexpert: "擅长：过敏性鼻炎；慢性鼻炎；过敏性鼻炎，慢性鼻炎，过敏性鼻炎，慢性鼻炎，过敏性鼻炎，慢性炎；",
			docconsultation: 945,
			docresponse: 45,
			docfootprice: 35
		},
		{
			docname: "王娟",
			docavatar: "/static/icons/icon_marshalling.png",
			doclevel: "主治医师",
			dochospital: "上海市第一人民医院",
			docpartment: "耳鼻喉科",
			doctag: "三甲",
			docexpert: "擅长：过敏性鼻炎；慢性鼻炎；过敏性鼻炎，慢性鼻炎，过敏性鼻炎，慢性鼻炎，过敏性鼻炎，慢性炎；",
			docconsultation: 945,
			docresponse: 45,
			docfootprice: 35
		}
	];
	import mainItem from '@/components/news/main-item.vue';
	export default {
		components: {
			mainItem
		},
		data() {
			return {
				titleString: "医生列表",
				searchVal: '',
				lawyerlist: [],
				reload: false,
				status: 'more',
				contentText: {
					contentdown: '上拉加载更多',
					contentrefresh: '加载中',
					contentnomore: '没有更多'
				}
			}
		},
		onLoad(e) {
			this.lawyerlist = [...lawyerdemo, ...lawyerdemo];
		},
		onPullDownRefresh() {
			this.reload = true;
			this.getList();
		},
		onReachBottom() {
			this.status = 'more';
			this.getList();
		},
		methods: {
			search(res) {
				uni.showToast({
					title: '搜索：' + res.value,
					icon: 'none'
				})
			},
			input(res) {
				this.searchVal = res.value
			},
			cancel(res) {
				uni.showToast({
					title: '点击取消，输入值为：' + res.value,
					icon: 'none'
				})
			},
			getList() {
				if(this.reload){
					this.lawyerlist = lawyerdemo;
					this.reload = false;
					uni.stopPullDownRefresh();
				}else{
					if (this.lawyerlist.length < 10) {
						uni.showToast({
							title: "loading",
							icon: "loading",
							duration: 2000
						})
						this.lawyerlist = this.lawyerlist.concat(lawyerdemo);
						// this.lawyerlist = [...this.lawyerlist, ...lawyerdemo];
					} else {
						uni.showModal({
						    title: '提示',
							showCancel: false,
						    content: '没有更多医生',
						    success: function (res) {
						        if (res.confirm) {
						            console.log('用户点击确定');
						        } else if (res.cancel) {
						            console.log('用户点击取消');
						        }
						    }
						});
					}
				}
			}
		},
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

	.search-header {
		width: 100%;
		position: fixed;
		z-index: 996;
		padding:0rpx 4rpx;
		font-size: 28rpx;
		background-color: #F7F8FA;
	}

	.search-body {
		margin: 4rpx;
		background-color: #F7F8FA;
	}

	.margintop-bar {
		margin-top: 110rpx;
	}
</style>
