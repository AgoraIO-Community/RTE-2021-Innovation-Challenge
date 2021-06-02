<?php
/*入口文件*/
session_start();
ini_set('display_errors', '1');
error_reporting(E_ALL);
//error_reporting(E_ALL & ~E_NOTICE & ~E_STRICT & ~E_DEPRECATED);
define("APP_ROOT","./admin/");
define("USER_LOG_STATE",0);

//$error_handler = set_error_handler("myErrorHandler");//开启自定义错误日志
require_once("./config/global.function.php");
require_once("./config/config.php");
require_once("./core/main.php");
define("STATICURL",WEBBASE."admin/public/");
date_default_timezone_set(TIMEZONE);
$DsCore=new DsCore();
$DsCore->run();
?>
