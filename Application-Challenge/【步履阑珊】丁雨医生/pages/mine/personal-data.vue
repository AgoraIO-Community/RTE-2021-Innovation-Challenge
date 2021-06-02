<template>
	<view class="page">
		<view class="flex align-center">
			<uni-list class="text-left m-2" :border="false">
				<uni-list-item title="头像" showArrow>
					<template slot="footer">
						<image class="slot-image" src="/static/icons/icon_assistanceavatar.png" mode="scaleToFill"></image>
					</template>
				</uni-list-item>
				<uni-list-item title="昵称" rightText="用户0369366" rightColor="black" showArrow>

				</uni-list-item>
				<uni-list-item title="性别" rightText="男" rightColor="black" showArrow>

				</uni-list-item>
				<uni-list-item title="生日" rightText="1986-01-10" rightColor="black" showArrow>

				</uni-list-item>
			</uni-list>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {

			}
		},
		methods: {

		}
	}
</script>

<style>
	page {
		display: flex;
		flex-direction: column;
		box-sizing: border-box;
		background-color: #ffffff;
		min-height: 100%;
		height: auto;
	}
	.slot-image {
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		width: 64rpx;
		height: 64rpx;
		border-radius: 32rpx;
	}
</style>
