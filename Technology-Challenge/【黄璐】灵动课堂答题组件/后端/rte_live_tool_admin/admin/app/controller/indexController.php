<?php
/**
 * Class indexController
 * 默认入口页
 */
class indexController extends Controller{
	public function start(){
		//校验登陆权限
		if($this->getModel("admin")->checkLoginState()==false){
			header("location:?c=login");
		}
	}

	public  function index(){
		$this->__tpl("index/index.tpl");
	}

}
?>
