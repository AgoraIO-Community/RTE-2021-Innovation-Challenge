<?php
/**
 * Class loginController
 * 登录
 */
class loginController extends Controller{
	//显示登陆界面
	public function index(){
		$this->__tpl("login/login.tpl.php");
	}
	
	//退出登陆
	public  function loginout(){
		echo "loginout";
		$_SESSION[SYS_SIGN.'adminNickname']="";
        $_SESSION[SYS_SIGN.'adminName']="";
        $_SESSION[SYS_SIGN.'id']="";
		session_destroy();
		header("location:?c=login&m=index");
	}

	//登陆
	public function login(){
        $con=$this->con;
		$uname=$this->_fpost("username");
		$pwd=$this->_fpost("password");
		if($uname=="" || $pwd==""){
		    $this->return_json(101,"缺少参数");
		    return;
        }

		$sql="select * from admin where uname='".$uname."'";
		$admin=$this->M->execSql($sql,$con);
		if(count($admin)==0){
            $this->return_json(101,"用户名不存在或密码错误");
            return;
        }

		$admin=$admin[0];

		if(md5(md5($pwd).SYS_PWDHASH)!=$admin['pwd']){
            $this->return_json(101,"用户名不存在或密码错误",md5(md5($pwd).SYS_PWDHASH));
            return;
        }
        $_SESSION[SYS_SIGN.'adminNickname']=$admin['nickname'];
        $_SESSION[SYS_SIGN.'adminName']=$uname;
        $_SESSION[SYS_SIGN.'id']=$admin['id'];
        $this->return_json(200,"ok");
	}
}
?>