<?php

namespace App\Models;

use Encore\Admin\Traits\DefaultDatetimeFormat;
use Illuminate\Contracts\Auth\MustVerifyEmail;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\SoftDeletes;
use Illuminate\Foundation\Auth\User as Authenticatable;
use Illuminate\Notifications\Notifiable;
use Illuminate\Support\Facades\DB;

class PointsRecord extends Authenticatable
{
    use HasFactory, Notifiable,SoftDeletes,DefaultDatetimeFormat;
    protected $table = 'point_record';


    const EXCHANGE = 1;
    const INCOME = 2;
    const TAX = 3;

    public static $exchangeType = [
        self::EXCHANGE=>'积分兑换',
        self::INCOME=>'课程销售收入',
        self::TAX=>'税额转入'
    ];

    public static $exchangeTypeEN = [
        self::EXCHANGE=>'exchange',
        self::INCOME=>'class_income',
        self::TAX=>'tax_income'
    ];

    public static function checkPoint($user){
        $hasPoint = DB::connection("flarum")->table("users")
            ->where("id","=",$user)->value("points_count");
        return $hasPoint;
    }

    public static function addRecord($username,$userid,$currentPoints,$points,$type=1,$obj_id,$obj_name,$reduce=true){
        self::query()->insert([
            'username'=>$username,
            'user_id'=>$userid,
            'type'=>$type,
            'amount'=>$reduce?$points *-1:$points,
            'obj_id'=>$obj_id,
            'obj_name'=>$obj_name,
        ]);
        DB::connection("flarum")->table("gam_points_logs")->insert(
            [
                'owner_id'=>$userid,
                'type'=>self::$exchangeTypeEN[$type], //积分兑换  需要修改对应插件中的文字内容
                'amount'=>$reduce?$points *-1:$points,
                'current'=>$reduce? $currentPoints-$points: $currentPoints+$points,
                'extra'=>'',
                'created_at'=>date("Y-m-d H:i:s"),
            ]
        );
        return true;
    }


    public static function reducePoints($userid,$points){
        DB::connection("flarum")->table("users")
            ->where("id","=",$userid)->decrement("points_count",$points);
        return true;
    }

    public static function addPoints($userid,$points){
        DB::connection("flarum")->table("users")
            ->where("id","=",$userid)->increment("points_count",$points);
        return true;
    }


}
