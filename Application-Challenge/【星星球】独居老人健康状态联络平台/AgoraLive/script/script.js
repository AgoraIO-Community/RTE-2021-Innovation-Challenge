
var APPID = "4e8a365ea4854572a8dfeb4a4946db1e"
var token = "0064e8a365ea4854572a8dfeb4a4946db1eIADLhQ9oJhcxrCdVeRCAfrKV60GK0T12enROSPGbayDbpUOQEggAAAAAEADEZWnpKee1YAEAAQAT57Vg"
var Channel = "myChannel"
// 处理错误的函数
let handleError = function(err){
    console.log("Error: ", err);
};

// 定义远端视频画面的容器
let remoteContainer = document.getElementById("remote-container");

// 将视频流添加到远端视频画面容器的函数
function addVideoStream(elementId){
    // 给每个流创建一个 div
    let streamDiv = document.createElement("div");
    // 将 elementId 分配到 div
    streamDiv.id = elementId;
    // 处理镜像问题
    streamDiv.style.transform = "rotateY(180deg)";
    // 将 div 添加到容器
    remoteContainer.appendChild(streamDiv);
};

// 将视频流从远端视频画面容器移除的函数
function removeVideoStream(elementId) {
    let remoteDiv = document.getElementById(elementId);
    if (remoteDiv) remoteDiv.parentNode.removeChild(remoteDiv);
};

let client = AgoraRTC.createClient({
    mode: "live",
    codec: "vp8",
});

client.init(APPID, function() {
    console.log("client initialized");
}, function(err) {
    console.log("client init failed ", err);
});

// role 的值可以是 "host" 或者 "audience".
client.setClientRole("host");
// 加入频道
client.join(token, Channel, null, (uid)=>{
    // 创建本地媒体流
    const canvasStream = canvasLocal.captureStream(30)
    let localStream = AgoraRTC.createStream({
        audio: false,
        video: true,
        videoSource: canvasStream.getVideoTracks()[0], 
    }); 
    // 初始化本地流
    localStream.init(()=>{
        // 播放本地流
        localStream.play("me");
        // 发布本地流
        client.publish(localStream, handleError);
    }, handleError);
}, handleError);

// 有远端用户发布流时进行订阅
client.on("stream-added", function(evt){
    client.subscribe(evt.stream, handleError);
});
// 订阅成功后播放远端用户的流
client.on("stream-subscribed", function(evt){
    let stream = evt.stream;
    let streamId = String(stream.getId());
    addVideoStream(streamId);
    stream.play(streamId);
});

// 远端用户取消发布流时，关闭及移除对应的流。
client.on("stream-removed", function(evt){
    let stream = evt.stream;
    let streamId = String(stream.getId());
    stream.close();
    removeVideoStream(streamId);
});
// 远端用户离开频道时，关闭及移除对应的流。
client.on("peer-leave", function(evt){
    let stream = evt.stream;
    let streamId = String(stream.getId());
    stream.close();
    removeVideoStream(streamId);
});



