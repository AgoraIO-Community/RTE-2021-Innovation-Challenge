const verdictTime = function(thetime) {
  var d = new Date(Date.parse(thetime.replace(/-/g, "/")));
  var timezone = 8; //目标时区时间，东八区   东时区正数 西市区负数
  var offset_GMT = new Date().getTimezoneOffset(); // 本地时间和格林威治的时间差，单位为分钟
  var nowDate = new Date().getTime(); // 本地时间距 1970 年 1 月 1 日午夜（GMT 时间）之间的毫秒数
  var targetDate = nowDate + offset_GMT * 60 * 1000 + timezone * 60 * 60 * 1000; //东八度时间戳
  if (d <= targetDate) {
    //传入时间小于当前东八区时间戳
    return true;
  } else {
    // 传入时间大于当前东八区时间;
    return false;
  }
};
export default verdictTime;
