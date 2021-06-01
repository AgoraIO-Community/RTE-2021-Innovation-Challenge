<?php

namespace App;


class Helpers
{
    /**
     * 把返回的数据集转换成Tree
     * @param array $list 要转换的数据集
     * @param string $pid parent标记字段
     * @param string $level level标记字段
     * @return array
     */
    public static function listToTree($list, $pk='id', $pid = 'pid', $child = '_child', $root = 0) {
        if (!is_array($list)) {
            return [];
        }

        // 创建基于主键的数组引用
        $tree = [];
        $aRefer = [];
        foreach ($list as $key => $data) {
            $aRefer[$data[$pk]] = &$list[$key];
        }
        // var_dump($aRefer);exit;
        foreach ($list as $key => $data) {
            // 判断是否存在parent
            $parentId = $data[$pid];
            if ($root === $parentId) {
                $tree[] = & $list[$key];
            } else {
                if (isset($aRefer[$parentId])) {
                    $parent = & $aRefer[$parentId];
                    $parent[$child][] = & $list[$key];
                }
            }
        }
        // var_dump($tree);exit;
        return $tree;
    }


    public static function  randCode($length = 8, $type = -1) {
        $arr = array(1 => "0123456789", 2 => "abcdefghijklmnopqrstuvwxyz", 3 => "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        if ($type == 0) {
            array_pop($arr);
            $string = implode("", $arr);
        } elseif ($type == "-1") {
            $string = implode("", $arr);
        } else {
            $string = $arr[$type];
        }
        $count = strlen($string) - 1;
        $code = '';
        for ($i = 0; $i < $length; $i++) {
            $code .= $string[rand(0, $count)];
        }
        return $code;
    }

    /**
     * Notes:
     * User: niclalalla
     * DateTime: 2020/9/15 16:11
     * @param $url 请求地址
     * @param string $params 参数
     * @return bool|string
     */
    public static function posturl($url,$data){
        $data  = json_encode($data);
        $headerArray =array("Content-type:application/json;charset='utf-8'","Accept:application/json");
        $curl = curl_init();
        curl_setopt($curl, CURLOPT_URL, $url);
        curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, FALSE);
        curl_setopt($curl, CURLOPT_SSL_VERIFYHOST,FALSE);
        curl_setopt($curl, CURLOPT_POST, 1);
        curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
        curl_setopt($curl,CURLOPT_HTTPHEADER,$headerArray);
        curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);
        $output = curl_exec($curl);
        curl_close($curl);
        return json_decode($output,true);
    }

}
