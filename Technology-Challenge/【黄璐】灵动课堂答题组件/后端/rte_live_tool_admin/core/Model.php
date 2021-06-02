<?php

class Model {
    public $_con;
    public $_tablename;

    //构造方法
    public function __construct () {
        $this->_tablename = str_replace("Model", "", get_class($this));
        $this->start();
    }

    public function start () {

    }

    //载入Lib
    protected function getLib ($classname, $dir = "") {
        $filename = "./lib/" . $dir . $classname . "Lib.php";
        if (file_exists($filename)) {
            require_once($filename);
            $className = $classname . "Lib";
            return new $className;
        }
        else {
            echo "<br/><font color='red'>第三方Lib库" . $filename . "不存在，请检查文件</font>";
            return NULL;
        }
    }

    public function execSql ($sql, $con) {
        if (@$_GET['debug_sql'] == "ASMITA") {
            echo $sql;
        }
        $re   = mysqli_query($con, $sql);
        $list = [];
        while ($row = mysqli_fetch_object($re)) {
            $list[] = $this->object_to_array($row);
        }
        return $list;
    }

    private function object_to_array ($obj) {
        $obj = (array)$obj;
        foreach ($obj as $k => $v) {
            if (gettype($v) == 'resource') {
                return;
            }
            if (gettype($v) == 'object' || gettype($v) == 'array') {
                $obj[$k] = (array)object_to_array($v);
            }
        }

        return $obj;
    }


    public function getCount ($where = "", $con = "") {
        if ($con == "") {
            $con = $this->_con;
        }
        $sql                               = "SELECT count(0) as count FROM " . $this->_tablename . " " . $where;
        $_SESSION[SYS_SIGN . '_SQLDATA'][] = $sql;
        //echo $sql."<br/>";
        $re    = mysqli_query($con, $sql);
        $count = 0;
        while ($row = mysqli_fetch_array($re)) {
            $count = $row['count'];
        }
        return $count;
    }

    public function getInfo ($tid, $con = "") {

        if ($con == "") {
            $con = $this->_con;
        }

        $sql                               = "select * from " . $this->_tablename . " where id='" . $tid . "'";
        $_SESSION[SYS_SIGN . '_SQLDATA'][] = $sql;
        //echo $sql."<br/>";
        $re  = mysqli_query($con, $sql);
        $out = [];
        while ($row = mysqli_fetch_array($re)) {
            $out[] = $row;
        }
        return $out;
    }

    public function getAlllist ($where = "", $limit = "", $con = "", $order = " order by id DESC ") {
        if ($con == "") {
            $con = $this->_con;
        }
        $sql                               = "SELECT * FROM " . $this->_tablename . " " . $where . " " . $order . " " .
                                             $limit;
        $_SESSION[SYS_SIGN . '_SQLDATA'][] = $sql;
        //		echo $sql."<br/>";
        $re  = mysqli_query($con, $sql);
        $out = [];
        while ($row = mysqli_fetch_array($re)) {
            $out[] = $row;
        }
        return $out;
    }

    public function doDel ($tid, $con = "") {
        if ($con == "") {
            $con = $this->_con;
        }
        $sql                               = "delete from " . $this->_tablename . " where id='" . $tid . "'";
        $_SESSION[SYS_SIGN . '_SQLDATA'][] = $sql;
        $re                                = mysqli_query($con, $sql);
        if (!$re) {
            echo json_encode(["error" => "701",
                              "info"  => "sql语句执行失败",
                              "sql"   => $sql]);
        }
        else {
            echo json_encode(["error" => "0",
                              "info"  => "删除成功"]);
        }
    }

    public function doDelByWhere ($where, $con) {
        $sql                               = "delete from " . $this->_tablename . " where " . $where;
        $_SESSION[SYS_SIGN . '_SQLDATA'][] = $sql;
        mysqli_query($con, $sql);
    }

