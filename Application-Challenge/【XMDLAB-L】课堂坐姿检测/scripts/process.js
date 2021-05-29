const canvasLocal = document.getElementById("canvas-video");
const context = canvasLocal.getContext("2d");

// show loading notice
context.fillStyle = "#333";
context.fillText("Loading...", canvasLocal.width / 2 - 30, canvasLocal.height / 3);

setInterval(() => {
  socket.send("update");
}, 100);
