<?php 
class adminModel{
	//检测登陆状态
	public function checkLoginState(){
		$http_type = ((isset($_SERVER['HTTPS']) && $_SERVER['HTTPS'] == 'on') || (isset($_SERVER['HTTP_X_FORWARDED_PROTO']) && $_SERVER['HTTP_X_FORWARDED_PROTO'] == 'https')) ? 'https://' : 'http://';
		if(@$_SESSION[SYS_SIGN.'adminNickname']==""){
			$_SESSION[SYS_SIGN."_admin_preurl"]=$http_type.$_SERVER['HTTP_HOST'].$_SERVER["REQUEST_URI"];
			return false;
			}
		else{
			return true;
			}
		}

	//执行修改密码Ajax
	public function doChangePassword($spwd,$pwd,$con){
		$sql="select pwd from admin where id='".$_SESSION[SYS_SIGN."id"]."'";

		$re=mysqli_query($con,$sql);
		if(!$re){
			echo json_encode(array("error"=>"701","info"=>"sql语句执行失败"));
		}
		else{
			if(mysqli_num_rows($re)==0){
				echo json_encode(array("error"=>"701","info"=>"账号不存在"));
				return;
			}

			while($row=mysqli_fetch_array($re)){
				if($row['pwd']!=md5(md5($spwd).SYS_PWDHASH)){
					echo json_encode(array("error"=>"3","info"=>"原密码输入错误","sql"=>$sql));
				}
				else{
					//更新密码
					$sql="update admin set pwd='".md5(md5($pwd).SYS_PWDHASH)."' where id='".$_SESSION[SYS_SIGN."id"]."'";
					if(mysqli_query($con,$sql)){
						echo json_encode(array("error"=>"0","info"=>"修改成功"));
					}
					else{
						echo json_encode(array("error"=>"701","info"=>"sql语句执行失败"));
					}
				}
			}
		}
	}
}
?>
