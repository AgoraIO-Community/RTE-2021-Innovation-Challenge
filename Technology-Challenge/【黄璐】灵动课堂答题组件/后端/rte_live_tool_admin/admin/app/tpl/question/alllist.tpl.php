<!DOCTYPE html>
<html lang="en">

<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta charset="utf-8" />
    <title>题目列表 -
        <?php echo WEBNAME ?>
    </title>
    <link rel="stylesheet" href="<?php echo STATICURL; ?>assets/css/jquery-ui.min.css" />
    <?php
    $fn = __DIR__ . "/../top_common.tpl";
    require_once($fn);
    ?>

    <div class="main-content">
        <div class="breadcrumbs" id="breadcrumbs">
            <a href="?c=<?php echo $_c ?>&m=add" class="btn btn-xs btn-primary pull-left"
               style="margin-top: 7px;margin-left: 20px;">添加</a>
        </div>
        <div class="page-content">
            <div class="page-content-area">
                <div class="page-header">
                    <h1>
                        题目列表
                    </h1>
                </div>
                <div class="row">
                    <div class="col-sm-1" style="margin-top:6px;">
                        筛选条件
                    </div>
                    <div class="col-xs-5 input-group no-padding-right">
                        <input type="tel" id="keyword" placeholder="请输入题目标题" value="<?php echo @$_GET['keyword'] ?>">
                        <button id="searchBtn" class="btn  btn-sm btn-success"
                                style="margin-left: 5px;height:34px;padding: 0 6px;position: relative;top: -2px;">搜索
                        </button>
                        <a href="?c=<?php echo $_c ?>&m=<?php echo $_m ?>" class="btn  btn-sm btn-success"
                           style="margin-left: 5px;height:34px;padding: 0 6px;position: relative;top: -2px;">全部</a>
                    </div>
                    <br />
                    <div class="col-xs-12">
                        <div class="row">
                            <div class="col-xs-12">
                                <table id="sample-table-1" class="table table-striped table-bordered table-hover">
                                    <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>题型</th>
                                        <th>题目</th>
                                        <th>选项</th>
                                        <th>答案</th>
                                        <th>创建时间</th>
                                        <th>操作</th>
                                    </tr>
                                    </thead>
                                    <tbody align="center">
                                    <?php
                                    foreach ($alllist as $k => $v) {
                                    ?>
                                    <tr data-id="<?php echo $v['id']; ?>">
                                        <td>
                                            <?php echo $v['id']; ?>
                                        </td>
                                        <td>
                                            <?php
                                            if ($v['stype'] == 1) {
                                                echo "<font color='green'>单选</font>";
                                            }
                                            else if ($v['stype'] == 2) {
                                                echo "<font color='red'>判断</font>";
                                            }
                                            else {
                                                echo "<font color='blue'>多选</font>";
                                            }
                                            ?>
                                        </td>
                                        <td>
                                            <?php echo $v['title']; ?>
                                        </td>
                                        <td style="text-align: left">
                                            <?php
                                            if ($v['stype'] != 2) {
                                                $options = explode("\n", $v['options']);
                                                foreach ($options as $sk => $sv) {
                                                    echo ($sk + 1) . "、" . $sv . "<br/>";
                                                }
                                            }
                                            ?>
                                        </td>
                                        <td>
                                            <?php
                                            if ($v['stype'] == 2) {
                                                if ($v['answer'] == 1) {
                                                    echo "对";
                                                }
                                                else {
                                                    echo "错";
                                                }
                                            }
                                            else {
                                                if ($v['answer'] > 0) {
                                                    echo $v['answer'];
                                                }
                                            }
                                            ?>
                                        </td>
                                        <td>
                                            <?php echo $v['addtime']; ?>
                                        </td>
                                        <td>
                                            <a class="btn btn-minier btn-success" title="修改"
                                               href="?c=<?php echo $_c ?>&m=edit&id=<?php echo $v['id']; ?>">
                                                修改
                                            </a>
                                            <button class="btn btn-minier btn-danger delete" title="删除"
                                                    data-id="<?php echo $v['id']; ?>">
                                                删除
                                            </button>
                                        </td>
                                    </tr>
                                    <?php
                                    }
                                    ?>
                                    </tbody>
                                </table>
                            </div>
                            <div class="page">
                                <?php echo $pageHtml ?>
                            </div>
                        </div>
                        <div class="hr hr-18 dotted hr-double"></div>
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
            $("#<?php echo $_c;?>Li").addClass("active open hsub");
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
            bindSearch();
            bindDelete();
        };

        function bindDelete() {
            $(".delete").on("click", function () {
                var tid = $(this).attr("data-id");
                var that = this;

                $("#dialog-message").html("确认删除？");
                var dialog = $("#dialog-message").removeClass('hide').dialog({
                    modal: true,
                    title: "<div class='widget-header widget-header-small'><h4 class='smaller'>提示</h4></div>",
                    title_html: true,
                    buttons: [

                        {
                            text: "确定",
                            "class": "btn btn-primary btn-xs",
                            click: function () {
                                $(this).dialog("close");
                                dodel();
                            }
                        },

                        {
                            text: "取消",
                            "class": "btn btn-xs",
                            click: function () {
                                $(this).dialog("close");
                            }
                        }
                    ]
                });

                function dodel() {
                    $.ajax({
                        url: "?c=<?php echo $_c?>&m=del",
                        data: "tid=" + tid,
                        type: "POST",
                        dataType: "json",
                        success: function (data) {
                            if (data.error == "0") {
                                $(that).parent().parent().remove();
                                $(".pageallnum").html(Number($(".pageallnum").html()) - 1);
                            }
                            else {
                                scDialog(data.info, "错误提示");
                            }

                        },
                        error: function (e) {
                            console.log(e);
                        }
                    });
                }
            });
        }

        function bindSearch() {
            $("#searchBtn").on("click", function () {
                var keyword = $("#keyword").val();
                location.replace("?c=<?php echo $_c?>&m=<?php echo $_m?>&keyword=" + encodeURI(keyword));
            });
        }
    </script>
    <?php
    $fn = __DIR__ . "/../bottom_common.tpl";
    require_once($fn);
    ?>
