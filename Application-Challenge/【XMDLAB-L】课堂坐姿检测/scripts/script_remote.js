// 处理错误的函数
let handleError = function(err){
    console.log("Error: ", err);
};

// 定义远端视频画面的容器
let remoteContainer = document.getElementById("remote-container");
console.log(" initialized");

// 将视频流添加到远端视频画面容器的函数
function addVideoStream(elementId){
    // 给每个流创建一个 div
    let streamDiv = document.createElement("div");
    // 将 elementId 分配到 div
    streamDiv.id = elementId;
    // 处理镜像问题
    //streamDiv.style.transform = "rotateY(180deg)";
    // 将 div 添加到容器
    remoteContainer.appendChild(streamDiv);
};

// 将视频流从远端视频画面容器移除的函数
function removeVideoStream(elementId) {
    let remoteDiv = document.getElementById(elementId);
    if (remoteDiv) remoteDiv.parentNode.removeChild(remoteDiv);
};

let client = AgoraRTC.createClient({
    mode: "rtc",
    codec: "vp8",
});

client.init("c24e75ca7769414cbc173f999914ed18", function() {
    console.log("client initialized");
}, function(err) {
    console.log("client init failed ", err);
});

// 加入频道
client.join("006c24e75ca7769414cbc173f999914ed18IADsgubpoGmDgBQS+oWN7DYgi4tTvVK3RAxvsA/Wuj5/N0OQEggAAAAAEAA8g5F5I7GxYAEAAQATsbFg", "myChannel", null, (uid)=>{
    // 创建本地媒体流
    let localStream = AgoraRTC.createStream({
        audio: true,
        video: true,
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
