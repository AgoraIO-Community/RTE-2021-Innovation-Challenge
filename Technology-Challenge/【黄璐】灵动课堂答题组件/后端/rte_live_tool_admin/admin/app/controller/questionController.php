<?php
/**
 * Class questionController
 * 题目管理
 */
class questionController extends Controller{
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

    public function add(){
        $this->__tpl($this->_classname."/add.tpl.php",[]);
    }

    public function doAdd(){
        $con=$this->con;
        $raw = @file_get_contents('php://input');
        if ($raw == "") {
            $this->return_json(100, "未获取到参数");
            return;
        }
        $data = @json_decode($raw, 1, 512, JSON_BIGINT_AS_STRING);

        $title=@$data['title'];
        $stype = @(int)$data['stype'];
        $options = @$data['options'];
        $answer = @$data['answer'];
        if($title==""){
            $this->return_json(101, "请输入标题");
            return;
        }
        if($stype==0){
            $this->return_json(101, "请输入题型");
            return;
        }
        if($stype!=2 && $options==""){
            $this->return_json(101, "请输入选项");
            return;
        }
        if($stype==2 && $answer==0){
            $this->return_json(101, "请输入判断题答案");
            return;
        }
        if($stype==2 && ($answer<1 || $answer>2)){
            $this->return_json(101, "判断题答案为1或2");
            return;
        }

        $uid=$_SESSION[SYS_SIGN."id"];
        $sql="insert into question set title='".addslashes($title)."',stype='".addslashes($stype)."',options='".addslashes($options)."',answer='".$answer."',addtime='".ntime()."',uid='".$uid."'";
        if(!mysqli_query($con,$sql)){
            $this->return_json(701,"SQL语句执行失败",$sql);
            return;
        }

        $this->return_json(200,"ok");
    }

    public function edit(){
        $tid=(int)$this->_fget("id");
        if($tid==""){
            $this->page_error("缺少参数");
            return;
        }

        $con=$this->con;
        $sql="select * from question where id='".$tid."'";
        $info=$this->M->execSql($sql,$con);
        if(count($info)==0){
            $this->page_error("记录不存在或已删除");
            return;
        }

        if($info[0]['uid']!=$_SESSION[SYS_SIGN."id"]){
            $this->page_error("您无权操作该记录");
            return;
        }

        $this->__tpl($this->_classname."/edit.tpl.php",[
            "info"=>$info[0]
        ]);
    }

    public function doEdit(){
        $con=$this->con;
        $raw = @file_get_contents('php://input');
        if ($raw == "") {
            $this->return_json(100, "未获取到参数");
            return;
        }
        $data = @json_decode($raw, 1, 512, JSON_BIGINT_AS_STRING);

        $id=@(int)$data['id'];
        $title=@$data['title'];
        $stype = @(int)$data['stype'];
        $options = @$data['options'];
        $answer = @$data['answer'];
        if($title==""){
            $this->return_json(101, "请输入标题");
            return;
        }
        if($stype==0){
            $this->return_json(101, "请输入题型");
            return;
        }
        if($stype!=2 && $options==""){
            $this->return_json(101, "请输入选项");
            return;
        }
        if($stype==2 && $answer==0){
            $this->return_json(101, "请输入判断题答案");
            return;
        }
        if($stype==2 && ($answer<1 || $answer>2)){
            $this->return_json(101, "判断题答案为1或2");
            return;
        }

        $sql="select * from question where id='".$id."'";
        $info=$this->M->execSql($sql,$con);
        if(count($info)==0){
            $this->return_json(101, "记录不存在或已删除");
            return;
        }

        if($info[0]['uid']!=$_SESSION[SYS_SIGN."id"]){
            $this->return_json(101, "您无权操作该记录");
            return;
        }

        $uid=$_SESSION[SYS_SIGN."id"];
        $sql="update question set title='".addslashes($title)."',stype='".addslashes($stype)."',options='".addslashes($options)."',answer='".$answer."',uid='".$uid."' where id='".$id."'";
        if(!mysqli_query($con,$sql)){
            $this->return_json(701,"SQL语句执行失败",$sql);
            return;
        }

        $this->return_json(200,"ok");
    }

	public function alllist(){
	    $con=$this->con;

		$where="where uid=".$_SESSION[SYS_SIGN."id"];
		$keyword=$this->_fget("keyword");
		if($keyword!=""){
			$where.=" and title like '%".addslashes($keyword)."%'";
		}

		$pageurl="keyword=".$keyword."&";

		$perpage=10;		//设置每页显示数
        $sql="select count(0) as c from question ".$where;
		$count=$this->M->execSql($sql,$con)[0]['c'];

		$sql="select * from question ".$where." order by id desc ".$this->getPageSql($perpage);

		$alllist=$this->M->execSql($sql,$con);
		$pageUtil=$this->getUtil("Page");
		$nowpage=1;
		if(isset($_GET['page'])&&is_numeric($_GET['page'])&&$_GET['page']>0){
			$nowpage=$_GET['page'];
		}
		
		$pageHtml=$pageUtil->getPageStr("?c=".$this->_c."&m=".$this->_m."&".$pageurl,$count,$perpage,$nowpage);
				
		$data=array("alllist"=>$alllist,"allcount"=>$count,"pageHtml" => $pageHtml);
		$this->__tpl($this->_classname."/alllist.tpl.php",$data);
	}
}
?>