<?php 
//数组处理工具类
class ArrayUtil{
    
    /**
     * 按照数组内指定的字段对数组进行排序，并返回一个数组下标为指定字段的数组
     * field:被排序的字段名称，比如array()
     * Array(
     *   [0] => 
     *         Array ([id] => 5,[name] => 墙纸) 
     *   [1] => 
     *         Array ([id] => 4,[name] => 壁画) 
     * 如果指定field为id，则会按照id进行排序，并且返回数组的下标为id，而不是默认的0,1
     * 
     * order:
     *   SORT_ASC - 默认。按升序排列 (A-Z)。
     *   SORT_DESC - 按降序排列 (Z-A)。
     *   
     * sortingtype:
     *   SORT_REGULAR - 默认。把每一项按常规顺序排列（Standard ASCII，不改变类型）。
     *   SORT_NUMERIC - 把每一项作为数字来处理。
     *   SORT_STRING - 把每一项作为字符串来处理。
     *   SORT_LOCALE_STRING - 把每一项作为字符串来处理，基于当前区域设置（可通过 setlocale() 进行更改）。
     *   SORT_NATURAL - 把每一项作为字符串来处理，使用类似 natsort() 的自然排序。
     *   SORT_FLAG_CASE - 可以结合（按位或）SORT_STRING 或 SORT_NATURAL 对字符串进行排序，不区分大小写。
     */
	public function c($array,$field,$order="ASC",$sortingtype="SORT_REGULAR"){
		return $this->changeArrayByField($array,$field,$order,$sortingtype);
	}
	public function changeArrayByField($array,$field,$order="ASC",$sortingtype="SORT_REGULAR"){
      $temparray=array();
      foreach ($array as $key => $value) {
        $temparray[$array[$key][$field]]=$array[$key];
      }
      if($order=="ASC"){
        array_multisort($temparray,SORT_ASC);
      }
      elseif($order=="DESC"){
        array_multisort($temparray,SORT_DESC);
      }
      $temparray2=array();
      foreach ($temparray as $key => $value) {
        $temparray2[$temparray[$key][$field]]=$temparray[$key];
      }      
      return $temparray2;
    }
	
 }
?>
