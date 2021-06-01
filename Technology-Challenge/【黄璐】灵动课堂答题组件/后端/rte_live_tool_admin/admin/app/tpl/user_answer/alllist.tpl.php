<!DOCTYPE html>
<html lang="en">

<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta charset="utf-8" />
    <title>【<?php echo $room_name;?>】推送题目数据 -
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
                        【<?php echo $room_name;?>】推送题目数据
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
                                        <th>问题ID</th>
                                        <th>题型</th>
                                        <th>题目</th>
                                        <th>选项</th>
                                        <th>答案</th>
                                        <th>推送时间</th>
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
                                            <?php echo $v['qid']; ?>
                                        </td>
                                        <td>
                                            <?php
                                            $qv=$question_list[$v['qid']];
                                            ?>
                                            <?php
                                            if ($qv['stype'] == 1) {
                                                echo "<font color='green'>单选</font>";
                                            }
                                            else if ($qv['stype'] == 2) {
                                                echo "<font color='red'>判断</font>";
                                            }
                                            else {
                                                echo "<font color='blue'>多选</font>";
                                            }
                                            ?>
                                        </td>
                                        <td>
                                            <?php echo $qv['title']; ?>
                                        </td>
                                        <td style="text-align: left">
                                            <?php
                                            if ($qv['stype'] != 2) {
                                                $options = explode("\n", $qv['options']);
                                                foreach ($options as $sk => $sv) {
                                                    echo ($sk + 1) . "、" . $sv . "<br/>";
                                                }
                                            }
                                            ?>
                                        </td>
                                        <td>
                                            <?php
                                            if ($qv['stype'] == 2) {
                                                if ($qv['answer'] == 1) {
                                                    echo "对";
                                                }
                                                else {
                                                    echo "错";
                                                }
                                            }
                                            else {
                                                if ($qv['answer'] > 0) {
                                                    echo $qv['answer'];
                                                }
                                            }
                                            ?>
                                        </td>
                                        <td>
                                            <?php echo $v['addtime']; ?>
                                        </td>
                                        <td>
                                            <a class="btn btn-minier btn-primary" title="答题情况"
                                               href="?c=user_answer&m=detail&id=<?php echo $v['id']; ?>" target="_blank">
                                                答题详情
                                            </a>
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
                '                                <i class="menu-icon fa fa-caret-right"></i>推送题目数据\n' +
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
