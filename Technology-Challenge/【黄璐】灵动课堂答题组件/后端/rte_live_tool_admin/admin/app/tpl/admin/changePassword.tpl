<!DOCTYPE html>
<html lang="en">

<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta charset="utf-8" />
    <title>修改管理员密码 -
        <?php echo WEBNAME ?>
    </title>
    <link rel="stylesheet" href="<?php echo STATICURL; ?>assets/css/jquery-ui.min.css" />

    <?php
    $fn = __DIR__ . "/../top_common.tpl";
    require_once($fn);
    ?>
    <div class="main-content">
        <div class="breadcrumbs" id="breadcrumbs">
            <ul class="breadcrumb">
                <li>
                    <i class="ace-icon fa fa-home home-icon"></i>
                    <a href="?c=index">后台首页</a>
                </li>
                <li>
                    <a href="?c=<?php echo $_c ?>&m=alllist">管理员管理</a>
                </li>
                <li>
                    <a href="javascript:void(0)">修改管理员密码</a>
                </li>
            </ul>
        </div>
        <div class="page-content">
            <div class="page-content-area">
                <div class="row">
                    <div class="col-xs-12">
                        <form class="form-horizontal" role="form">
                            <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 原密码：</label>

                                <div class="col-sm-9">
                                    <input type="password" id="spwd" placeholder="输入原密码" class="col-xs-10 col-sm-5"
                                           value="">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 新密码：</label>
                                <div class="col-sm-9">
                                    <input type="password" id="password1" placeholder="为空时表示不修改"
                                           class="col-xs-10 col-sm-5" value="">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 确认密码：</label>
                                <div class="col-sm-9">
                                    <input type="password" id="password2" placeholder="为空时表示不修改"
                                           class="col-xs-10 col-sm-5" value="">
                                </div>
                            </div>
                            <div class="clearfix form-actions">
                                <div class="col-md-offset-3 col-md-9">
                                    <button class="btn btn-info add" type="button">
                                        <i class="ace-icon fa fa-check bigger-110"></i>
                                        确认修改
                                    </button>

                                    <button class="btn" type="reset">
                                        <i class="ace-icon fa fa-undo bigger-110"></i>
                                        重置
                                    </button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div id="dialog-message" class="hide">
    </div>
    <script type="text/javascript">
        window.jQuery || document.write("<script src='<?php echo STATICURL;?>assets/js/jquery.min.js'>" + "<" + "/script>");
    </script>
    <script type="text/javascript">
        if ('ontouchstart' in document.documentElement) {
            document.write("<script src='<?php echo STATICURL;?>assets/js/jquery.mobile.custom.min.js'>" + "<" + "/script>");
        }
    </script>
    <script src="<?php echo STATICURL; ?>assets/js/bootstrap.min.js"></script>
    <script src="<?php echo STATICURL; ?>assets/js/jquery-ui.min.js"></script>
    <script src="<?php echo STATICURL; ?>assets/js/jquery.ui.touch-punch.min.js"></script>
    <script src="<?php echo STATICURL; ?>assets/js/ace-elements.min.js"></script>
    <script src="<?php echo STATICURL; ?>assets/js/ace.min.js"></script>
    <script type="text/javascript">
        window.onload = function () {
            $("#adminindexLi").addClass("active open hsub");

            $.widget("ui.dialog", $.extend({}, $.ui.dialog.prototype, {
                _title: function (title) {
                    var $title = this.options.title || '&nbsp;'
                    if (("title_html" in this.options) && this.options.title_html == true) {
                        title.html($title);
                    }
                    else {
                        title.text($title);
                    }
                }
            }));
        };

        $('body').on('click', '.add', function () {
            var spwd = $("#spwd").val();
            if (spwd == "") {
                scDialog('原始密码不能为空', '提示');
                return;
            }

            var password1 = $("#password1").val();
            if (password1 == "") {
                scDialog('新密码不能为空', '提示');
                return;
            }
            var password2 = $("#password2").val();
            if (password2 == "") {
                scDialog('确认密码不能为空', '提示');
                return;
            }

            if (password1 != password2) {
                scDialog('两次密码不一致', '提示');
                return;
            }

            addIt();

            //更换密码提交
            function addIt() {
                $.ajax({
                    url: "?c=<?php echo $this->_c?>&m=doChangePassword",
                    data: "spwd=" + spwd + "&pwd=" + password1,
                    type: "POST",
                    dataType: "json",
                    success: function (data) {
                        if (data.error == 0) {
                            scDialog('修改成功', '提示', function () {
                                location.replace("?c=index");
                            });
                        }
                        else {
                            scDialog(data.info, '错误', function () {
                            });
                        }
                    },

                    error: function (e) {
                        console.log(e);
                    }
                });
            }
        });
    </script>
    <?php
    $fn = __DIR__ . "/../bottom_common.tpl";
    require_once($fn);
    ?>
