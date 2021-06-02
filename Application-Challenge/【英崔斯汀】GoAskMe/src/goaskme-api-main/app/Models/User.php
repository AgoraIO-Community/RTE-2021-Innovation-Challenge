<?php

namespace App\Models;

use Illuminate\Contracts\Auth\MustVerifyEmail;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Foundation\Auth\User as Authenticatable;
use Illuminate\Notifications\Notifiable;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Hash;

class User extends Authenticatable
{
    use HasFactory, Notifiable;
    const TEACHER_GROUP = 11;

    public static function checkPassword($username,$password){
        $user = DB::connection("flarum")->table("users")->where("username","=",$username)
            ->orWhere("email","=",$username)
            ->first();
        if (!$user)
            return  false;
        if (Hash::check($password,$user->password)){
            $token = md5($username.time());
            if (self::query()->where("user_id","=",$user->id)->first()){
                self::query()->where("user_id","=",$user->id)->update(
                    [
                        'headimg'=>'https://goaskme.app/assets/avatars/'.$user->avatar_url,
                        'token'=>$token,
                    ]
                );
            }else{
                self::query()->insert(
                    [
                        'username'=>$user->username,
                        'user_id'=>$user->id,
                        'headimg'=>'https://goaskme.app/assets/avatars/'.$user->avatar_url,
                        'token'=>$token,
                    ]
                );
            }
            $user = self::query()->where("user_id","=",$user->id)->first();
            return [
                'token'=>$token,
                'username'=>$user->username,
                'headimg'=>$user->headimg,
            ];
        }
        return false;
    }


    public static function getUserByToken($token){
        return self::query()->where("token","=",$token)->first();
    }

    public static function userGroup($user)
    {
        return DB::connection("flarum")->table("group_user")->where("user_id","=",$user)
            ->value("group_id");
    }


    public static function checkTeacher($user){
        return DB::connection("flarum")->table("group_user")->where("user_id","=",$user)
            ->where("group_id","=",self::TEACHER_GROUP)
            ->first();
    }

}
