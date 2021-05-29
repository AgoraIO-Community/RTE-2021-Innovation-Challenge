var help_state=0;
$(document).ready(function () {
    var calling_audio_ele=document.getElementById("calling_audio");
    //选择内容
    $(".info_list .list").on("touchstart",function(){
        $(".info_list .list").removeClass("active");
        $(this).addClass("active");
    });

    //提交补充地址
    $("#sub_address").on("touchstart",function(){
        if($("#more_address").val()==""){
            Toast("请输入地址后提交");
            return;
        }
        Toast("提交成功");
    });

    //呼叫
    $("#get_help_call").on("touchstart",function(){
        if(help_state==0){
            //呼叫
            dial();
            return;
        }
        if(help_state>0){
            hang_up();
            return;
        }
    });

    //拨号
    function dial() {
        help_state=1;
        $(".call_box").addClass("calling");
        $(".tip").html("单击取消");
        $(".info").html("正在拨号…");
        calling_audio_ele.play();
        connect(function(){
            $(".info").html("等待接听…");
        },function () {
            help_state=2;
            $(".call_box").removeClass("calling");
            $(".call_box").addClass("called");
            calling_audio_ele.pause();
            $(".tip").html("单击挂断");
            $(".info").html("正在通话…");
        });
    }

    //挂断
    function hang_up() {
        help_state=0;
        $(".call_box").removeClass("calling");
        $(".call_box").removeClass("called");
        $(".tip").html("单击呼叫救援");
        leave();
        calling_audio_ele.pause();
    }

    show_time();
    function show_time() {
        setInterval(function () {
            $(".time").html(get_now_time());
        }, 1000);
    }
    function get_now_time() {
        var today = new Date();
        var h = today.getHours();
        var m = today.getMinutes();
        var s = today.getSeconds();
        // 在 numbers<10 的数字前加上 0
        h = checkTime(h);
        m = checkTime(m);
        s = checkTime(s);
        return h + ":" + m + ":" + s;
    }
    function checkTime(i) {
        if (i < 10) {
            i = "0" + i;
        }
        return i;
    }

    //接入声网接口
    var rtc,options;
    function connect(success_callback,subscribe_callback){
        $.ajax({
            url: "api.php",
            data: "",
            type: "GET",
            dataType: "json",
            success: function (res) {
                if(res.errcode!=200){
                    Toast(res.info,1500);
                    hang_up();
                    return;
                }

                rtc = {
                    // 用来放置本地客户端。
                    client: null,
                    // 用来放置本地音频轨道对象。
                    localAudioTrack: null,
                };

                options = {
                    // 替换成你自己项目的 App ID。
                    appId: res.data.appid,
                    // 传入目标频道名。
                    channel: res.data.channel,
                    // 如果你的项目开启了 App 证书进行 Token 鉴权，这里填写生成的 Token 值。
                    token: res.data.token,
                };

                async function startBasicCall() {
                    //创建本地客户端
                    rtc.client = AgoraRTC.createClient({ mode: "rtc", codec: "vp8" });
                    //加入频道
                    const uid = await rtc.client.join(options.appId, options.channel, options.token, null);
                    //创建并发布本地音频轨道
                    rtc.localAudioTrack = await AgoraRTC.createMicrophoneAudioTrack();
                    //发布本地音频轨道
                    await rtc.client.publish([rtc.localAudioTrack]);
                    console.log("发布本地音频轨道成功");
                    success_callback();

                    //订阅远端用户
                    rtc.client.on("user-published", async (user, mediaType) => {
                        console.log("开始订阅远端用户");
                        // 开始订阅远端用户
                        await rtc.client.subscribe(user, mediaType);
                        console.log("订阅远端用户成功");
                        subscribe_callback();

                        //音频处理
                        if (mediaType === "audio") {
                            //订阅完成后，从 `user` 中获取远端音频轨道对象。
                            const remoteAudioTrack = user.audioTrack;
                            //播放音频因为不会有画面，不需要提供 DOM 元素的信息。
                            console.log("播放音频");
                            remoteAudioTrack.play();
                        }
                    });
                }

                startBasicCall();
            },
            error: function (e) {
                console.log(e);
            }
        });
    }

    //离开频道
    async function leave() {
        //销毁本地音频轨道
        rtc.localAudioTrack.close();

        //离开频道
        await rtc.client.leave();
    }
});

function Toast(msg,duration){
    $("#myToast").remove();
    duration=isNaN(duration)?800:duration;
    var m = document.createElement('div');
    m.id="myToast";
    m.innerHTML = msg;
    m.style.cssText="max-width:60%;min-width: 150px;padding:0 14px;height: 40px;color: rgb(255, 255, 255);line-height: 40px;text-align: center;border-radius: 4px;position: fixed;top: 50%;left: 50%;transform: translate(-50%, -50%);z-index: 999999;background: rgba(0, 0, 0,.7);font-size: 16px;";
    document.body.appendChild(m);
    setTimeout(function() {
        var d = 0.5;
        m.style.webkitTransition = '-webkit-transform ' + d + 's ease-in, opacity ' + d + 's ease-in';
        m.style.opacity = '0';
        setTimeout(function() { document.body.removeChild(m) }, d * 1000);
    }, duration);
}

