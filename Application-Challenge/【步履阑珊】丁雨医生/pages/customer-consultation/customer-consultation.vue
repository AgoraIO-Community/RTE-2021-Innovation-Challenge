<template>
	<view class="page">
		<scroll-view scroll-y="true" :style="'height:'+scrollH+'px;'" @scrolltolower="loadmoreEvent">
			<block v-for="(item,index) in consultlist" :key="index">
				<consultation-item :item="item" :index="index" @click="openDetail(item)"></consultation-item>
			</block>
			<load-more :loadmore="loadmore"></load-more>
		</scroll-view>
	</view>
</template>

<script>
	import consultationItem from '@/components/consultation/consultation-item.vue';
	import loadMore from '@/components/common/load-more.vue';
	const consultdemo = [{
			docname: "张清北",
			docavatar: "/static/icons/icon-avatarimg.png",
			doclevel: "主治医师",
			dochospital: "上海市第一人民医院",
			docpartment: "消化内科",
			consultag: "图文",
			consultype:1,//问诊类型：1,图文,2,音频,3,视频
			consulstate: 1,//问诊状态：1,待接诊,2,咨询中,3,已完成
			consulnote: "待接诊",
			consultitle: "主诉：最近总是腹泻 腹泻时肚子疼肚子疼",
			consultime: "2017-09-02 10:33",
		},
		{
			docname: "张清北",
			docavatar: "/static/icons/icon-doctarimg.png",
			doclevel: "主治医师",
			dochospital: "上海市第一人民医院",
			docpartment: "消化内科",
			consultag: "音频",
			consultype:2,//问诊类型：1,图文,2,音频,3,视频
			consulstate: 2,//问诊状态：1,待接诊,2,咨询中,3,已完成
			consulnote: "咨询中",
			consultitle: "主诉：最近总是腹泻 腹泻时肚子疼肚子疼",
			consultime: "2017-09-02 10:33",
		},
		{
			docname: "张清北",
			docavatar: "/static/icons/icon_marshalling.png",
			doclevel: "主治医师",
			dochospital: "上海市第一人民医院",
			docpartment: "消化内科",
			consultag: "图文",
			consultype:1,//问诊类型：1,图文,2,音频,3,视频
			consulstate: 3,//问诊状态：1,待接诊,2,咨询中,3,已完成
			consulnote: "已完成",
			consultitle: "主诉：最近总是腹泻 腹泻时肚子疼肚子疼",
			consultime: "2017-09-02 10:33",
		},
		{
			docname: "张清北",
			docavatar: "/static/icons/icon_marshalling.png",
			doclevel: "主治医师",
			dochospital: "上海市第一人民医院",
			docpartment: "消化内科",
			consultag: "视频",
			consultype:3,//问诊类型：1,图文,2,音频,3,视频
			consulstate: 4,//问诊状态：1,待接诊,2,咨询中,3,已完成,4,已取消
			consulnote: "已完成",
			consultitle: "主诉：最近总是腹泻 腹泻时肚子疼肚子疼肚子疼",
			consultime: "2017-09-02 10:33",
		}
	];
	export default {
		components: {
			consultationItem,
			loadMore
		},
		data() {
			return {
				scrollH: 500,
				consultlist: [],
				loadmore: "上拉加载更多",
			}
		},
		onLoad(e) {
			// 加载测试数据
			uni.getSystemInfo({
				success:res=>{
					this.scrollH = res.windowHeight;
				}
			});
			this.consultlist = [...consultdemo, ...consultdemo];
		},
		methods: {
			openDetail(item) {
				console.log(item.consultag);
			},
			// 上拉加载
			loadmoreEvent() {
				// 验证当前是否处于可加载状态
				if (this.loadmore !== '上拉加载更多') return;
				// 设置加载状态
				this.loadmore = '加载中...'
				// 模拟请求数据
				setTimeout(() => {
					// 加载数据
					this.consultlist = [...consultdemo, ...consultdemo]
					// 设置加载状态
					this.loadmore = '上拉加载更多'
				}, 2000)
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
</style>
