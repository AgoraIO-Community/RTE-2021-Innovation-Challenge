<!DOCTYPE html>
<html lang="en">

<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta charset="utf-8" />
    <title>首页 -
        <?php echo WEBNAME?>
    </title>
    <meta name="description" content="" />
    <?php 
        $fn=__DIR__."/../top_common.tpl";
        require_once($fn);
    ?>
    <div class="main-content">
        <!-- #section:basics/content.breadcrumbs -->
        <div class="breadcrumbs" id="breadcrumbs">
            <ul class="breadcrumb">
                <li>
                    <i class="ace-icon fa fa-home home-icon"></i>
                    <a href="?c=index">后台首页</a>
                </li>
            </ul>
            <!-- /.breadcrumb -->
            <!-- #section:basics/content.searchbox -->
            <div class="nav-search" id="nav-search">
            </div>
            <!-- /.nav-search -->
            <!-- /section:basics/content.searchbox -->
        </div>
        <div class="page-content">
            <!-- /section:settings.box -->
            <div class="page-content-area">
                <div class="row">
                    <div class="col-xs-12">
                        <!-- PAGE CONTENT BEGINS -->
                        <div class="alert alert-block alert-info sysinfo">
                            <button type="button" class="close" data-dismiss="alert">
                                <i class="ace-icon fa fa-times"></i>
                            </button>
                            <i class="ace-icon fa fa-check green"></i>
                            <strong class="green">欢迎您使用本系统</strong>
                            <br/>
                            <span>PHP版本号：<?php echo phpversion();?></span>
                            <span>ZEND版本：<?PHP echo zend_version(); ?></span>
                            <br/>
                            <span>MYSQL支持：<?php echo function_exists (mysql_close)?"是":"否"; ?></span>
                            <span>数据库持续连接：<?PHP echo @get_cfg_var("mysql.allow_persistent")?"是 ":"否"; ?></span>
                            <br/>
                            <span>MySQL最大连接数：<?php echo @get_cfg_var("mysql.max_links")==-1 ? "不限" : @get_cfg_var("mysql.max_links");
?></span>
                            <span>服务器操作系统：<?PHP echo PHP_OS;?></span>
                            <br/>
                            <span>最大执行时间：<?php echo  get_cfg_var("max_execution_time")."秒 ";?></span>
                            <span>最大上传限制：<?PHP echo get_cfg_var ("upload_max_filesize")?get_cfg_var ("upload_max_filesize"):"不允许上传附件";
?></span>
                            <br/>
                            <span>服务器时间：
                        <?php 
                                date_default_timezone_set (PRC);
                                echo date("Y-m-d G:i:s");
                        ?>
                    </span>
                        </div>
                        <!-- /.row -->
                        <!-- PAGE CONTENT ENDS -->
                    </div>
                    <!-- /.col -->
                </div>
                <!-- /.row -->
            </div>
            <!-- /.page-content-area -->
        </div>
        <!-- /.page-content -->
    </div>
    <!-- /.main-content -->
    <!--[if !IE]> -->
    <script type="text/javascript">
    window.jQuery || document.write("<script src='<?php echo STATICURL;?>assets/js/jquery.min.js'>" + "<" + "/script>");
    </script>
    <!-- <![endif]-->
    <!--[if IE]>
            <script type="text/javascript">
             window.jQuery || document.write("<script src='<?php echo STATICURL;?>assets/js/jquery1x.min.js'>"+"<"+"/script>");
            </script>
        <![endif]-->
    <script type="text/javascript">
    if ('ontouchstart' in document.documentElement) document.write("<script src='<?php echo STATICURL;?>assets/js/jquery.mobile.custom.min.js'>" + "<" + "/script>");
    </script>
    <script src="<?php echo STATICURL;?>assets/js/bootstrap.min.js"></script>
    <!--[if lte IE 8]>
          <script src="<?php echo STATICURL;?>assets/js/excanvas.min.js"></script>
        <![endif]-->
    <script src="<?php echo STATICURL;?>assets/js/jquery-ui.custom.min.js"></script>
    <script src="<?php echo STATICURL;?>assets/js/jquery.ui.touch-punch.min.js"></script>
    <script src="<?php echo STATICURL;?>assets/js/ace-elements.min.js"></script>
    <script src="<?php echo STATICURL;?>assets/js/ace.min.js"></script>
    <script type="text/javascript">
        $("#adminindexLi").addClass("active");
    </script>

    <?php 
    $fn=__DIR__."/../bottom_common.tpl";
    require_once($fn);
?>
