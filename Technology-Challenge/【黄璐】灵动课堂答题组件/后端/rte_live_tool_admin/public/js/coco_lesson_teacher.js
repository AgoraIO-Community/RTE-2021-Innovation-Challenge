/**
 *  coco插件 教师端
 *  作者：Coco 联系电话：15600008886
 *  Created By PhpStorm on 2021/05/28
 *  遵循开源协议 MIT 声网 RTE 2021编程挑战赛作品
 */
var coco_lesson_teacher = {
    appid: "",
    teacher_id: "",
    room_uuid: "",
    base_api: "",
    init(config) {
        if (typeof (config) == "undefined") {
            coco_lesson_teacher.common.toast("缺少初始化参数", 5000);
            return;
        }

        if (typeof (config.appid) == "undefined") {
            coco_lesson_teacher.common.toast("缺少初始化参数:appid", 5000);
            return;
        }

        if (typeof (config.teacher_id) == "undefined") {
            coco_lesson_teacher.common.toast("缺少初始化参数:teacher_id", 5000);
            return;
        }

        if (typeof (config.room_uuid) == "undefined") {
            coco_lesson_teacher.common.toast("缺少初始化参数:room_uuid", 5000);
            return;
        }

        if (typeof (config.base_api) == "undefined") {
            coco_lesson_teacher.common.toast("缺少初始化参数:base_api", 5000);
            return;
        }

        if (config.appid == "") {
            coco_lesson_teacher.common.toast("缺少初始化参数:appid", 5000);
            return;
        }

        if (config.teacher_id == "") {
            coco_lesson_teacher.common.toast("缺少初始化参数:teacher_id", 5000);
            return;
        }

        if (config.room_uuid == "") {
            coco_lesson_teacher.common.toast("缺少初始化参数:room_uuid", 5000);
            return;
        }

        if (config.base_api == "") {
            coco_lesson_teacher.common.toast("缺少初始化参数:base_api", 5000);
            return;
        }

        this.appid = config.appid;
        this.teacher_id = config.teacher_id;
        this.room_uuid = config.room_uuid;
        this.base_api = config.base_api;

        this.lesson.init();
    },
    lesson: {
        init() {
            this.start();
            this.bind_ele();
        },
        //启动
        start() {
            let _this = this;
            let sub_data = {
                appid: coco_lesson_teacher.appid,
                teacher_id: coco_lesson_teacher.teacher_id,
                room_uuid: coco_lesson_teacher.room_uuid
            };
            $.ajax({
                url: coco_lesson_teacher.base_api + "?c=api&m=get_teacher_token",
                data: JSON.stringify(sub_data),
                type: "POST",
                dataType: "json",
                success: function (res) {
                    if (res.code != 200) {
                        coco_lesson_teacher.common.toast(res.msg, 1500);
                        return;
                    }

                    AgoraEduSDK.config({
                        appId: coco_lesson_teacher.appid
                    });
                    // 启动课堂
                    AgoraEduSDK.launch(document.querySelector("#container"), {
                        // 你的用户的全局唯一标识ID，需要与你签发restToken时使用的uid一致
                        userUuid: coco_lesson_teacher.teacher_id,
                        // 声网RESTful API token，必须使用RTM Token
                        rtmToken: res.data.token,
                        // 用于显示的用户名
                        userName: res.data.teacher_name,
                        // 你的课堂的全局唯一标识ID
                        roomUuid: coco_lesson_teacher.room_uuid,
                        // 进入房间的角色，roleType 1: 老师, 2: 学生
                        roleType: 1,
                        // 房间的类型，0: 1v1, 1: 小班, 2: 大班
                        roomType: 0,
                        // 用于显示的房间名
                        roomName: res.data.room_name,
                        // 是否开启设备检测，true的话会在进入教室前显示设备检测页面
                        pretest: false,
                        duration: res.data.duration,
                        language: "zh",
                        courseWareList: [],
                        listener: (evt) => {
                            if (evt == 1) {
                                _this.on_load();
                            }
                        }
                    });
                },
                error: function (e) {
                    console.log(e);
                }
            });
        },
        //载入成功后的操作
        on_load() {
            console.log("【coco插件】初始化直播间成功");
            $("#queston_btn,#user_test").show();
            this.init_btns();
            this.bind_ele();
        },
        //载入新按钮
        init_btns() {
            $("body").append("<div id='question_btn'>题</div>");
            $("body").append("<div id='question_select_box'><div class='q_title'>题目推送<span class='close'>关闭</span><span class='manager'>管理题目</span></div><div class='content_box'></div></div>");
        },
        //绑定按钮事件
        bind_ele() {
            let _this = this;
            //单击打开学生端测试
            $("#user_test").on("click", function () {
                window.open("./student.html");
            });
            //单击打开题目按钮
            $("#question_btn").on("click", function () {
                _this.get_question_list();
            });
            //关闭题目推送窗口
            $("#question_select_box .q_title span.close").on("click", function () {
                $("#question_select_box").hide();
            });
            //打开管理窗口
            $("#question_select_box .q_title span.manager").on("click", function () {
                window.open(coco_lesson_teacher.base_api);
            });
        },
        //获取题目并展示
        get_question_list() {
            let _this = this;
            $.ajax({
                url: coco_lesson_teacher.base_api + "?c=api&m=get_questions&appid=" + coco_lesson_teacher.appid,
                type: "GET",
                dataType: "json",
                success: function (res) {
                    if (res.code != "200") {
                        coco_lesson_teacher.common.toast(res.msg);
                        return;
                    }

                    let data_length = res.data.length;
                    if (data_length == 0) {
                        coco_lesson_teacher.common.toast("暂未配置题目");
                        return;
                    }

                    let out_html = "";
                    for (let i = 0; i < data_length; i++) {
                        let d = res.data[i];
                        let question_type = "<font color='green'>【单选题】</font>";
                        if (d.stype == "2") {
                            question_type = "<font color='red'>【判断题】</font>";
                        }
                        if (d.stype == "3") {
                            question_type = "<font color='blue'>【多选题】</font>";
                        }

                        out_html += '<div class="question_list not_bind question_list_' + d['id'] + '">\n' +
                            '            <div class="title">\n' + question_type +
                            d['title'] +
                            '            </div>\n' +
                            '            <span class="open_question" data-id="' + d['id'] + '">展开</span>\n' +
                            '            <span class="send_question" data-id="' + d['id'] + '">推送</span>\n' +
                            '            <div class="content content_' + d['id'] + '">\n';


                        for (let m in d['options']) {
                            let o = d['options'][m];

                            if (d['answer'] != '') {
                                let has_answer = false;
                                let answer = d['answer'].split(",");
                                for (let a = 0; a < answer.length; a++) {
                                    if (answer[a] == (Number(m) + 1)) {
                                        out_html += '<div class="answer">' + (Number(m) + 1) + "、" + o + ' 【√】</div>';
                                        has_answer = true;
                                        continue;
                                    }
                                }

                                if (has_answer) {
                                    continue;
                                }
                            }

                            out_html += '<div>' + (Number(m) + 1) + "、" + o + '</div>';
                        }

                        if (d['stype'] == 2) {
                            if (d['answer'] == 1) {
                                out_html += "<font color='green'>【对】</font>";
                            }
                            else {
                                out_html += "<font color='red'>【错】</font>";
                            }
                        }

                        out_html += '            </div>\n';
                        out_html += '        </div>';
                    }
                    $("#question_select_box .content_box").html(out_html);
                    $("#question_select_box").show();
                    _this.question_list_bind_ele();
                },
                error: function (e) {
                    console.log(e);
                    coco_lesson_teacher.common.toast("请检查网络连接后重试");
                }
            })
        },
        //题目列表绑定单击事件
        question_list_bind_ele() {
            let _this = this;
            //展开/收起问题列表
            $(".question_list.not_bind .open_question").on("click", function () {
                let tid = $(this).attr("data-id");
                if ($(this).html() == "展开") {
                    //展开
                    $(".content_" + tid).addClass("active");
                    $(this).html("收起");
                    return;
                }

                $(".content_" + tid).removeClass("active");
                $(this).html("展开");
            });
            //推送问题到学生端
            $(".question_list.not_bind .send_question").on("click", function () {
                let tid = $(this).attr("data-id");
                _this.send_question(tid);
            });
            $(".question_list.not_bind").removeClass("not_bind");
        },
        //推送题目
        send_question(qid) {
            let _this = this;
            $.ajax({
                url: coco_lesson_teacher.base_api + "?c=api&m=send_question&appid=" + coco_lesson_teacher.appid + "&room_id=" + coco_lesson_teacher.room_uuid + "&qid=" + qid,
                type: "GET",
                dataType: "json",
                success: function (res) {
                    if (res.code != "200") {
                        coco_lesson_teacher.common.toast(res.msg);
                        return;
                    }
                    coco_lesson_teacher.common.toast("推送成功");
                },
                error: function (e) {
                    console.log(e);
                    coco_lesson_teacher.common.toast("请检查网络连接后重试");
                }
            })
        }
    },
    common: {
        toast(msg, duration) {
            $("#coco_toast").remove();
            duration = isNaN(duration) ? 1500 : duration;
            var m = document.createElement('div');
            m.id = "coco_toast";
            m.innerHTML = msg;
            m.style.cssText = "max-width:60%;min-width: 150px;padding:0 14px;height: 40px;color: rgb(255, 255, 255);line-height: 40px;text-align: center;border-radius: 4px;position: fixed;top: 50%;left: 50%;transform: translate(-50%, -50%);z-index: 999999;background: rgba(0, 0, 0,.7);font-size: 16px;";
            document.body.appendChild(m);
            setTimeout(function () {
                var d = 0.5;
                m.style.webkitTransition = '-webkit-transform ' + d + 's ease-in, opacity ' + d + 's ease-in';
                m.style.opacity = '0';
                setTimeout(function () {
                    document.body.removeChild(m)
                }, d * 1000);
            }, duration);
        }
    }
};