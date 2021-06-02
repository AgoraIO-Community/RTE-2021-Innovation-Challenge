<?php 
//字符处理工具类
class CharUtil{
    
    /**
     * 获得随机字符串
     * $length是字符串长度
     */
    public function getRandChar($length=4,$strPol="ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz"){
       $str = null;
       $max = strlen($strPol)-1;

       for($i=0;$i<$length;$i++){
        $str.=$strPol[rand(0,$max)];
       }

       return $str;
      }
  /**
   * 检测字符串是否存在
   * @param  [string] $findstr [查找的内容]
   * @param  [string] $str     [被查找的字符串]
   * @return [boolean]          [description]
   */
  public function checkStrExsit($findstr,$str){
    if($str==""){
      return false;
    }
    $tempArr=explode($findstr,$str);
    if(count($tempArr)>1){
      return true;
    }
    else{
      return false;
    }
  }
 }
?>
