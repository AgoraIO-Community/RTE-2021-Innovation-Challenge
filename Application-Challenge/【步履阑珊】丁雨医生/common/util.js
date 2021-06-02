export default {
	// 监听网络
	onNetWork(){
		let func = (res)=>{
			if (res.networkType === 'none') {
				uni.showToast({
					title: '当前处于断网状态,请先连接',
					icon: 'none'
				});
			}
		}
		uni.getNetworkType({
		    success:func
		});
		uni.onNetworkStatusChange(func);
	},
	// 热更新
	update(){
		// #ifdef APP-PLUS  
		plus.runtime.getProperty(plus.runtime.appid, function(widgetInfo) {  
		    uni.request({  
		        url: 'https://ceshi.dishait.cn/api/v1/update',  
		        data: {  
		            version: widgetInfo.version,  
		            name: widgetInfo.name  
		        },  
		        success: (result) => {  
		            var data = result.data;  
		            if (data.update && data.wgtUrl) {  
		                uni.downloadFile({  
		                    url: data.wgtUrl,  
		                    success: (downloadResult) => {  
		                        if (downloadResult.statusCode === 200) {  
		                            plus.runtime.install(downloadResult.tempFilePath, {  
		                                force: false  
		                            }, function() {  
		                                console.log('install success...');  
		                                plus.runtime.restart();  
		                            }, function(e) {  
		                                console.error('install fail...');  
		                            });  
		                        }  
		                    }  
		                });  
		            }  
		        }  
		    });  
		});  
		// #endif
	},
	// 转化公共列表数据
	formatCommonList(v){
		return {
			id:v.id,
			user_id:v.user_id,
			username:v.user.username,
			userpic:v.user.userpic,
			newstime:v.create_time,
			title:v.title,
			titlepic:v.titlepic,
			isFollow:false,
			support:{
				type:"support", // 顶
				support_count:1,
				unsupport_count:2
			},
			comment_count:v.comment_count,
			share_num:v.sharenum
		}
	},
	// 数组置顶
	__toFirst(arr,index){
		if (index != 0) {
			arr.unshift(arr.splice(index,1)[0]);
		}
		return arr;
	},
	formatTime(time) {
		if (typeof time !== 'number' || time < 0) {
			return time
		}
	
		var hour = parseInt(time / 3600)
		time = time % 3600
		var minute = parseInt(time / 60)
		time = time % 60
		var second = time
	
		return ([hour, minute, second]).map(function (n) {
			n = n.toString()
			return n[1] ? n : '0' + n
		}).join(':')
	},
	formatLocation(longitude, latitude) {
		if (typeof longitude === 'string' && typeof latitude === 'string') {
			longitude = parseFloat(longitude)
			latitude = parseFloat(latitude)
		}
	
		longitude = longitude.toFixed(2)
		latitude = latitude.toFixed(2)
	
		return {
			longitude: longitude.toString().split('.'),
			latitude: latitude.toString().split('.')
		}
	}
}

export var dateUtils = {
	UNITS: {
		'年': 31557600000,
		'月': 2629800000,
		'天': 86400000,
		'小时': 3600000,
		'分钟': 60000,
		'秒': 1000
	},
	humanize: function (milliseconds) {
		var humanize = '';
		for (var key in this.UNITS) {
			if (milliseconds >= this.UNITS[key]) {
				humanize = Math.floor(milliseconds / this.UNITS[key]) + key + '前';
				break;
			}
		}
		return humanize || '刚刚';
	},
	format: function (dateStr) {
		var date = this.parse(dateStr)
		var diff = Date.now() - date.getTime();
		if (diff < this.UNITS['天']) {
			return this.humanize(diff);
		}
		var _format = function (number) {
			return (number < 10 ? ('0' + number) : number);
		};
		return date.getFullYear() + '/' + _format(date.getMonth() + 1) + '/' + _format(date.getDate()) + '-' +
			_format(date.getHours()) + ':' + _format(date.getMinutes());
	},
	parse: function (str) { //将"yyyy-mm-dd HH:MM:ss"格式的字符串，转化为一个Date对象
		var a = str.split(/[^0-9]/);
		return new Date(a[0], a[1] - 1, a[2], a[3], a[4], a[5]);
	}
};