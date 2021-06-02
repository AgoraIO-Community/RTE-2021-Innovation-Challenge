<!DOCTYPE html>
<html lang="en">

<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta charset="utf-8" />
    <title>答题详情列表 -
        <?php echo WEBNAME ?>
    </title>
    <link rel="stylesheet" href="<?php echo STATICURL; ?>assets/css/jquery-ui.min.css" />
    <?php
    $fn = __DIR__ . "/../top_common.tpl";
    require_once($fn);
    ?>
    <div class="main-content">
        <div class="page-content">
            <div class="page-content-area">
                <div class="page-header">
                    <h1>
                        答题详情列表
                    </h1>
                </div>
                <div class="row">
                    <div class="col-xs-12">
                        <div class="row">
                            <div class="col-xs-12">
                                <table id="sample-table-1" class="table table-striped table-bordered table-hover">
                                    <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>用户ID</th>
                                        <th>用户答案</th>
                                        <th>是否正确</th>
                                        <th>提交时间</th>
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
                                            <?php echo $v['user_id']; ?>
                                        </td>
                                        <td style="text-align: left">
                                            <?php
                                            if($question['stype']==2){
                                                if($v['aid']==1){
                                                    echo "<font color='green'>对</font>";
                                                }
                                                else{
                                                    echo "<font color='red'>错</font>";
                                                }
                                            }
                                            else{
                                                $aid=explode(",",$v['aid']);
                                                $options=explode("\n",$question['options']);
                                                foreach ($options as $ok=>$ov){
                                                    foreach ($aid as $ak=>$av){
                                                        if($ok+1==$av){
                                                            echo $av."、".$ov."<br/>";
                                                        }
                                                    }
                                                }
                                            }

                                            ?>
                                        </td>
                                        <td>
                                            <?php
                                            if($v['is_right']==0){
                                                echo "<font color='#ccc'>无标准答案</font>";
                                            }
                                            else if($v['is_right']==1){
                                                echo "<font color='green'>正确</font>";
                                            }
                                            else{
                                                echo "<font color='red'>错误</font>";
                                            }?>
                                        </td>
                                        <td>
                                            <?php echo $v['addtime']; ?>
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
                            <!-- /.span -->
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
            $("#roomLi").addClass("active open hsub");
            $("#<?php echo "room__add"?>Li").after('<li class="active">\n' +
                '                            <a href="javascript:void(0)">\n' +
                '                                <i class="menu-icon fa fa-caret-right"></i>答题详情\n' +
                '                            </a>\n' +
                '                            <b class="arrow"></b>\n' +
                '                        </li>');

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
    </script>
    <?php
    $fn = __DIR__ . "/../bottom_common.tpl";
    require_once($fn);
    ?>
