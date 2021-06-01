<!DOCTYPE html>
<html lang="en">

<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta charset="utf-8" />
    <title>后台管理员登录 -
        <?php echo WEBNAME ?>
    </title>
    <meta name="description" content="Restyling jQuery UI Widgets and Elements" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
    <link rel="stylesheet" href="<?php echo STATICURL; ?>assets/css/bootstrap.min.css" />
    <link rel="stylesheet" href="<?php echo STATICURL; ?>assets/css/font-awesome.min.css" />
    <link rel="stylesheet" href="<?php echo STATICURL; ?>assets/css/ace-fonts.css" />
    <link rel="stylesheet" href="<?php echo STATICURL; ?>assets/css/ace.min.css" id="main-ace-style" />
    <script src="<?php echo STATICURL; ?>assets/js/ace-extra.min.js"></script>
</head>
</head>

<body class="login-layout blur-login">
<div class="main-container">
    <div class="main-content">
        <div class="row">
            <div class="col-sm-10 col-sm-offset-1">
                <div class="login-container">
                    <div class="center">
                        <h1>
                            <span class="white"><?php echo WEBNAME ?></span>
                        </h1>
                    </div>
                    <div class="space-6"></div>
                    <div class="position-relative">
                        <div id="login-box" class="login-box visible widget-box no-border">
                            <div class="widget-body">
                                <div class="widget-main">
                                    <h4 class="header blue lighter bigger">
                                        <i class="ace-icon fa fa-coffee green"></i>
                                        输入登录信息
                                    </h4>
                                    <div class="space-6"></div>
                                    <form>
                                        <fieldset>
                                            <label class="block clearfix">
                                                    <span class="block input-icon input-icon-right username">
															<input type="text" class="form-control" placeholder="用户名"
                                                                   value="2306c08dc63b4e16834d1a5bb41621a6" />
															<i class="ace-icon fa fa-user"></i>
														</span>
                                            </label>
                                            <label class="block clearfix">
                                                    <span class="block input-icon input-icon-right password">
															<input type="password" class="form-control"
                                                                   placeholder="密码" />
															<i class="ace-icon fa fa-lock"></i>
														</span>
                                            </label>
                                            <div class="space"></div>
                                            <div class="clearfix">
                                                <label class="inline">
                                                    <input type="checkbox" class="ace" />
                                                    <span class="lbl"> 记住我</span>
                                                </label>
                                                <button type="button"
                                                        class="width-35 pull-right btn btn-sm btn-primary login-btn">
                                                    <i class="ace-icon fa fa-key"></i>
                                                    <span class="bigger-110">登录</span>
                                                </button>
                                            </div>
                                            <p style="    text-align: center;
    color: #b3adad;
    margin-top: 20px;">比赛测试账号：2306c08dc63b4e16834d1a5bb41621a6<br />密码：123456</p>
                                            <div class="space-4"></div>
                                        </fieldset>
                                    </form>
                                </div>
                            </div>
                        </div>
                        <div id="forgot-box" class="forgot-box widget-box no-border">
                            <div class="widget-body">
                                <div class="widget-main">
                                    <h4 class="header red lighter bigger">
                                        <i class="ace-icon fa fa-key"></i>
                                        重置密码
                                    </h4>
                                    <div class="space-6"></div>
                                    <p>
                                        输入您注册时候的email，用以接收密码重置信息
                                    </p>
                                    <form>
                                        <fieldset>
                                            <label class="block clearfix">
                                                    <span class="block input-icon input-icon-right">
															<input type="email" class="form-control"
                                                                   placeholder="Email" />
															<i class="ace-icon fa fa-envelope"></i>
														</span>
                                            </label>
                                            <div class="clearfix">
                                                <button type="button" class="width-35 pull-right btn btn-sm btn-danger">
                                                    <i class="ace-icon fa fa-lightbulb-o"></i>
                                                    <span class="bigger-110">发送!</span>
                                                </button>
                                            </div>
                                        </fieldset>
                                    </form>
                                </div>
                                <div class="toolbar center">
                                    <a href="#" data-target="#login-box" class="back-to-login-link">
                                        返回登录
                                        <i class="ace-icon fa fa-arrow-right"></i>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    window.jQuery || document.write("<script src='<?php echo STATICURL;?>assets/js/jquery.min.js'>" + "<" + "/script>");
</script>
<script type="text/javascript">
    if ('ontouchstart' in document.documentElement) {
        document.write("<script src='<?php echo STATICURL;?>assets/js/jquery.mobile.custom.min.js'>" + "<" + "/script>");
    }
</script>
<script type="text/javascript">
    jQuery(function ($) {
        $(document).on('click', '.toolbar a[data-target]', function (e) {
            e.preventDefault();
            var target = $(this).data('target');
            $('.widget-box.visible').removeClass('visible');
            $(target).addClass('visible');
        });
    });

    $(".login-btn").on("click", function (event) {
        dologin();
    });
    $(".username input, .password input").keydown(
        function (event) {
            if (event.keyCode == 13) {
                dologin();
            }
        }
    );

    function dologin() {
        var uname = $(".username input").val();
        var pwd = $(".password input").val();
        if (uname == '') {
            alert("请填写用户名");
            $(".username input").focus();
            return;
        }
        if (pwd == '') {
            alert("请填写密码");
            $(".password input").focus();
            return;
        }
        $.ajax({
            url: "?c=login&m=login",
            data: "username=" + $(".username input").val() + "&password=" + $(".password input").val(),
            type: "POST",
            dataType: "json",
            success: function (res) {
                if (res.code == 200) {
                    location.replace("<?php
                        if (isset($_SESSION[SYS_SIGN . "_admin_preurl"]) == FALSE || $_SESSION[SYS_SIGN . "_admin_preurl"] == "") {
                            $_SESSION[SYS_SIGN . "_admin_preurl"] = "?c=index";
                        }
                        echo $_SESSION[SYS_SIGN . "_admin_preurl"];
                        ?>");
                }
                else {
                    Toast(res.info);
                }
            },

            error: function (e) {
                console.log(e);
            }
        });
    };

    function Toast(msg, duration) {
        $("#toast").remove();
        duration = isNaN(duration) ? 1000 : duration;
        var m = document.createElement('div');
        m.id = "toast";
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
</script>
</body>

</html>
