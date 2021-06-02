<?php

/*路由类*/

class Router {
    public function __construct() {

    }

    /*解析路由*/
    public function initRouter() {
        function myautoload($classname) {
            $filename = APP_ROOT . "/app/controller/" . $classname . ".php";
            if (file_exists($filename)) {
                include_once($filename);
            }
            else {
                echo "<head><title>" . WEBNAME . "</title></head><h1 style='    text-align: center;
                                font-size: 103px;
                                color: #a09e9e;
                                margin-bottom: 2px;
                                margin-top: 10%;'>ERROR</h1>
                                       <p style='    text-align: center;
                                font-size: 25px;
                                color: #a09e9e;'>引入的类不存在，类名：" . $classname . "</p>
                            ";
                die();
            }
        }

        require_once("Controller.php");
        spl_autoload_register("myautoload");

        //初始化控制器
        if (isset($_GET['c']) && @$_GET['c'] != '') {
            $ctlName = $_GET['c'] . "Controller";
            $ctl = @new $ctlName;
            if (isset($_GET['m'])) {
                //初始化方法
                if (method_exists($ctl, $_GET['m'])) {
                    $m = $_GET['m'];
                    $ctl->$m();
                }
                else {
                    $ctl->index();
                }
            }
            else {
                @$ctl->index();
            }
        }
        else {
            header("location:?c=index");
        }
    }
}

?>