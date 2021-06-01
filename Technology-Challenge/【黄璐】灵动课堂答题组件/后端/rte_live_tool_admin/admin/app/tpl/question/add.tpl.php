<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta charset="utf-8" />
    <title>新增题目-<?php echo WEBNAME ?></title>
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
                        新增题目
                    </h1>
                </div>
                <div class="row">
                    <div class="col-xs-12">
                        <form class="form-horizontal" role="form">
                            <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right">
                                    题干：</label>
                                <div class="col-sm-9">
                                    <input type="text" id="title" placeholder="请输入题干" class="col-xs-10 col-sm-5"
                                           value="" maxlength="100">
                                    <br /><br />
                                    <font style="color:red;padding-left: 0;line-height: 33px;">
                                        *最大长度为150字符
                                    </font>
                                </div>
                            </div>

                            <div class="form-group is_expire">
                                <label class="col-sm-3 control-label no-padding-right" for="stype"> 题型：</label>
                                <div class="col-sm-9">
                                    <select name="" id="stype">
                                        <option value="1">单选</option>
                                        <option value="2">判断</option>
                                        <option value="3">多选</option>
                                    </select>
                                </div>
                            </div>

                            <div class="form-group stype1">
                                <label class="col-sm-3 control-label no-padding-right" for="options"> 题目选项：</label>
                                <div class="col-sm-9">
                                    <textarea name="" id="options" cols="30" rows="10" placeholder="每行为一个答案"
                                              class="col-xs-10 col-sm-5"></textarea>
                                    <br /><br />
                                    <div style="color:red;padding-left: 0;line-height: 33px;clear: both">
                                        *无需填写题号，系统自动补全
                                    </div>
                                </div>
                            </div>

                            <div class="form-group stype1">
                                <label class="col-sm-3 control-label no-padding-right" for="answer"> 答案所在行：</label>
                                <div class="col-sm-9">
                                    <input type="text" id="answer" placeholder="输入答案，例如 1,2,3"
                                           class="col-xs-10 col-sm-5" maxlength="100" value="">
                                    <br /><br />
                                    <font style="color:red;padding-left: 0;line-height: 33px;">
                                        可不填
                                    </font>
                                </div>
                            </div>

                            <div class="form-group stype2" style="display: none">
                                <label class="col-sm-3 control-label no-padding-right" for="query"> 正确答案：</label>
                                <div class="col-sm-9">
                                    <select name="" id="answer2">
                                        <option value="1">对</option>
                                        <option value="2">错</option>
                                    </select>
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
    <script type="text/javascript" charset="utf-8"
            src="<?php echo STATICURL; ?>js/lib/umeditor/umeditor.config.js"></script>
    <script type="text/javascript" charset="utf-8"
            src="<?php echo STATICURL; ?>js/lib/umeditor/umeditor.min.js"></script>
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

        $("#stype").on("change", function () {
            if ($(this).val() == 2) {
                $(".stype2").show();
                $(".stype1").hide();
            }
            else {
                $(".stype2").hide();
                $(".stype1").show();
            }
        });

        $('body').on('click', '.add', function () {
            var title = $("#title").val();
            if (title == "") {
                scDialog("请输入题干", "提示");
                return;
            }

            var stype = $("#stype").val();

            var options = $("#options").val();
            if (stype != 2 && options == "") {
                scDialog("请输入选项", "提示");
                return;
            }

            var answer = $("#answer").val();
            var answer2 = $("#answer2").val();
            if (stype == 2) {
                answer = answer2;
            }
            if (stype == 2 && answer == 0) {
                scDialog("请输入答案", "提示");
                return;
            }

            if (stype == 2 && (answer < 1 || answer > 2)) {
                scDialog("判断题答案为1或2", "提示");
                return;
            }
            addIt();

            //更换密码提交
            function addIt() {
                let sub_data = {
                    title: title,
                    stype: stype,
                    options: options,
                    answer: answer
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
