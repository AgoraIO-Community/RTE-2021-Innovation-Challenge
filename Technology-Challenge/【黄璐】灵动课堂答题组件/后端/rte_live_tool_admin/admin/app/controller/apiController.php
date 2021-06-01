<?php
/**
 * Class apiController
 * 对外接口类
 */
class apiController extends Controller {
    //启动项
    public function start () {
    }

    //获取全部题目
    public function get_questions () {
        $con = $this->con;
        $appid=$this->_fget("appid");

        $sql="select id from admin where uname='".addslashes($appid)."'";
        $admin=$this->M->execSql($sql,$con);
        if(count($admin)==0){
            $this->return_json(102,"账号不存在");
            return;
        }

        $uid=$admin[0]['id'];
        $sql="select * from question where uid='".$uid."'  order by id desc";
        $list=$this->M->execSql($sql,$con);

        foreach ($list as $k=>$v){
            $options=$v['options'];
            if($v['stype']!=2){
                $options=explode("\n",$options);
                $v['options']=$options;
            }

            unset($v['addtime']);
            unset($v['uid']);
            $list[$k]=$v;
        }


        $this->return_json(200,"ok",$list);

    }

    //推送题目
    public function send_question(){
        $con = $this->con;
        $appid=$this->_fget("appid");
        $room_id=$this->_fget("room_id");
        $qid=(int)$this->_fget("qid");

        //获取录入人
        $sql="select * from admin where uname='".addslashes($appid)."'";
        $admin=$this->M->execSql($sql,$con);
        if(count($admin)==0){
            $this->return_json(201,"您无权发布题目");
            return;
        }
        $admin=$admin[0];

        //获取题目权限
        $sql="select * from question where id='".$qid."'";
        $question=$this->M->execSql($sql,$con);
        if(count($question)==0){
            $this->return_json(202,"题目不存在");
            return;
        }
        $question=$question[0];

        if($question['uid']!=$admin['id']){
            $this->return_json(203,"没有操作该题目的权限");
            return;
        }

        $sql="insert into send_question set appid='".addslashes($appid)."',room_id='".addslashes($room_id)."',qid='".$qid."',addtime='".ntime()."'";
        if(!mysqli_query($con,$sql)){
            $this->return_json(700,"SQL执行错误",$sql);
            return;
        }

        $this->return_json(200,"ok");
    }

    //获取当前推送的题目
    public function get_now_question(){
        $con = $this->con;
        $appid=$this->_fget("appid");
        $room_id=$this->_fget("room_id");
        $user_id=$this->_fget("user_id");

        //获取最近的一题
        $sql="select id,qid,addtime from send_question where appid='".addslashes($appid)."' and room_id='".addslashes($room_id)."' order by id desc limit 0,1";
        $send_question=$this->M->execSql($sql,$con);
        if(count($send_question)==0){
            $this->return_json(201,"没有推送的题目");
            return;
        }
        $send_question=$send_question[0];

        $sql="select * from question where id='".$send_question['qid']."'";
        $question=$this->M->execSql($sql,$con);
        if(count($question)==0){
            $this->return_json(201,"题目不存在");
            return;
        }

        //判断客户是否回答过
        $sql="select id from user_answer where sqid='".$send_question['id']."' and user_id='".$user_id."'";
        $user_answer=$this->M->execSql($sql,$con);
        if(count($user_answer)>0){
            $this->return_json(201,"用户已经回答过了，不再推送");
            return;
        }

        $question[0]['send_id']=$send_question['id'];
        if($question[0]['stype']!=2){
            $question[0]['options']=explode("\n",$question[0]['options']);
        }

        $this->return_json(200,"ok",[
            "question"=>$question,
        ]);
        return;
    }

    //客户提交答案
    public function sub_answer(){
        $con=$this->con;
        $appid=$this->_fget("appid");
        $qid=(int)$this->_fget("qid");
        $room_id=$this->_fget("room_id");
        $user_id=$this->_fget("user_id");
        $send_id=(int)$this->_fget("send_id");
        $answer=$this->_fget("answer");
        $answer_state=(int)$this->_fget("answer_state");

        $sql="insert into user_answer set appid='".addslashes($appid)."',room_id='".addslashes($room_id)."',qid='".addslashes($qid)."',sqid='".addslashes($send_id)."',aid='".addslashes($answer)."',is_right='".$answer_state."',user_id='".$user_id."',addtime='".ntime()."'";
        if(!mysqli_query($con,$sql)){
            $this->return_json(701,"SQL语句执行失败",$sql);
            return;
        }

        $this->return_json(200,"ok");

    }

