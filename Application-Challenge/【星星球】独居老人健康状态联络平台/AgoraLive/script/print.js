const canvasLocal = document.getElementById("canvas-video");
const context = canvasLocal.getContext("2d");

// show loading notice
context.fillStyle = "#333";
context.fillText("正在检测", canvasLocal.width / 2 - 30, canvasLocal.height / 3);

setInterval(() => {
  socket.send("update");
}, 100);
