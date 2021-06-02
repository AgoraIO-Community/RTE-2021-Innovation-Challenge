<?php
if (isset($ctl) == false) {
    $ctl = new Controller();
    $db = $ctl->DbUtil;
    $con = $ctl->DbUtil->_dbObj;
}

class Controller {
    public $M;
    public $DbUtil;
    public $con;
    public $t;
    public $_m;
    public $_c;
    public $_classname;
    public $_get;
    public $_post;
    public $is_check_auth;
    public $root;

    //构造方法
    public function __construct($is_check_auth = 1,$root="") {
        $this->is_check_auth = $is_check_auth;
        $DbUtil = $this->getUtil("DataBase");
        $this->DbUtil = $DbUtil;
        $this->con = $this->DbUtil->_dbObj;
        $this->root=$root;

        $this->_c = $this->_fget("c");
        $this->_m = $this->_fget("m");
        if($this->_m==""){
            $this->_m="index";
        }

        $this->user_operation_log();

        $this->_classname = str_replace("Controller", "", get_class($this));
        if ($this->_classname != "") {
            $this->M = $this->getModel($this->_classname);
        }
        else {
            $this->M = new StdClass();
        }
        $this->M->_con = $this->DbUtil->_dbObj;

        $this->_get = $this->_filteArr($_GET);
        $this->_post = $this->_filteArr($_POST);

        if (method_exists($this, "start")) {
            $this->start();
        }
    }

    public function start(){

    }

    //记录操作日志
    public function user_operation_log() {
        if(@USER_LOG_STATE==0){
            return;
        }
        $con = $this->con;
        if (@$_SESSION[SYS_SIGN . "admin_id"] == 0) {
            return;
        }
        //当前存储文件戳
        $raw = file_get_contents('php://input');
        $data=md5($this->_c. $this->_m.json_encode($_GET, 256).json_encode($_POST, 256).$raw);
        if(@$_SESSION[SYS_SIGN."user_operation_log"]==$data){
            return;
        }
        $_SESSION[SYS_SIGN."user_operation_log"]=$data;

        $sql = "insert into user_operation_log set
                    uid='" . @$_SESSION[SYS_SIGN . "admin_id"] . "',
                    controller='" . $this->_c . "',
                    model='" . $this->_m . "',
                    `get`='" . json_encode($_GET, 256) . "',
                    post='" . json_encode($_POST, 256) . "',
                    raw='" . $raw . "',
                    header='" . json_encode($_SERVER, 256) . "',
                    addtime='" . date("Y-m-d H:i:s") . "'
            ";
        mysqli_query($con, $sql);
    }

    public function __destruct() {
        $_SESSION[SYS_SIGN . '_SQLDATA'] = [];
    }

    //权限校验
    public function checkPower($ctlPower) {
        $userPower = @$_SESSION[SYS_SIGN . 'power'];
        $userPower = explode(",", $userPower);
        $powerState = 0;
        $ctlPowerArr = explode(",", $ctlPower);

        foreach ($userPower as $k => $v) {
            foreach ($ctlPowerArr as $ck => $cv) {
                if ($v == $cv) {
                    $powerState = 1;
                }
            }
        }

        if ($powerState == 0) {
            echo json_encode(array("error" => "901", "info" => "请检查当前登录账号是否拥有操作权限！"), JSON_UNESCAPED_UNICODE);
            die();
        }
    }

    //默认index执行
    public function index() {
        $this->page_error("404");
    }

    public function return_header(){
        header('content-type:application/json');
        header('Access-Control-Allow-Origin:*');
        header('Access-Control-Allow-Methods:*');
        header('Access-Control-Allow-Headers:*');
        header('Content-Type:application/json;charset=utf-8');
    }

    public function return_json($code,$info,$data=[],$use_time=0){
        $this->return_header();
        $str=$this->get_return_json($code,$info,$data,$use_time);
        header('Content-Length: '. strlen($str));//告诉浏览器本次响应的数据大小只有上面的echo那么多
        echo $str;
    }

