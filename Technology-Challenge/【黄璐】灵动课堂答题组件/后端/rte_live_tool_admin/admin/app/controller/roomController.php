<?php
/**
 * Class roomController
 * 直播间管理
 */
class roomController extends Controller{
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

        $appCertificate=@$data['appCertificate'];
        $teacher_id = @$data['teacher_id'];
        $teacher_name = @$data['teacher_name'];
        $room_uuid = @$data['room_uuid'];
        $room_name = @$data['room_name'];
        $duration = @(int)$data['duration'];
        if($appCertificate==""){
            $this->return_json(101, "请输入声网appCertificate");
            return;
        }
        if($teacher_id==""){
            $this->return_json(101, "请输入老师ID");
            return;
        }
        if($teacher_name==""){
            $this->return_json(101, "请输入老师昵称");
            return;
        }
        if($room_uuid==""){
            $this->return_json(101, "请输入房间号ID");
            return;
        }
        if($room_name==""){
            $this->return_json(101, "请输入房间名称");
            return;
        }
        if($duration==0){
            $this->return_json(101, "请输入直播持续时间（秒）");
            return;
        }

        $uid=(int)$_SESSION[SYS_SIGN."id"];
        //获取用户信息
        $sql="select * from admin where id='".$uid."'";
        $admin=$this->M->execSql($sql,$con);
        if(count($admin)==0){
            $this->return_json(150, "管理员账号不存在或已删除");
            return;
        }
        $appid=$admin[0]['uname'];

        //检查是否有同名直播间
        $sql="select room_name from room where appID='".addslashes($appid)."' and room_uuid='".addslashes($room_uuid)."'";
        $check=$this->M->execSql($sql,$con);
        if(count($check)>0){
            $this->return_json(150, "已存在相同id直播间，名称为：".$check[0]['room_name']);
            return;
        }

        //新增数据
        $sql="insert into room set appID='".addslashes($appid)."',appCertificate='".addslashes($appCertificate)."',teacher_id='".addslashes($teacher_id)."',teacher_name='".addslashes($teacher_name)."',room_uuid='".addslashes($room_uuid)."',room_name='".addslashes($room_name)."',duration='".$duration."',addtime='".ntime()."',create_uid='".$uid."'";
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
        $sql="select * from room where id='".$tid."'";
        $info=$this->M->execSql($sql,$con);
        if(count($info)==0){
            $this->page_error("记录不存在或已删除");
            return;
        }

        if($info[0]['create_uid']!=$_SESSION[SYS_SIGN."id"]){
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
        $appCertificate=@$data['appCertificate'];
        $teacher_id = @$data['teacher_id'];
        $teacher_name = @$data['teacher_name'];
        $room_uuid = @$data['room_uuid'];
        $room_name = @$data['room_name'];
        $duration = @(int)$data['duration'];
        if($id==0){
            $this->return_json(101, "未发现记录");
            return;
        }
        if($appCertificate==""){
            $this->return_json(101, "请输入声网appCertificate");
            return;
        }
        if($teacher_id==""){
            $this->return_json(101, "请输入老师ID");
            return;
        }
        if($teacher_name==""){
            $this->return_json(101, "请输入老师昵称");
            return;
        }
        if($room_uuid==""){
            $this->return_json(101, "请输入房间号ID");
            return;
        }
        if($room_name==""){
            $this->return_json(101, "请输入房间名称");
            return;
        }
        if($duration==0){
            $this->return_json(101, "请输入直播持续时间（秒）");
            return;
        }

        $sql="select id,appID,create_uid from room where id='".$id."'";
        $info=$this->M->execSql($sql,$con);
        if(count($info)==0){
            $this->return_json(101, "记录不存在或已删除");
            return;
        }
        $appid=$info[0]['appID'];

        if($info[0]['create_uid']!=$_SESSION[SYS_SIGN."id"]){
            $this->return_json(101, "您无权操作该记录");
            return;
        }

        //检查是否有同名直播间
        $sql="select room_name from room where appID='".addslashes($appid)."' and room_uuid='".addslashes($room_uuid)."' and id<>'".$id."'";
        $check=$this->M->execSql($sql,$con);
        if(count($check)>0){
            $this->return_json(150, "已存在相同id直播间，名称为：".$check[0]['room_name']);
            return;
        }

        $sql="update room set appCertificate='".addslashes($appCertificate)."',teacher_id='".addslashes($teacher_id)."',teacher_name='".addslashes($teacher_name)."',room_uuid='".addslashes($room_uuid)."',room_name='".addslashes($room_name)."',duration='".$duration."',updatetime='".ntime()."' where id='".$id."'";
        if(!mysqli_query($con,$sql)){
            $this->return_json(701,"SQL语句执行失败",$sql);
            return;
        }

        $this->return_json(200,"ok");
    }

	public function alllist(){
	    $con=$this->con;

		$where="where create_uid=".$_SESSION[SYS_SIGN."id"];
		$keyword=$this->_fget("keyword");
		if($keyword!=""){
			$where.=" and room_name like '%".addslashes($keyword)."%'";
		}

		$pageurl="keyword=".$keyword."&";

		$perpage=10;		//设置每页显示数
        $sql="select count(0) as c from room ".$where;
		$count=$this->M->execSql($sql,$con)[0]['c'];

		$sql="select * from room ".$where." order by id desc ".$this->getPageSql($perpage);

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