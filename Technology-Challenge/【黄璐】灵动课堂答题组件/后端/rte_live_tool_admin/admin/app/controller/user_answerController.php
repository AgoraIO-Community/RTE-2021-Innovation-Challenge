<?php
/**
 * Class user_answerController
 * 答题情况
 */
class user_answerController extends Controller{
	public function start(){
		//校验登陆权限
		if($this->getModel("admin")->checkLoginState()==false){		
			header("location:?c=login");
		}
		//获取本类对应的model	
		$this->M=	$this->getModel(str_replace("Controller","", __CLASS__));
	}

	public function del(){
	    $this->return_json(101,"比赛期间该功能暂不开放");
    }

    public function detail(){
        $con=$this->con;

        $id=(int)$this->_fget("id");
        if($id==0){
            $this->page_error("缺少参数id");
            return;
        }

        //获取用户appid
        $uid=(int)$_SESSION[SYS_SIGN."id"];
        $sql="select * from admin where id='".$uid."'";
        $admin=$this->M->execSql($sql,$con);
        if(count($admin)==0){
            $this->return_json(150, "管理员账号不存在或已删除");
            return;
        }
        $appid=$admin[0]['uname'];

        //获取推送题目信息
        $sql="select * from send_question where id='".$id."'";
        $send_question=$this->M->execSql($sql,$con);
        if(count($send_question)==0){
            $this->page_error("推送信息不存在");
            return;
        }
        $send_question=$send_question[0];
        if($send_question['appid']!=$appid){
            $this->page_error("您无权查看该推送数据详情");
            return;
        }

        $room_id=$send_question['room_id'];
        $qid=$send_question['qid'];

        //获取题目信息
        $sql="select * from question where id='".$qid."'";
        $question=$this->M->execSql($sql,$con);
        if(count($question)==0){
            $this->page_error("题目不存在或已删除");
            return;
        }

        $where="where appid='".addslashes($appid)."' and room_id='".addslashes($room_id)."' and sqid='".$id."' ";
        $keyword=$this->_fget("keyword");
        if($keyword!=""){
            $where.=" and username like '%".addslashes($keyword)."%'";
        }

        $pageurl="keyword=".$keyword."&id=".$id."&";

        $perpage=10;		//设置每页显示数
        $sql="select count(0) as c from user_answer ".$where;
        $count=$this->M->execSql($sql,$con)[0]['c'];

        $sql="select * from user_answer ".$where." order by id desc ".$this->getPageSql($perpage);

        $alllist=$this->M->execSql($sql,$con);
        $pageUtil=$this->getUtil("Page");
        $nowpage=1;
        if(isset($_GET['page'])&&is_numeric($_GET['page'])&&$_GET['page']>0){
            $nowpage=$_GET['page'];
        }

        $pageHtml=$pageUtil->getPageStr("?c=".$this->_c."&m=".$this->_m."&".$pageurl,$count,$perpage,$nowpage);

        $data=array("question"=>$question[0],"alllist"=>$alllist,"allcount"=>$count,"pageHtml" => $pageHtml);
        $this->__tpl($this->_classname."/detail.tpl.php",$data);
    }

	public function alllist(){
	    $con=$this->con;

	    $id=(int)$this->_fget("id");
	    if($id==0){
	        $this->page_error("缺少参数id");
	        return;
        }

	    //获取直播间信息
        $sql="select * from room where id='".$id."'";
	    $room=$this->M->execSql($sql,$con);
	    if(count($room)==0){
            $this->page_error("直播间不存在");
            return;
        }
        $room=$room[0];
	    if($room['create_uid']!=$_SESSION[SYS_SIGN."id"]){
            $this->page_error("您无权查看该直播间信息");
            return;
        }
	    $appid=$room['appID'];
        $room_id=$room['room_uuid'];
        $room_name=$room['room_name'];

		$where="where appid='".addslashes($appid)."' and room_id='".addslashes($room_id)."' ";
		$keyword=$this->_fget("keyword");
		if($keyword!=""){
			$where.=" and username like '%".addslashes($keyword)."%'";
		}

		$pageurl="keyword=".$keyword."&id=".$id."&";

		$perpage=10;		//设置每页显示数
        $sql="select count(0) as c from send_question ".$where;
		$count=$this->M->execSql($sql,$con)[0]['c'];

		$sql="select * from send_question ".$where." order by id desc ".$this->getPageSql($perpage);

		$alllist=$this->M->execSql($sql,$con);
		$pageUtil=$this->getUtil("Page");
		$nowpage=1;
		if(isset($_GET['page'])&&is_numeric($_GET['page'])&&$_GET['page']>0){
			$nowpage=$_GET['page'];
		}

		//获取题目列表
        $where=$this->listGetFields($alllist,"qid","id");
		$question_list=[];
		if($where!=""){
		    $sql="select * from question where ".$where;
		    $question_list=$this->M->execSql($sql,$con);
            $question_list=$this->arr_change_by_idx($question_list,"id");
        }
		
		$pageHtml=$pageUtil->getPageStr("?c=".$this->_c."&m=".$this->_m."&".$pageurl,$count,$perpage,$nowpage);
				
		$data=array("room_name"=>$room_name,"question_list"=>$question_list,"alllist"=>$alllist,"allcount"=>$count,"pageHtml" => $pageHtml);
		$this->__tpl($this->_classname."/alllist.tpl.php",$data);
	}
}
?>