    //获取老师端令牌信息
    public function get_teacher_token(){
        $con=$this->con;
        $raw = @file_get_contents('php://input');
        if ($raw == "") {
            $this->return_json(100, "未获取到参数");
            return;
        }
        $data = @json_decode($raw, 1, 512, JSON_BIGINT_AS_STRING);
        $appid=@$data['appid'];
        $teacher_id=@$data['teacher_id'];
        $room_uuid=@$data['room_uuid'];
        if($appid==""){
            $this->return_json(101,"参数错误：appid");
            return;
        }
        if($teacher_id==""){
            $this->return_json(101,"参数错误：teacher_id");
            return;
        }
        if($room_uuid==""){
            $this->return_json(101,"参数错误：room_uuid");
            return;
        }

        //数据库查找房间信息
        $sql="select * from room where appid='".addslashes($appid)."' and room_uuid='".addslashes($room_uuid)."'";
        $room=$this->M->execSql($sql,$con);
        if(count($room)==0){
            $this->return_json(201, "房间不存在");
            return;
        }

        $room=$room[0];
        //检测老师身份信息
        if($room['teacher_id']!=$teacher_id){
            $this->return_json(201, "教师信息错误");
            return;
        }

        //获取token
        require_once("./lib/Agora/RtmTokenBuilder.php");
        $role = RtmTokenBuilder::RoleRtmUser;
        $expireTimeInSeconds = $room['duration'];
        $currentTimestamp = (new DateTime("now", new DateTimeZone('UTC')))->getTimestamp();
        $privilegeExpiredTs = $currentTimestamp + $expireTimeInSeconds;

        $token = RtmTokenBuilder::buildToken($appid, $room['appCertificate'], $teacher_id, $role, $privilegeExpiredTs);
        if($token==""){
            $this->return_json(202, "获取Token失败，请联系管理员");
            return;
        }

        //返回结果数据
        $data=[
            "token"=>$token,
            "teacher_name"=>$room['teacher_name'],
            "room_name"=>$room['room_name'],
            "duration"=>(int)$room['duration']
        ];
        $this->return_json(200,"ok",$data);
    }

    //获取学生端令牌信息
    public function get_student_token(){
        $con=$this->con;
        $raw = @file_get_contents('php://input');
        if ($raw == "") {
            $this->return_json(100, "未获取到参数");
            return;
        }
        $data = @json_decode($raw, 1, 512, JSON_BIGINT_AS_STRING);
        $appid=@$data['appid'];
        $user_id=@$data['user_id'];
        $room_uuid=@$data['room_uuid'];
        if($appid==""){
            $this->return_json(101,"参数错误：appid");
            return;
        }
        if($user_id==""){
            $this->return_json(101,"参数错误：user_id");
            return;
        }
        if($room_uuid==""){
            $this->return_json(101,"参数错误：room_uuid");
            return;
        }

        //数据库查找房间信息
        $sql="select * from room where appid='".addslashes($appid)."' and room_uuid='".addslashes($room_uuid)."'";
        $room=$this->M->execSql($sql,$con);
        if(count($room)==0){
            $this->return_json(201, "房间不存在");
            return;
        }

        $room=$room[0];

        //获取token
        require_once("./lib/Agora/RtmTokenBuilder.php");
        $role = RtmTokenBuilder::RoleRtmUser;
        $expireTimeInSeconds = $room['duration'];
        $currentTimestamp = (new DateTime("now", new DateTimeZone('UTC')))->getTimestamp();
        $privilegeExpiredTs = $currentTimestamp + $expireTimeInSeconds;

        $token = RtmTokenBuilder::buildToken($appid, $room['appCertificate'], $user_id, $role, $privilegeExpiredTs);
        if($token==""){
            $this->return_json(202, "获取Token失败，请联系管理员");
            return;
        }

        //返回结果数据
        $data=[
            "token"=>$token,
            "room_name"=>$room['room_name'],
            "duration"=>(int)$room['duration']
        ];
        $this->return_json(200,"ok",$data);
    }
}

?>