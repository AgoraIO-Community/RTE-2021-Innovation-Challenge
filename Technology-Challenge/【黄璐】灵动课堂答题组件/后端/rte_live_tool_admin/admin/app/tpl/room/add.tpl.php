<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta charset="utf-8" />
    <title>新增直播间-<?php echo WEBNAME ?></title>
    <link rel="stylesheet" href="<?php echo STATICURL; ?>assets/css/jquery-ui.min.css" />
    <link href="<?php echo STATICURL; ?>js/lib/umeditor/themes/default/css/umeditor.css" type="text/css"
          rel="stylesheet">
    <link rel="stylesheet" href="<?php echo STATICURL; ?>assets/css/jquery-ui.min.css" />
    <link rel="stylesheet" href="<?php echo STATICURL; ?>assets/css/datepicker.css" />
    <link rel="stylesheet" href="<?php echo STATICURL; ?>assets/css/bootstrap-timepicker.css" />
    <link rel="stylesheet" href="<?php echo STATICURL; ?>assets/css/daterangepicker.css" />
    <link rel="stylesheet" href="<?php echo STATICURL; ?>assets/css/bootstrap-datetimepicker.css" />
    <link rel="stylesheet" href="<?php echo STATICURL; ?>assets/css/bootstrap-switch.min.css" />
    <?php
    $fn = __DIR__ . "/../top_common.tpl";
    require_once($fn);
    ?>
    <div class="main-content">
        <div class="page-content">
            <div class="page-content-area">
                <div class="page-header">
                    <h1>
                        新增直播间
                    </h1>
                </div>
                <div class="row">
                    <div class="col-xs-12">
                        <form class="form-horizontal" role="form">
                            <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right">
                                    appCertificate：</label>
                                <div class="col-sm-9">
                                    <input type="text" id="appCertificate" placeholder="请输入声网appCertificate" class="col-xs-10 col-sm-5"
                                           value="" maxlength="100">
                                    <br /><br />
                                    <font style="color:red;padding-left: 0;line-height: 33px;">
                                        *最大长度为100字符
                                    </font>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right">
                                    老师ID：</label>
                                <div class="col-sm-9">
                                    <input type="text" id="teacher_id" placeholder="请输入老师ID" class="col-xs-10 col-sm-5"
                                           value="" maxlength="100">
                                    <br /><br />
                                    <font style="color:red;padding-left: 0;line-height: 33px;">
                                        *最大长度为100字符
                                    </font>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right">
                                    老师昵称：</label>
                                <div class="col-sm-9">
                                    <input type="text" id="teacher_name" placeholder="请输入老师昵称" class="col-xs-10 col-sm-5"
                                           value="" maxlength="100">
                                    <br /><br />
                                    <font style="color:red;padding-left: 0;line-height: 33px;">
                                        *最大长度为100字符
                                    </font>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right">
                                    房间号ID：</label>
                                <div class="col-sm-9">
                                    <input type="text" id="room_uuid" placeholder="请输入房间号ID" class="col-xs-10 col-sm-5"
                                           value="" maxlength="100">
                                    <br /><br />
                                    <font style="color:red;padding-left: 0;line-height: 33px;">
                                        *最大长度为100字符，房间号ID为唯一
                                    </font>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right">
                                    房间名称：</label>
                                <div class="col-sm-9">
                                    <input type="text" id="room_name" placeholder="请输入房间名称" class="col-xs-10 col-sm-5"
                                           value="" maxlength="100">
                                    <br /><br />
                                    <font style="color:red;padding-left: 0;line-height: 33px;">
                                        *最大长度为100字符
                                    </font>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right">
                                    直播持续时间：</label>
                                <div class="col-sm-9">
                                    <input type="number" id="duration" placeholder="请输入直播持续时间（秒）" class="col-xs-10 col-sm-5"
                                           value="3600">
                                    <br /><br />
                                    <font style="color:red;padding-left: 0;line-height: 33px;">
                                        *单位为秒
                                    </font>
                                </div>
                            </div>


                            <div class="clearfix form-actions">
                                <div class="col-md-offset-3 col-md-9">
                                    <button class="btn btn-info add" type="button"><i
                                                class="ace-icon fa fa-check bigger-110"></i> 立即提交
                                    </button>

                                    <button class="btn" type="reset"><i class="ace-icon fa fa-undo bigger-110"></i> 重置
                                    </button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div id="dialog-message" class="hide"></div>
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
    <script type="text/javascript" charset="utf-8" src="<?php echo STATICURL; ?>js/lib/umeditor/umeditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="<?php echo STATICURL; ?>js/lib/umeditor/umeditor.min.js"></script>
    <script type="text/javascript" src="<?php echo STATICURL; ?>js/lib/umeditor/lang/zh-cn/zh-cn.js"></script>
    <script src="<?php echo STATICURL; ?>assets/js/date-time/bootstrap-datepicker.min.js"></script>
    <script src="<?php echo STATICURL; ?>assets/js/date-time/bootstrap-timepicker.min.js"></script>
    <script src="<?php echo STATICURL; ?>assets/js/date-time/moment.min.js"></script>
    <script src="<?php echo STATICURL; ?>assets/js/date-time/daterangepicker.min.js"></script>
    <script src="<?php echo STATICURL; ?>assets/js/date-time/bootstrap-datetimepicker.min.js"></script>
    <script src="<?php echo STATICURL; ?>assets/js/bootstrap-switch.min.js"></script>
    <script type="text/javascript">
        window.onload = function () {
            $("#<?php echo $_c?>Li").addClass("active open hsub");
            <?php
            echo '$("#' . $_c . "__" . $_m . 'Li").addClass("active");';
            ?>

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
            var appCertificate	 = $("#appCertificate\t").val();
            if (appCertificate	 == "") {
                scDialog("请输入声网appCertificate", "提示");
                return;
            }

            var teacher_id = $("#teacher_id").val();
            if (teacher_id == "") {
                scDialog("请输入老师ID", "提示");
                return;
            }

            var teacher_name = $("#teacher_name").val();
            if (teacher_name == "") {
                scDialog("请输入老师昵称", "提示");
                return;
            }

            var room_uuid = $("#room_uuid").val();
            if (room_uuid == "") {
                scDialog("请输入房间号ID", "提示");
                return;
            }

            var room_name = $("#room_name").val();
            if (room_name == "") {
                scDialog("请输入房间名称", "提示");
                return;
            }

            var duration = $("#duration").val();
            if (duration == "") {
                scDialog("请输入直播持续时间", "提示");
                return;
            }

            addIt();
            //更换密码提交
            function addIt() {
                let sub_data = {
                    appCertificate: appCertificate,
                    teacher_id: teacher_id,
                    teacher_name: teacher_name,
                    room_uuid: room_uuid,
                    room_name: room_name,
                    duration: duration,
                };
                $.ajax({
                    url: "?c=<?php echo $this->_c?>&m=doAdd",
                    data: JSON.stringify(sub_data),
                    type: "POST",
                    dataType: "json",
                    success: function (res) {
                        if (res.code == 200) {
                            scDialog("添加成功", "提示", function () {
                                location.replace("?c=<?php echo $_c;?>&m=alllist");
                            });
                            return;
                        }

                        scDialog(res.info, "提示");
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
