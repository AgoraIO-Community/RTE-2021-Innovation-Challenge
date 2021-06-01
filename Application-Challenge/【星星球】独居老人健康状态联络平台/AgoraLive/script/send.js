const wsurl = "ws://127.0.0.1:9999/send-frame";
const socket = new WebSocket(wsurl);
const img = new Image();

function sendMsg() {
  socket.send("update");
  console.log("socket: send update");
}
function Uint8ToString(u8a) {
  var CHUNK_SZ = 0x8000;
  var c = [];
  for (var i = 0; i < u8a.length; i += CHUNK_SZ) {
    c.push(String.fromCharCode(...u8a.subarray(i, i + CHUNK_SZ)));
  }
  return c.join("");
}
function drawFrame(frame) {
  var uint8Arr = new Uint8Array(frame);
  var str = Uint8ToString(uint8Arr);
  var base64String = btoa(str);

  img.onload = function () {
    context.drawImage(this, 0, 0, canvasLocal.width, canvasLocal.height);
  };
  img.src = "data:image/png;base64," + base64String;
}

socket.onopen = () => {
  console.log("socket: connected");
};
socket.onmessage = (msg) => {
  msg.data.arrayBuffer().then((buffer) => {
    //socket.send(buffer)
    drawFrame(buffer);
    console.log("socket: frame updated");
  });
};