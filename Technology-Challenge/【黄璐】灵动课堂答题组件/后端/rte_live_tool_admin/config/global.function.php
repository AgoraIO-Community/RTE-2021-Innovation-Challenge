<?php
//返回当前时间
function ntime(){
    return date("Y-m-d H:i:s");
}

//检测字符串是否存在
function check_str($find, $str) {
    $tmp = explode($find, $str);
    if (count($tmp) > 1) {
        return true;
    }
    return false;
}
?>
