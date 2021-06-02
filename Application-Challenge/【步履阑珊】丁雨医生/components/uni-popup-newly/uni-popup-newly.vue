<template>
	<view class="uni-popup-newly">
		<view v-if="false" class="uni-newly-title"><text class="uni-newly-title-text">{{title}}</text></view>
		<view class="uni-newly-content">
			<view class="uni-newly-content-box">
				<view class="uni-newly-content-item" v-for="(item,index) in bottomData" :key="index"
					@click.stop="select(item,index)">
					<image class="uni-newly-image" :src="item.icon" mode="aspectFill"></image>
					<text class="uni-newly-text">{{item.text}}</text>
				</view>

			</view>
		</view>
		<view class="uni-newly-button-box">
			<button class="uni-newly-button" @click="close">取消</button>
		</view>
	</view>
</template>

<script>
	export default {
		name: 'UniPopupShare',
		props: {
			title: {
				type: String,
				default: '分享到'
			}
		},
		inject: ['popup'],
		data() {
			return {
				bottomData: [{
						text: '新增采购订单',
						icon: '../../static/icons/icon_gouwuche.png',
						name: 'wechat'
					},
					{
						text: '新增采购入库单',
						icon: '../../static/icons/icon_caigouruku.png',
						name: 'wechat'
					},
					{
						text: '新增采购退货单',
						icon: '../../static/icons/icon_caigoutuihuo.png',
						name: 'wechat'
					},
					{
						text: '新增销售订单',
						icon: '../../static/icons/icon_xiaoshoudingdan.png',
						name: 'wechat'
					},
					{
						text: '新增销售出库单',
						icon: '../../static/icons/icon_xiaoshouchuku.png',
						name: 'wechat'
					},
					{
						text: '新增销售退货单',
						icon: '../../static/icons/icon_xiaoshoutuihuo.png',
						name: 'wechat'
					},
					{
						text: '新增盘点单',
						icon: '../../static/icons/icon_pandian.png',
						name: 'wechat'
					},
					{
						text: '新增其他入库单',
						icon: '../../static/icons/icon_rukudan.png',
						name: 'wechat'
					},
					{
						text: '新增其他出库单',
						icon: '../../static/icons/icon_chukudan.png',
						name: 'wechat'
					},
					{
						text: '新增调拨单',
						icon: '../../static/icons/icon_diaobo.png',
						name: 'wechat'
					}
				]
			}
		},
		created() {},
		methods: {
			/**
			 * 选择内容
			 */
			select(item, index) {
				this.$emit('select', {
					item,
					index
				}, () => {
					this.popup.close()
				})
			},
			/**
			 * 关闭窗口
			 */
			close() {
				this.popup.close()
			}
		}
	}
</script>
<style lang="scss" scoped>
	.uni-popup-newly {
		background-color: #fff;
	}

	.uni-newly-title {
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: row;
		align-items: center;
		justify-content: center;
		height: 40px;
	}

	.uni-newly-title-text {
		font-size: 14px;
		color: #666;
	}

	.uni-newly-content {
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: row;
		justify-content: center;
		padding-top: 10px;
	}

	.uni-newly-content-box {
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: row;
		flex-wrap: wrap;
		width: 360px;
	}

	.uni-newly-content-item {
		width: 120px;
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: column;
		justify-content: center;
		padding: 10px 0;
		align-items: center;
	}

	.uni-newly-content-item:active {
		background-color: #f5f5f5;
	}

	.uni-newly-image {
		width: 30px;
		height: 30px;
	}

	.uni-newly-text {
		margin-top: 10px;
		font-size: 14px;
		color: #3B4144;
	}

	.uni-newly-button-box {
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: row;
		padding: 10px 15px;
	}

	.uni-newly-button {
		flex: 1;
		border-radius: 50px;
		color: #666;
		font-size: 16px;
	}

	.uni-newly-button::after {
		border-radius: 50px;
	}
</style>
