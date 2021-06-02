<?php
/**********************************
 *      配置文件
 **********************************/

define("SYS_SIGN","rte_live_tool_admin");
define("SYS_UPLOAD_IMG_ROOT","./upload/img/");
define("SYS_UPLOAD_DATA_ROOT","./upload/data/");

define("SYS_KEYWORD","");
define("SYS_DESCRIPTION","");

define("VER","1.0.0");
define("COPYRIGHT","Copyright © Coco 2021 Inc. 保留所有权利<br>");
define("SYS_PWDHASH","dbsajbnmxzbcmneqouaslkd.s,manmbcjxab");

define("TIMEZONE","PRC");		//设置时区为中国
define("BASE_DIR",dirname(__DIR__).DIRECTORY_SEPARATOR);

/**********************************
 *      数据库配置
 **********************************/
if($_SERVER['HTTP_HOST']=="localhost"){ //本地开发环境
    define("WEBNAME","[开发]直播辅助工具");
    define("WEB_ADMIN_NAME","[开发]直播辅助工具管理后台");

    define("WEBURL","http://localhost/bisai/rte_live_tool_admin/");
    define("WEBBASE","http://localhost/bisai/rte_live_tool_admin/");

    define("DB_DATABASE","live_tools");
    define("DB_HOST","localhost");
    define("DB_USER","root");
    define("DB_PASSWORD","");
    define("DB_PORT","3308");
    define("DB_CHARESET","utf8");
}
else if($_SERVER['HTTP_HOST']=="minipro-course.todoen.com"){    //生产环境
    error_reporting(0);
    define("WEBNAME","直播辅助工具");
    define("WEB_ADMIN_NAME","直播辅助工具管理后台");

    define("WEBURL","https://minipro-course.todoen.com/live_tools/");
    define("WEBBASE","https://minipro-course.todoen.com/live_tools/");

    define("DB_DATABASE","live_tools");
    define("DB_HOST","localhost");
    define("DB_USER","root");
    define("DB_PASSWORD","");
    define("DB_PORT","3306");
    define("DB_CHARESET","utf8");
}
?>
