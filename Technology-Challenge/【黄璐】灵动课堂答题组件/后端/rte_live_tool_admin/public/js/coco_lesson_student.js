/**
 *  coco插件 学生端
 *  作者：Coco 联系电话：15600008886
 *  Created By PhpStorm on 2021/05/28
 *  遵循开源协议 MIT 声网 RTE 2021编程挑战赛作品
 */
var coco_lesson_student = {
    appid: "",
    user_id: "",
    room_uuid: "",
    base_api: "",
    init(config) {
        if (typeof (config) == "undefined") {
            coco_lesson_student.common.toast("缺少初始化参数", 5000);
            return;
        }

        if (typeof (config.appid) == "undefined") {
            coco_lesson_student.common.toast("缺少初始化参数:appid", 5000);
            return;
        }

        if (typeof (config.user_id) == "undefined") {
            coco_lesson_student.common.toast("缺少初始化参数:user_id", 5000);
            return;
        }

        if (typeof (config.room_uuid) == "undefined") {
            coco_lesson_student.common.toast("缺少初始化参数:room_uuid", 5000);
            return;
        }

        if (typeof (config.base_api) == "undefined") {
            coco_lesson_student.common.toast("缺少初始化参数:base_api", 5000);
            return;
        }

        if (config.appid == "") {
            coco_lesson_student.common.toast("缺少初始化参数:appid", 5000);
            return;
        }

        if (config.user_id == "") {
            coco_lesson_student.common.toast("缺少初始化参数:user_id", 5000);
            return;
        }

        if (config.room_uuid == "") {
            coco_lesson_student.common.toast("缺少初始化参数:room_uuid", 5000);
            return;
        }

        if (config.base_api == "") {
            coco_lesson_student.common.toast("缺少初始化参数:base_api", 5000);
            return;
        }

        this.appid = config.appid;
        this.user_id = config.user_id;
        this.room_uuid = config.room_uuid;
        this.base_api = config.base_api;

        this.lesson.init();
    },
    lesson: {
        init() {
            this.start()
        },
        start() {
            let _this = this;
            let sub_data = {
                appid: coco_lesson_student.appid,
                user_id: coco_lesson_student.user_id,
                room_uuid: coco_lesson_student.room_uuid
            };
            $.ajax({
                url: coco_lesson_student.base_api + "?c=api&m=get_student_token",
                data: JSON.stringify(sub_data),
                type: "POST",
                dataType: "json",
                success: function (res) {
                    if (res.code != 200) {
                        coco_lesson_student.common.toast(res.msg, 1500);
                        return;
                    }

                    AgoraEduSDK.config({
                        appId: coco_lesson_student.appid
                    });
                    // 启动课堂
                    AgoraEduSDK.launch(document.querySelector("#container"), {
                        // 你的用户的全局唯一标识ID，需要与你签发restToken时使用的uid一致
                        userUuid: coco_lesson_student.user_id,
                        // 声网RESTful API token，必须使用RTM Token
                        rtmToken: res.data.token,
                        // 用于显示的用户名
                        userName: coco_lesson_student.user_id,
                        // 你的课堂的全局唯一标识ID
                        roomUuid: coco_lesson_student.room_uuid,
                        // 进入房间的角色，roleType 1: 老师, 2: 学生
                        roleType: 2,
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
                                _this.recive_event();
                            }
                        }
                    });
                },
                error: function (e) {
                    console.log(e);
                }
            });
        },
        //接收推送事件
        recive_event_itv: 0,
        recive_event() {
            let _this = this;
            this.recive_event_itv = setInterval(function () {
                $.ajax({
                    url: coco_lesson_student.base_api + "?c=api&m=get_now_question&appid=" + coco_lesson_student.appid + "&room_id=" + coco_lesson_student.room_uuid + "&user_id=" + coco_lesson_student.user_id,
                    data: "",
                    type: "GET",
                    dataType: "json",
                    success: function (res) {
                        if (res.code == 200) {
                            if (res.data.question.length > 0) {
                                _this.load_question(res.data.question[0]);
                            }
                        }
                    },
                    error: function (e) {
                        console.log(e);
                    }
                });
            }, 2000);
        },
        answer_state: 0,
        question_info: {
            "id": 0
        },
        load_question(question) {
            if (this.question_info.id == question.id) {
                console.log("【答题】当前题目正在作答，无需重新载入");
                return;
            }

            $("#question_box,#question_small_btn").remove();
            this.question_info = question;
            let q_list = "";

            let type = "【单选题】";
            if (question.stype == 2) {
                type = "【判断题】";
                q_list = '<div class="list" data-id="1">对</div>' +
                    '<div class="list" data-id="2">错</div>';
            }
            else {
                for (let i = 0; i < question.options.length; i++) {
                    q_list += '<div class="list" data-id="' + (i + 1) + '">' + (i + 1) + '、' + question.options[i] + '</div>';
                }
            }

            let out_html = '<div class="question_box noselect" id="question_box">\n' +
                '        <div class="title">\n' +
                '            答题卡\n' +
                '            <span>收起</span>\n' +
                '        </div>\n' +
                '        <div class="question_content">\n' +
                '            <div class="q_title">' + type + question['title'] + '</div>\n' +
                '            <div class="q_list">\n' + q_list +
                '            </div>\n' +
                '        </div>\n' +
                '\n' +
                '        <div class="sub_answer">提交</div>\n' +
                '    </div>';
            $("body").after(out_html);
            $("body").after('<div id="question_small_btn">答题卡</div>');

            $("body").addClass("has_question");
            $(".chat-history").append('<div class="chat-message"><div class="chat-message-left"><div class="chat-message-username">【答题】</div><div class="chat-message-content ghost">' + type + question['title'] + '</div></div></div>');

            this.bind_question_box_ele();
        },
        //绑定答题卡事件
        bind_question_box_ele() {
            let _this = this;
            //收起
            $("#question_box .title span").on("click", function () {
                $("#question_box").hide();
                $("#question_small_btn").show();
            });
            //打开
            $("#question_small_btn").on("click", function () {
                $("#question_box").show();
                $("#question_small_btn").hide();
            });
            //选项
            $("#question_box .q_list .list").on("click", function () {
                //判断题
                if (_this.question_info.stype == 1 || _this.question_info.stype == 2) {
                    $("#question_box .q_list .list").removeClass("active");
                    $(this).addClass("active");
                }
                else {
                    if ($(this).hasClass("active")) {
                        $(this).removeClass("active");
                    }
                    else {
                        $(this).addClass("active");
                    }
                }

            });
            //提交
            $("#question_box .sub_answer").on("click", function () {
                if ($("#question_box .q_list .list.active").length == 0) {
                    coco_lesson_student.common.toast("您还没有选择选项");
                    return;
                }

                let answers = [];
                let answer_text = "";
                $("#question_box .q_list .list.active").each(function (i, e) {
                    answers.push($(e).attr("data-id"));
                    answer_text += $(e).html() + "<br/>";
                });
                answers = answers.toString();
                //判断答对答错
                let answer_state = 0;
                if (answers == _this.question_info.answer) {
                    answer_state = 1;
                }
                else {
                    answer_state = 2;
                }
                if (_this.question_info.answer == "") {
                    answer_state = 0;
                }


                //提交答案
                $.ajax({
                    url: coco_lesson_student.base_api + "?c=api&m=sub_answer&appid=" + coco_lesson_student.appid + "&room_id=" + coco_lesson_student.room_uuid + "&user_id=" + coco_lesson_student.user_id + "&send_id=" + _this.question_info['send_id'] + "&answer=" + answers + "&answer_state=" + answer_state + "&qid=" + _this.question_info.id,
                    data: "",
                    type: "GET",
                    dataType: "json",
                    success: function (res) {
                        if (res.code == 200) {
                            coco_lesson_student.common.toast("提交成功");
                            //展示答案对错
                            if (_this.question_info.answer != '') {
                                let question_answer = _this.question_info.answer.split(",");
                                //标注对的
                                for (let a = 0; a < question_answer.length; a++) {
                                    $("#question_box .list[data-id='" + question_answer[a] + "']").addClass("right");
                                }
                                //标注当前是否答错
                                $("#question_box .q_list .list.active").each(function (i, e) {
                                    if (question_answer.indexOf($(e).attr("data-id")) == -1) {
                                        $(e).addClass("wrong");
                                    }
                                });
                            }

                            $(".sub_answer").hide();
                        }
                    },
                    error: function (e) {
                        console.log(e);
                    }
                });

            });
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