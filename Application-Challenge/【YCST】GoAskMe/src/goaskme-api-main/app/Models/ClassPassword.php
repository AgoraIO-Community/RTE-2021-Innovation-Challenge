<?php

namespace App\Models;

use App\Helpers;
use Encore\Admin\Traits\DefaultDatetimeFormat;
use Illuminate\Contracts\Auth\MustVerifyEmail;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\SoftDeletes;
use Illuminate\Foundation\Auth\User as Authenticatable;
use Illuminate\Notifications\Notifiable;
use Illuminate\Support\Facades\Storage;
use Illuminate\Support\Str;

class ClassPassword extends Authenticatable
{
    use HasFactory, Notifiable,SoftDeletes,DefaultDatetimeFormat;
    protected $table = 'class_password';
    const PASSWORD_EXPIRE = 3*60*60;   //密码失效时间

    public static function in($password,$roomname,$role = 1){
        $password = self::query()->where("password","=",$password)->first();
        if (!$password)
            return '密码错误';
        $class = Classes::query()->where("id","=",$password->class_id)->first();
        if (!$class)
            return '课程不存在或已删除';
        if (time() + 600 < strtotime($class->start_time))
            return '课程还未开始';
        if (strtotime($class->start_time) - self::PASSWORD_EXPIRE > time() )
            return '密码已失效';
        if ($password->role != $role)
            return '身份错误';
        if ($password->roomname != $roomname)
            return '密码教室名不对应';
        return true;
    }


    public static function classBuy($classid,$user){
        return self::query()->where("user_id","=",$user)->where("class_id","=",$classid)
            ->first();
    }

    public static function create($class_id,$user_id,$username,$roomname,$role=1){
        $password = Helpers::randCode();
        self::query()->insert([
            'class_id' =>$class_id,
            'user_id' =>$user_id,
            'username' =>$username,
            'roomname' =>$roomname,
            'password' =>$password,
            'role'=>$role
        ]);
        return $password;
    }
}
