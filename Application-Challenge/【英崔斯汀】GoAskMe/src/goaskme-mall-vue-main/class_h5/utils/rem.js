window.addEventListener(
  "load",
  function() {
    var setRem = function() {
      // UI设计稿的宽度
      var uiWidth = 750;

      // 移动端屏幕宽度
      var winWidth = document.documentElement.clientWidth;

      // 比率
      var rate = winWidth / uiWidth;

      // 设置html元素的字体大小
      document.documentElement.style.fontSize = rate * 20 + "px";
    };

    setRem();

    window.onresize = function() {
      setRem();
    };
  },
  false
);