    public function dataArrToSqlUpdate ($data, $filterArr = []) {
        $updateStr = "";
        foreach ($data as $key => $value) {
            $fState = 0;
            if (count($filterArr) > 0) {
                foreach ($filterArr as $fkey => $fvalue) {
                    if ($fvalue == $key) {
                        $fState = 1;
                    }
                }
                if ($fState == 0) {
                    $updateStr .= $key . "='" . $value . "',";
                }
            }
            else {
                $updateStr .= $key . "='" . $value . "',";
            }


        }
        $updateStr .= "_Ds";
        $updateStr = str_replace(",_Ds", "", $updateStr);
        return $updateStr;
    }

    public function dataArrToSqlInsert ($data, $filterArr = [], $addArr = []) {
        $keyStr   = "";
        $valueStr = "";
        foreach ($data as $key => $value) {
            $fState = 0;
            if (count($filterArr) > 0) {
                foreach ($filterArr as $fkey => $fvalue) {
                    if ($fvalue == $key) {
                        $fState = 1;
                    }
                }
                if ($fState == 0) {
                    $keyStr   .= $key . ",";
                    $valueStr .= "'" . addslashes($value) . "',";
                }
            }
            else {
                $keyStr   .= $key . ",";
                $valueStr .= "'" . addslashes($value) . "',";
            }
        }

        if (count($addArr) > 0) {
            foreach ($addArr as $key => $value) {
                $keyStr   .= $key . ",";
                $valueStr .= "'" . addslashes($value) . "',";
            }
        }

        $keyStr   .= "_Ds";
        $valueStr .= "_Ds";

        $keyStr   = str_replace(",_Ds", "", $keyStr);
        $valueStr = str_replace(",_Ds", "", $valueStr);
        return "(" . $keyStr . ") values (" . $valueStr . ")";
    }

    //载入工具类
    protected function getUtil ($UtilClass) {
        $filename = "./core/Util/" . $UtilClass . "Util.php";
        if (file_exists($filename)) {
            require_once($filename);
            $className = $UtilClass . "Util";
            return new $className;
        }
        else {
            echo "<br/><font color='red'>工具类" . $UtilClass . "不存在，请检查文件</font>";
            return NULL;
        }
    }


    //载入model
    protected function getModel ($classname, $root = "") {
        if ($root == "") {
            $root = APP_ROOT;
        }
        $filename = $root . "app/model/" . $classname . "Model.php";
        if (file_exists($filename)) {
            require_once("./core/Model.php");
            require_once($filename);
            $className = $classname . "Model";
            return new $className;
        }
        else {
            if (@$_GET['s'] != 1) {
                echo "<br/><font color='red'>模块" . $classname . "不存在，请检查文件</font>";
            }
            return NULL;
        }
    }

    //载入model
    public function checkIdExist ($id, $tableName, $con) {

        if ($con == "") {
            $con = $this->_con;
        }

        $sql                               = "select * from " . $tableName . " where id='" . $id . "'";
        $_SESSION[SYS_SIGN . '_SQLDATA'][] = $sql;
        $re                                = mysqli_query($con, $sql);
        if (mysqli_num_rows($re) > 0) {
            return TRUE;
        }
        else {
            return FALSE;
        }
    }

    public function checkStr ($find, $str) {
        $tmp = explode($find, $str);
        if (count($tmp) > 1) {
            return TRUE;
        }
        return FALSE;
    }

    public function request_get_json ($url = "") {
        if ($url == "") {
            return "";
        }

        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($ch, CURLOPT_HTTPHEADER, ['Content-Type:application/json',
                                              "Authorization:HCe4fVsOypZk3v9W"]);
        return curl_exec($ch);
    }

    public function request_post_json($url = "", $data = "",$header=['Content-Type: application/json']) {
        if ($url == "" ) {
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

    public function request_del_json ($url = "") {
        if ($url == "") {
            return "";
        }

        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, 'DELETE');
        curl_setopt($ch, CURLOPT_HTTPHEADER, ['Content-Type: application/json']);
        return curl_exec($ch);
    }

    public function request_put_json ($url = "", $data = "") {
        if ($url == "") {
            return "";
        }

        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, 'PUT');
        curl_setopt($ch, CURLOPT_POSTFIELDS, $data); //设置请求体，提交数据包
        curl_setopt($ch, CURLOPT_HTTPHEADER, ['Content-Type: application/json']);
        return curl_exec($ch);
    }
}

?>