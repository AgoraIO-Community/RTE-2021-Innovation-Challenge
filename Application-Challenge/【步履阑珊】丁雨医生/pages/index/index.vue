<template>
	<uni-indexed-list :options="list" :showSelect="false" @click="bindClick"></uni-indexed-list>
</template>

<script>
	export default {
		data() {
			return {
				list: [{
					"letter": "Basic",
					"data": [
						"JoinChannelAudio",
						"JoinChannelVideo",
						"StringUid"
					]
				}, {
					"letter": "Advanced",
					"data": [
						"MultiChannel"
					]
				}]
			}
		},
		onLoad() {

		},
		methods: {
			bindClick(e) {
				uni.navigateTo({
					url: `../${e.item.key}/${e.item.name}`
				})
			}
		}
	}
</script>
