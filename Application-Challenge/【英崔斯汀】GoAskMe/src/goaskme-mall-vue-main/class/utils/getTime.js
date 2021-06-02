export function getTime() {
  var timezone = 8; //目标时区时间，东八区   东时区正数 西市区负数
  var offset_GMT = new Date().getTimezoneOffset(); // 本地时间和格林威治的时间差，单位为分钟
  var nowDate = new Date().getTime(); // 本地时间距 1970 年 1 月 1 日午夜（GMT 时间）之间的毫秒数
  var targetDate = nowDate + offset_GMT * 60 * 1000 + timezone * 60 * 60 * 1000;
  var date = new Date(targetDate);
  var seperator1 = "-";
  var seperator2 = ":";
  var year = date.getFullYear();
  var month = date.getMonth() + 1;
  var strDate = date.getDate();
  var hour = date.getHours(); //获取当前小时数(0-23)
  var minutes = date.getMinutes(); //获取当前分钟数(0-59)
  var second = date.getSeconds(); //获取当前秒数(0-59)
  if (month >= 1 && month <= 9) {
    month = "0" + month;
  }
  if (strDate >= 0 && strDate <= 9) {
    strDate = "0" + strDate;
  }
  if (hour >= 0 && hour <= 9) {
    hour = "0" + hour;
  }
  if (minutes >= 0 && minutes <= 9) {
    minutes = "0" + minutes;
  }
  if (second >= 0 && second <= 9) {
    second = "0" + second;
  }
  var currentdate =
    month +
    seperator1 +
    strDate +
    " " +
    hour +
    seperator2 +
    minutes +
    seperator2 +
    second;
  return currentdate;
}
