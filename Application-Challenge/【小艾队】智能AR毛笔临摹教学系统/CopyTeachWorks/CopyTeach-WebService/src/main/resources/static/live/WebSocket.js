var websocket = null;

function wsUrl(uid) {
    return "wss://ct.joeallen.top/websocket/" + uid
    // return "ws://localhost:8888/websocket/" + uid
}

function initWebSocket(uid) {
//判断当前浏览器是否支持WebSocket, 主要此处要更换为自己的地址
    if ('WebSocket' in window) {
        websocket = new WebSocket(wsUrl(uid));
    } else {
        alert('浏览器不支持WebSocket')
    }


//连接发生错误的回调方法
    websocket.onerror = function () {
        print("WebSocket发生错误");
    };

//连接成功建立的回调方法
    websocket.onopen = function (event) {
        print("WebSocket连接成功" + event);
    }

//接收到消息的回调方法
    websocket.onmessage = function (event) {
        print(event.data);
    }

//连接关闭的回调方法
    websocket.onclose = function () {
        print("WebSocket断开");

        initWebSocket(options.uid)
    }

//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function () {
        closeWebSocket()
    }
    print("连接WebSocket")

}

//将消息显示在网页上
function print(str) {
    console.log(str)
}

//手动关闭连接
function closeWebSocket() {
    websocket.close();
}

//发送消息
function send(message) {
    websocket.send(message);
}