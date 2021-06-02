<template>
	<view>
		<view class="flex align-center">
			<uni-list class="text-left" :border="true">
				<uni-list-item leftText="病情描述">
					<view slot="body">
						<input class="uni-input" name="nickname" v-model="nickname"  maxlength="5" placeholder="请填写真实姓名" />
					</view>
				</uni-list-item>
				<uni-list-item leftText="患者身份证号">
					<view slot="body">
						<input type="number" class="uni-input" name="passport"  maxlength="20" v-model="passport"
							placeholder="请填写身份证号" />
					</view>
				</uni-list-item>
				<uni-list-item leftText="性别">
					<view slot="body">
						<view class="tag-view">
							<uni-tag class="mx-20" text="男" @click="doGender(true)"
								:type="genderd ? 'indigo' : 'default'" />
							<uni-tag class="piccon-tag" text="女" @click="doGender(false)"
								:type="genderd ? 'default' : 'indigo'" />
						</view>
					</view>
				</uni-list-item>
				<uni-list-item leftText="出生日期">
					<view slot="body">
						<picker mode="date" :value="birthday" :start="startDate" :end="endDate" @change="bindDateChange">
							<view class="uni-input">{{birthday}}</view>
						</picker>
					</view>
				</uni-list-item>
				<uni-list-item leftText="体重">
					<view slot="body">
						<input type="number" class="uni-input" name="weight" maxlength="5" v-model="weight" placeholder="请输入体重(kg)" />
					</view>
				</uni-list-item>
				<uni-list-item leftText="过敏史">
					<view slot="body">
						<view class="tag-view">
							<uni-tag class="mx-20" text="无" @click="doHeal(true)"
								:type="heal0 ? 'indigo' : 'default'" />
							<uni-tag class="piccon-tag" text="有" @click="doHeal(false)"
								:type="heal0 ? 'default' : 'indigo'" />
						</view>
					</view>
				</uni-list-item>
				<uni-list-item leftText="过往病史">
					<view slot="body">
						<view class="tag-view">
							<uni-tag class="mx-20" text="无" @click="doHeal1(true)"
								:type="heal1 ? 'indigo' : 'default'" />
							<uni-tag class="piccon-tag" text="有" @click="doHeal1(false)"
								:type="heal1 ? 'default' : 'indigo'" />
						</view>
					</view>
				</uni-list-item>
				<uni-list-item leftText="肝功能">
					<view slot="body">
						<view class="tag-view">
							<uni-tag class="mx-20" text="正常" @click="doHeal2(true)"
								:type="heal2 ? 'indigo' : 'default'" />
							<uni-tag class="piccon-tag" text="异常" @click="doHeal2(false)"
								:type="heal2 ? 'default' : 'indigo'" />
						</view>
					</view>
				</uni-list-item>
				<uni-list-item leftText="肾功能">
					<view slot="body">
						<view class="tag-view">
							<uni-tag class="mx-20" text="正常" @click="doHeal3(true)"
								:type="heal3 ? 'indigo' : 'default'" />
							<uni-tag class="piccon-tag" text="异常" @click="doHeal3(false)"
								:type="heal3 ? 'default' : 'indigo'" />
						</view>
					</view>
				</uni-list-item>
			</uni-list>
		</view>
		<view class="uni-flex mt-20">
			<button type="primary" class="piccon_button" @click="picodoneAction"><text
					class="font-md">提交</text></button>
		</view>
	</view>
</template>

<script>
	function getDate(type) {
		const date = new Date();

		let year = date.getFullYear();
		let month = date.getMonth() + 1;
		let day = date.getDate();

		if (type === 'start') {
			year = year - 60;
		} else if (type === 'end') {
			year = year + 2;
		}
		month = month > 9 ? month : '0' + month;;
		day = day > 9 ? day : '0' + day;

		return `${year}-${month}-${day}`;
	}
	export default {
		data() {
			return {
				nickname:"",
				passport:"",
				weight:"",
				genderd: true,
				heal0: true,
				heal1: true,
				heal2: true,
				heal3: true,
				birthday: getDate({
					format: true
				}),
				startDate:getDate('start'),
				endDate:getDate('end'),
			}
		},
		methods: {
			// 性别选择
			doGender(gender) {
				this.genderd = gender;
			},
			doHeal(heal) {
				this.heal0 = heal;
			},
			doHeal1(heal) {
				this.heal1 = heal;
			},
			doHeal2(heal) {
				this.heal2 = heal;
			},
			doHeal3(heal) {
				this.heal3 = heal;
			},
			bindDateChange: function(e) {
				this.birthday = e.detail.value
			},
			picodoneAction: function() {
				let genders=this.genderd?"男":"女";
				let heals0=this.heal0?"无过敏史":"有过敏史";
				let heals1=this.heal1?"无过往病史":"有过往病史";
				let heals2=this.heal2?"肝功能正常":"肝功能异常";
				let heals3=this.heal3?"肾功能正常":"肾功能异常";
				let content1 = "患者身份证号:"+this.passport+"\n性别:"+genders+"\n出生日期:"+this.birthday+"\n体重:"+this.weight+"KG\n";
				let content2 = heals0+"\n"+heals1+"\n"+heals2+"\n"+heals3+"\n";
				uni.showModal({
				    title: this.nickname,
					showCancel: false,
				    content: content1+content2,
				    success: function (res) {
				        if (res.confirm) {
				            console.log('用户点击确定');
				        } else if (res.cancel) {
				            console.log('用户点击取消');
				        }
				    }
				});
			},
		}
	}
</script>

<style>
	page {
		display: flex;
		flex-direction: column;
		box-sizing: border-box;
		background-color: #FFFFFF;
		min-height: 100%;
		height: auto;
	}

	.tag-view {
		/* #ifndef APP-PLUS-NVUE */
		display: flex;
		/* #endif */
		flex-direction: row;
		justify-content: flex-start;
		align-items: flex-start;
	}

	.piccon-tag {
		margin-left: 20rpx;
		margin-right: 20rpx;
	}

	.piccon_button {
		width: 92%;
		padding: 0 72rpx;
		margin: 64rpx 32rpx;
		font-size: 48rpx;
		line-height: 1.8;
		border-radius: 8rpx;
		border-width: 0rpx;
		background-image: linear-gradient(90deg, #07C193, #3EAAB4);
	}
</style>
