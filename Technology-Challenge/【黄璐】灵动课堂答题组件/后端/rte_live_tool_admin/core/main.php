<?php

/*核心控制类*/

class DsCore {
    public function __autoload($className) {
        if (file_exists($className . '.php')) {
            　　include_once($className . '.php');
        }
        else {
            exit('no file');
        }
    }

    public function __construct() {

    }

    public function run() {
        spl_autoload_register();

        $Router = new Router();
        $Router->initRouter();
    }
}

//工具方法
function flog($fname, $content) {
    $debugInfo = debug_backtrace();
    $line = $debugInfo[0]['line'];
//    $content .= "执行代码行号：" . $line . "<br/>";
    $fPath = $parentDirName = dirname(dirname(__FILE__));
    $fPath = $fPath . DIRECTORY_SEPARATOR . "log" . DIRECTORY_SEPARATOR . $fname;
    if (substr($fname, -5, 5) == ".html") {
        if (!file_exists($fPath)) {
            //初始化html文件
            $fp = fopen($fPath, "a+");
            $init_content = '<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta charset="utf-8" />
    <title>日志' . date("Y-m-d") . '</title>
    <meta name="description" content="" />
    <style>
    body{
    word-wrap: break-word;
    }
    </style>
</head>';
            fwrite($fp, $init_content);
            fclose($fp);
        }
    }

    //检测后缀名
    //    if(substr($fname,-5,5))
    $fp = fopen($fPath, "a+");
    fwrite($fp, $content);
    fclose($fp);
}

?>