    public function get_return_json($code,$info,$data=[],$use_time=0){
        if($use_time==0){
            return json_encode(
                [
                    "code"=>$code,
                    "info"=>$info,
                    "data"=>$data
                ],
                256
            );
        }

        return json_encode(
            [
                "code"=>$code,
                "info"=>$info,
                "use_time"=>$use_time,
                "data"=>$data
            ],
            256
        );
    }

    public function get_return_obj($code,$info,$data=[]){
        return [
            "code"=>$code,
            "info"=>$info,
            "data"=>$data
        ];
    }

    /**
     * 获取limit语句
     * @param $perpage  每页显示数
     * @param string $nowpage   当前页码
     * @return string
     */
    protected function getPageSql($perpage, $nowpage = "") {
        if ($nowpage == "") {
            $page = 1;
            if (isset($_GET['page'])) {
                $page = $_GET['page'];
            }
            if ($page <= 0) {
                $page = 1;
            }
        }
        else {
            if ($nowpage <= 0) {
                $page = 1;
            }
            else {
                $page = $nowpage;
            }
        }

        return "limit " . ($page - 1) * $perpage . "," . $perpage;
    }

    /**
     * 获取当前页码
     * @param string $method    获取方法 GET/POST/BYVAL 默认GET，使用BYVAL时，则以第三个参数为准
     * @param int $maxpage      最大页码 为0时不限制
     * @param int $nowpage      传入页码
     * @return int
     */
    protected function getNowPage($method="GET",$maxpage=0,$nowpage=0){
        if($method=="GET"){
            $page=(int)$this->_fget("page");
        }
        else if($method=="POST"){
            $page=(int)$this->_fpost("page");
        }
        else{
            $page=(int)$nowpage;
        }

        if($page==0){
            $page=1;
        }
        if($maxpage>0 && $page>$maxpage){
            $page=$maxpage;
        }
        return $page;
    }

    //过滤提交的参数
    protected function _filterSQL($str) {
        //return addslashes(htmlspecialchars($str));
        if(gettype($str)=="string"){
            return htmlspecialchars($str);
        }
        else if(gettype($str)=="object" || gettype($str)=="array" ){
            foreach($str as $k=>$v){
                $str[$k]=$this->_filterSQL($v);
            }
            return $str;
        }

        return $str;
    }

    protected function _filterSQL2($str) {
        return htmlspecialchars($str);
    }

    //过滤所有post参数
    protected function _filtePOST() {
        foreach ($_POST as $key => $value) {
            $_POST[$key] = $this->_filterSQL($value);
        }
    }

    //过滤所有get参数
    protected function _filteGET() {
        foreach ($_GET as $key => $value) {
            $_GET[$key] = $this->_filterSQL($value);
        }
    }

    //过滤所有参数
    protected function _filteArr($arr) {
        foreach ($arr as $key => $value) {
            $arr[$key] = $this->_filterSQL($value);
        }
        return $arr;
    }

    //获取GET参数
    protected function _get($getName) {
        if (isset($_GET[$getName])) {
            return $_GET[$getName];
        }
        else {
            return "";
        }
    }

    //获取Header参数
    protected function _getheader($getName) {
        if (isset($_SERVER["HTTP_".strtoupper($getName)])) {
            return $_SERVER["HTTP_".strtoupper($getName)];
        }
        else {
            return "";
        }
    }

    //获取GET参数并对其进行字符串过滤
    protected function _fget($getName) {
        return $this->_filterSQL($this->_get($getName));
    }

    //获取POST参数
    protected function _post($getName) {
        if (isset($_POST[$getName])) {
            return $_POST[$getName];
        }
        else {
            return "";
        }
    }

    //获取HEADER参数并对其进行字符串过滤
    protected function _fheader($getName) {
        return $this->_filterSQL($this->_getheader($getName));
    }

    protected function _fpost($getName) {
        return $this->_filterSQL($this->_post($getName));
    }

