<?php
/**
 * Class adminController
 * 管理员
 */
class adminController extends Controller{
	//启动项
	public function start(){
		//校验登陆权限
		if($this->getModel("admin")->checkLoginState()==false){		
			header("location:?c=login");
		}
		//获取本类对应的model	
		$this->M=	$this->getModel(str_replace("Controller","", __CLASS__));
	}
	//修改密码页面
	public function changePassword(){
		$this->__tpl("admin/changePassword.tpl");
	}
	
	//执行修改密码Ajax
	public function doChangePassword(){
		$spwd=$this->_fpost("spwd");
		$pwd=$this->_fpost("pwd");
		if($spwd==""||$pwd==""){
			echo json_encode(array("error"=>"1","info"=>"参数不全"));
		}
		else{
			$this->M->doChangePassword($spwd,$pwd,$this->DbUtil->_dbObj);
		}
	}
	
}
?>