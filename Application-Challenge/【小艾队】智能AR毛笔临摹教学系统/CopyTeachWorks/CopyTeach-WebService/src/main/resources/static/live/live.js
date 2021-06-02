// create Agora client 立即创建客户端
var client = AgoraRTC.createClient({mode: "rtc", codec: "vp8"});
AgoraRTC.enableLogUpload()// 开启日志上传功能
AgoraRTC.setLogLevel(0);
var localTracks = {
    videoTrack: null,
    audioTrack: null
};
var remoteUsers = {};
// Agora client options Agora客户端选项
var options = {
    appid: null,
    channel: null,
    uid: null,
    token: null
};

// the demo can auto join channel with params in url 演示可以使用url中的参数自动加入频道
$(() => {
    var urlParams = new URL(location.href).searchParams;
    options.appid = urlParams.get("appid");
    options.channel = urlParams.get("channel");
    options.token = urlParams.get("token");
    if (options.appid && options.channel) {
        $("#appid").val(options.appid);
        $("#token").val(options.token);
        $("#channel").val(options.channel);
        $("#join-form").submit();
    }
})

$("#join-form").submit(async function (e) {
    e.preventDefault();
    $("#join").attr("disabled", true);
    try {
        options.appid = $("#appid").val();
        options.token = $("#token").val();
        options.channel = $("#channel").val();
        await join();
        if (options.token) {
            $(".success-alert-with-token").css("display", "block");
        } else {
            $("#success-alert a").attr("href", `index.html?appid=${options.appid}&channel=${options.channel}&token=${options.token}`);
            $("#success-alert").css("display", "block");
        }
    } catch (error) {
        console.error(error);
    } finally {
        $("#leave").attr("disabled", false);
    }

    initWebSocket(options.uid)
})

$("#leave").click(async function (e) {
    await leave();
    hw.clear();
})

async function joins() {
    try {
        options.appid = $("#appid").val();
        options.token = $("#token").val();
        options.channel = $("#channel").val();
        await join();
    } catch (error) {
        console.error(error);
    } finally {
    }

    initWebSocket(options.uid)


    document.getElementById("teachView").hidden = false
    document.getElementById("joinView").hidden = true
// location.href="/live"
}

$("#join").click(async function (e) {
    console.log("join")
    await joins()
})

$(".leave").click(function (e) {
    leave();
    hw.clear();

    document.getElementById("teachView").hidden = true
    document.getElementById("joinView").hidden = false
})

async function f1() {
    console.log("join")
    await joins()
}
 function demo() {
     window.location.href="/video";
}

async function join() {
    //  添加事件侦听器以在远程用户发布时播放远程轨道。
    client.on("user-published", handleUserPublished);
    client.on("user-unpublished", handleUserUnpublished);

    //加入频道并创建本地曲目，我们可以使用Promise.all并发运行它们
    [options.uid, localTracks.audioTrack, localTracks.videoTrack] = await Promise.all([
        //  加入频道
        client.join(options.appid, options.channel, options.token || null),
        //  使用麦克风和摄像头创建本地轨道
        AgoraRTC.createMicrophoneAudioTrack(),
        AgoraRTC.createCameraVideoTrack()
    ]);
    console.log("我的id == " + options.uid)
    //  播放本地视频轨道
    localTracks.videoTrack.play("local-player");
    $("#local-player-name").text(`本地视频(${options.uid})`);

    //  将本地曲目发布到频道   不发布
    // await client.publish(Object.values(localTracks));
    console.log("发布成功");

}

async function leave() {
    for (trackName in localTracks) {
        var track = localTracks[trackName];
        if (track) {
            track.stop();
            track.close();
            localTracks[trackName] = undefined;
        }
    }

    // remove remote users and player views 删除远程用户和播放器视图
    remoteUsers = {};
    $("#remote-playerlist").html("");

    // leave the channel 离开频道
    await client.leave();

    $("#local-player-name").text("");
    $("#join").attr("disabled", false);
    $("#leave").attr("disabled", true);
    console.log("客户离开渠道成功");
    closeWebSocket()
}

var playerLists = []

async function subscribe(user, mediaType) {
    //测试效果是多画两个正常就一个
    await f(user, mediaType)
    await f(user, mediaType)
    await f(user, mediaType)
    await f(user, mediaType)
}

async function f(user, mediaType) {
    const uid = user.uid;
    //  订阅远程用户c
    await client.subscribe(user, mediaType);
    console.log("订阅成功");
    if (mediaType === 'video') {
        const player = $(`
      <div id="player-wrapper-${uid}"  style="display: inline-block; width: 30%;padding: 20px;" onclick="renderLink(this)"  data-p="0">
        <div style="display: inline-block;"><span class="player-name" >远程使用者(${uid})</span></div>
<!--<p class="player-name">remoteUser(${uid})</p>-->
        <div id="player-${uid}"  style="display: inline-block;  background-color: rgba(214,214,214,0.62);" class="player" ></div>
      </div>
    `);
        $("#remote-playerlist").append(player);
        user.videoTrack.play(`player-${uid}`);
    }
    if (mediaType === 'audio') {
        user.audioTrack.play();
    }
}

function handleUserPublished(user, mediaType) {
    console.log("远程视频")
    console.log(user.uid)
    const id = user.uid;
    remoteUsers[id] = user;
    subscribe(user, mediaType);
}

function handleUserUnpublished(user) {
    console.log("取消远程")
    const id = user.uid;
    delete remoteUsers[id];
    $(`#player-wrapper-${id}`).remove();
}

/**
 * 视图单击
 * @param obj
 */
function renderLink(obj) {
    // console.log("obj")
    // console.log(obj.dataset.p)
    //
    // if (!isExist(playerLists, obj)) {
    //     playerLists[playerLists.length] = obj
    // }
    // if (playerLists.length === 2) {
    //     narrow(playerLists[0])
    //     playerLists.shift()
    // }
    // if (obj.dataset.p === "0") {
    //     amplification(obj)
    // } else {
    //     narrow(obj)
    // }
}

/**
 * 放大
 * @param obj
 */
function amplification(obj) {
    obj.style.position = "relative";
    obj.dataset.p = "1";
    obj.style.zIndex = 100
    obj.style.animation = "bubble_big 0.5s linear 1 forwards";
    // locationMiddle(obj)
    // obj.style.animationName = "locationMiddle";
    // obj.style.animationDuration = "4s"
}

/**
 * 缩小
 * @param obj
 */
function narrow(obj) {
    obj.style.position = "relative";
    obj.dataset.p = "0";
    obj.style.animation = "bubble_small 0.5s linear 1 forwards";
    // locationReduction(obj)

    // obj.style.zIndex = 0

    // obj.style.animationName = "locationReduction";
    // obj.style.animationDuration = "4s"
}

/**
 * 查询list中是否以存在obj
 * @param list
 * @param obj
 * @returns {boolean}
 */
function isExist(list, obj) {
    for (let i = 0; i < list.length; i++) {
        var bl = list[i] === obj
        if (bl) {
            return bl
        }
    }
    return bl
}