    //载入model
    protected function getModel($classname, $root = "") {
        if ($classname == "") {
            return;
        }
        if($root=="" && $this->root!=""){
            $root=$this->root;
        }
        if ($root == "") {
            $root = APP_ROOT;
        }

        $filename = $root . "app/model/" . $classname . "Model.php";
        //echo $filename."\r\n";
        if (file_exists($filename)) {
            require_once("./core/Model.php");
            require_once($filename);
            $className = $classname . "Model";
            return new $className;
        }
        else {
            $mod_data="<?php class ".$classname."Model  extends Model{
}
?>";
            if(!file_put_contents($filename,$mod_data)){
                $this->page_error("模块" . $classname . "不存在，请检查文件");
                return null;
                die();
            }

            require_once("./core/Model.php");
            require_once($filename);
            $className = $classname . "Model";
            return new $className;
        }
    }

    //载入controller
    protected function getController($classname, $root = "") {
        if ($classname == "") {
            return;
        }
        if ($root == "") {
            $root = APP_ROOT;
        }
        else{
            $this->root=$root;
        }

        $filename = $root . "app/controller/" . $classname . "Controller.php";

        if (file_exists($filename)) {
            require_once("./core/Controller.php");
            require_once($filename);
            $className = $classname . "Controller";
            return new $className(1,$root);
        }
        else {
            $this->page_error("控制器" . $classname . "不存在，请检查文件");
            die();
            return null;
        }
    }

    //载入controller简写
    protected function C($classname, $root = "") {
        return $this->getController($classname, $root);
    }

    //载入Lib
    protected function getLib($classname, $dir = "") {
        $filename = "./lib/" . $dir . $classname . "Lib.php";
        if (file_exists($filename)) {
            require_once($filename);
            $className = $classname . "Lib";
            return new $className;
        }
        else {
            echo "<br/><font color='red'>第三方Lib库" . $filename . "不存在，请检查文件</font>";
            return null;
        }
    }

    //载入Lib简写
    protected function L($classname, $dir = "") {
        return $this->getLib($classname);
    }

    //载入工具类
    protected function getUtil($UtilClass) {
        $filename = "./core/Util/" . $UtilClass . "Util.php";
        if (file_exists($filename)) {
            require_once($filename);
            $className = $UtilClass . "Util";
            return new $className;
        }
        else {
            echo "<br/><font color='red'>工具类" . $UtilClass . "不存在，请检查文件</font>";
            return null;
        }
    }

    //载入工具类简写
    protected function U($UtilClass) {
        return $this->getUtil($UtilClass);
    }

    //处理手机号
    protected function mobile($mobile) {
        //只保留加号和数字
        $mobile = preg_replace('/[^\+^\d]/mi', '', $mobile);

        //去除非第一位的加号
        if (substr($mobile, 0, 1) != "+") {               //第一位不是加号时去除所有加号
            $mobile = str_replace("+", "", $mobile);
        }
        else {                                                     //第一位是加号时去除后面所有加号
            $mobile = "+" . str_replace("+", "", $mobile);
        }

        //判断是否为+86国内手机号
        if (strlen($mobile) == 14 && substr($mobile, 0, 3) == "+86") {
            $mobile = str_replace("+86", "", $mobile);
            return $mobile;
        }

        //判断是否为11位国内手机号
        if (substr($mobile, 0, 1) == "1" && strlen($mobile) == 11) {
            return $mobile;
        }

        //非国内手机号处理
        $mobile = str_replace("+", "", $mobile);    //去除加号

        //防止其他系统多处理了00
        while (substr($mobile, 0, 4) == "0000") {
            $mobile = substr($mobile, 2);
        }

        //首字符不包含00补齐
        if (substr($mobile, 0, 2) != "00") {
            $mobile = "00" . $mobile;
            return $mobile;
        }

        return $mobile;
    }

    //载入模板
    protected function __tpl($tplname, $data = array()) {
        $data['_m'] = $this->_m;
        $data['_c'] = $this->_c;
        if ($tplname != "") {
            $filename = APP_ROOT . "app/tpl/" . $tplname;
            if (file_exists($filename)) {
                extract($data);
                require($filename);
            }
            else {
                $this->page_error("模板文件" . $tplname . "不存在");
                die();
            }
        }
    }

    /**********默认 ctl  方法**********/
    public function alllist() {
        $alllist = $this->M->getAlllist("", "", $this->con);
        $data = array("alllist" => $alllist);
        $this->__tpl($this->_classname . "/alllist.tpl", $data);
    }

    public function add() {
        $this->__tpl($this->_classname . "/add.tpl.php");
    }

    public function edit() {
        $tid = (float)$this->_fget("id");
        if ($tid == "") {
            header("location:?c=" . $this->_classname . "&m=alllist");
        }
        $info = $this->M->getInfo($tid, $this->con);
        $this->checkInfoExist($info);
        $data = array("info" => $info[0]);
        $this->__tpl($this->_classname . "/edit.tpl.php", $data);
    }

    public function getOne($checkType = "url") {
        $tid = $this->_fpost("id");
        if ($tid == "") {
            echo json_encode(array("error" => "201", "info" => "参数不全"));
            return;
        }
        $ctype = $this->_fpost("id");
        if ($ctype != "") {
            $checkType = $ctype;
        }
        $info = $this->M->getInfo($tid, $this->con);
        $this->checkInfoExist($info, "", $checkType);
        $data = array("error" => "0", "info" => $info[0]);
        echo json_encode($data);
    }

    public function del() {
        $tid = $this->_fpost("tid");
        if ($tid == "") {
            echo json_encode(array("error" => "1", "info" => "参数不全"));
        }
        else {
            $this->M->doDel($tid, $this->con);
        }
    }

    //判断修改的id是否存在
    public function checkInfoExist($arr, $url = "", $checkInfoType = "url") {
        if ($url == "") {
            $url = "index.php?c=" . $this->_c . "&m=alllist";
        }
        if ($checkInfoType == "url") {
            if (count($arr) == 0) {
                header("Location:" . $url);
            }
        }
        else {
            if (count($arr) == 0) {
                echo json_encode(array("error" => "202", "info" => "该记录不存在"));
                die();
            }
        }
    }

    public static function ajaxDecode($str) {
        $str = str_replace("|Ds_bfh|", "%", $str);
        $str = str_replace("|Ds_dyh|", "'", $str);
        $str = str_replace("|Ds_syh|", "\"", $str);
        $str = str_replace("|Ds_kg|", " ", $str);
        $str = str_replace("|Ds_zjt|", "<", $str);
        $str = str_replace("|Ds_yjt|", ">", $str);
        $str = str_replace("|Ds_and|", "&", $str);
        $str = str_replace("|Ds_tab|", "\t", $str);
        $str = str_replace("|Ds_plus|", "+", $str);
        return $str;
    }

    public static function ajaxEncode($str) {
        $str = str_replace("%", "|Ds_bfh|", $str);
        $str = str_replace("'", "|Ds_dyh|", $str);
        $str = str_replace("\"", "|Ds_syh|", $str);
        $str = str_replace(" ", "|Ds_kg|", $str);
        $str = str_replace("<", "|Ds_zjt|", $str);
        $str = str_replace(">", "|Ds_yjt|", $str);
        $str = str_replace("&", "|Ds_and|", $str);
        $str = str_replace("\t", "|Ds_tab|", $str);
        $str = str_replace("\\", "|Ds_fxg|", $str);
        $str = str_replace("+", "|Ds_plus|", $str);
        return $str;
    }

    public function ajaxDecodeArr($arr) {
        foreach ($arr as $k => $v) {
            $arr[$k] = $this->ajaxDecode($v);
        }
        return $arr;
    }

    //从查询出来的集合中提取某一字段的全部值，并合并为数据库查询语句中的条件
    public function listGetFields($list = array(), $filedname = "", $whereField = "id", $ifnot = "") {
        if (!is_array($list)) {
            $list = array();
        }
        $tempArr = array();
        if ($ifnot == "") {
            foreach ($list as $k => $v) {
                if (!in_array($v[$filedname], $tempArr)) {
                    $tempArr[] = $v[$filedname];
                }
            }
        }
        else {
            foreach ($list as $k => $v) {
                if ($v[$filedname] != $ifnot) {
                    if (!in_array($v[$filedname], $tempArr)) {
                        $tempArr[] = $v[$filedname];
                    }
                }
            }
        }

        $sql = "";
        foreach ($tempArr as $k => $v) {
            $sql .= $whereField . "='" . $v . "' or ";
        }
        $sql .= "DOSHARE";
        $sql = str_replace("or DOSHARE", "", $sql);
        $sql = "(" . $sql . ")";

        if ($sql == "(DOSHARE)") {
            $sql = "";
        }

        return $sql;
    }

    public function request_post_json($url = "", $data = "",$header=['Content-Type: application/json']) {
        if ($url == "" || $data == "") {
            return "";
        }

        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($ch, CURLOPT_POST, 1);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $data);
        curl_setopt($ch, CURLOPT_HTTPHEADER, $header);

        return curl_exec($ch);
    }

    public function request_post_from($url = "", $data = "") {
        if ($url == "" || $data == "") {
            return "";
        }

        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($ch, CURLOPT_POST, 1);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $data);
        curl_setopt($ch, CURLOPT_HTTPHEADER, array('application/x-www-form-urlencoded; charset=UTF-8'));

        return curl_exec($ch);
    }

    public function request_del_json($url = "") {
        if ($url == "") {
            return "";
        }

        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, 'DELETE');
        curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type: application/json'));
        return curl_exec($ch);
    }

    public function request_put_json($url = "", $data = "") {
        if ($url == "") {
            return "";
        }

        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, 'PUT');
        curl_setopt($ch, CURLOPT_POSTFIELDS, $data); //设置请求体，提交数据包
        curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type: application/json'));
        return curl_exec($ch);
    }

    public function request_get($url, $timeout = 10, $header = "") {
        $opts = array(
            "http" => array(
                "method"  => "GET",
                "timeout" => $timeout,
                "header"  => $header
            ),
        );
        $context = stream_context_create($opts);
        $result = file_get_contents($url, false, $context);
        return $result;
    }

    public function arr_change_by_idx($arr, $idx) {
        $arr2 = [];
        foreach ($arr as $k => $v) {
            @$arr2[$v[$idx]] = $v;
        }
        return $arr2;
    }

    public function ajax_common_header() {
        header('Access-Control-Allow-Origin:*');
        header('content-type:application/json;charset=utf-8');
        header('Access-Control-Allow-Headers:DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Content-Range,Range,username,password,token,tokenid,lazyUpdate,terminal,appname,channel');
    }

    public function page_error($err) {
        $this->__tpl("index/page_error.tpl.php",["err"=>$err]);
        die();
    }

    public function page_info($info = "提示", $err) {
        $this->__tpl("index/page_info.tpl.php",["err"=>$err,"info"=>$info]);
        die();
    }

    //跳转到指定页面， is_record_now_url是否记录原url地址
    public function redirect_url($url,$is_record_now_url=1){
        if($is_record_now_url==1){
            $_SESSION[SYS_SIGN."redirect_pre_url"]=WEBURL.$_SERVER['REQUEST_URI'];
        }
        header("Location:".$url);
    }

    protected function _cache($dir,$name,$content){
        $fDir = BASE_DIR . "data".DIRECTORY_SEPARATOR."cache" . DIRECTORY_SEPARATOR .$dir ;
        if(!file_exists($fDir)){
           @mkdir($fDir);
        }
        $fPath=$fDir.DIRECTORY_SEPARATOR.$name.".data";
        file_put_contents($fPath,$content);
    }

    protected function get_cache($dir,$name){
        $fDir = BASE_DIR ."data".DIRECTORY_SEPARATOR. "cache" . DIRECTORY_SEPARATOR .$dir ;
        $fPath=$fDir.DIRECTORY_SEPARATOR.$name.".data";
        return @file_get_contents($fPath);
    }

    protected function del_cache($dir,$name){
        $fDir = BASE_DIR . "data".DIRECTORY_SEPARATOR."cache" . DIRECTORY_SEPARATOR .$dir ;
        $fPath=$fDir.DIRECTORY_SEPARATOR.$name.".data";
        if(unlink($fPath)){return true;}
        return false;
    }

    protected function out_str($str,$color=""){
        echo "<font color='".$color."'>".$str."</font><br/>";
    }
}

?>
