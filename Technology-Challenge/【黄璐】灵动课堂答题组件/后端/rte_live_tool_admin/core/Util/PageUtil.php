<?php 
//分页工具类
class PageUtil{

    /*
     * 获取分页字符串
     * @param1 string $uri，分页要请求的脚本url
     * @param3 int $counts，总记录数
     * @param4 int $length，每页显示的记录数
     * @param5 int $page = 1，当前页码
     * @return string，带有a标签的，可以点击发起请求的字符串
    */
    public static function getPageStr($uri,$counts,$length,$page = 1){
      //构造一个能够点击的字符串
      //得到数据显示的字符串
      $pagecount = ceil($counts/$length);        //总页数
      $str_info = "<p id='pageInfo_doshare'>共有<i class='pageallnum'>{$counts}</i>条记录，每页显示<i class='pepage'>{$length}</i>条，共<i class='allpage'>{$pagecount}</i>页，当前是第<i class='nowpage'>{$page}</i>页</p>";
      //生成可以操作的连接：首页 上一页 下一页 末页
      //求出上一页和下一页页码
      $prev = ($page <= 1) ? 1 : $page - 1;
      $next = ($page >= $pagecount) ? $pagecount : $page + 1;
      $str_click = '<a href="'.$uri.'page=1" id="firstPage_doshare">首页</a>
        <a href="'.$uri.'page='.$prev.'" id="prevPage_doshare">上一页</a>';
      $str_click2='
        <a href="'.$uri.'page='.$next.'" id="nextPage_doshare">下一页</a>
        <a href="'.$uri.'page='.$pagecount.'" id="lastPage_doshare">末页</a>';
      //按照页码分页字符串
      $str_number = '';
      $stoppage=7;
      $startpage=1;
      if($page-6<1){
        $startpage=1;
      }
      else{
        if($page+6>$pagecount){
          $startpage=$pagecount-6;
        }
        else{        
          $startpage=$page-2;
        }
      }


      if($page<7){
        $stoppage=7;
        if($stoppage>$pagecount){
          $stoppage=$pagecount;
        }       
      }
      else{
        //最后6页码
        if($page+6>$pagecount){
          $stoppage=$pagecount;
        }
        else{
          if($page+4>$pagecount){
            $stoppage=$pagecount;
          }
          else{
            $stoppage=$page+4;
          }
        }
      }

      for($i = $startpage; $i <= $stoppage;$i++){
        if($i==$page){
          $str_number .= "<a href='{$uri}page={$i}' class='act'>{$i}</a> ";
        }
        else{
          $str_number .= "<a href='{$uri}page={$i}'>{$i}</a> ";  
        }
        
      }
      //下拉框分页字符串：利用js的onchang事件来改变当前脚本的href
      $str_select = "<select onchange=\"location.href='{$uri}page='+this.value\" id='pageselect_doshare'>";
      //将所有的页码放入到option
      for($i = 1;$i <= $pagecount;$i++){
        if($i == $page)
          $str_select .= "<option value='{$i}' selected='selected'>{$i}</option>";
        else
          $str_select .= "<option value='{$i}'>{$i}</option>";
      }
      $str_select .= "</select>";
      $str_allinfo="<div style='display:none'><span id='allpagenum_doshare'>".$pagecount."</span><span id='nowpage_doshare'>".$page."</span><span id='pagePreUrl'>".$uri."</span></div>";
      //返回值
      return $str_info . $str_click . $str_number.$str_click2 . $str_select.$str_allinfo;
    }
}
?>