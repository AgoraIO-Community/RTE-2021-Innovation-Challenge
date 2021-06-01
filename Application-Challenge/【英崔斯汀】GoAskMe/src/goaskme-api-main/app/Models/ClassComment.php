<?php

namespace App\Models;

use Encore\Admin\Traits\DefaultDatetimeFormat;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\SoftDeletes;
use Illuminate\Foundation\Auth\User as Authenticatable;
use Illuminate\Notifications\Notifiable;
use Illuminate\Support\Facades\Storage;
use Illuminate\Support\Str;

class ClassComment extends Authenticatable
{
    use HasFactory, Notifiable,SoftDeletes,DefaultDatetimeFormat;
    protected $table = 'class_comment';


    public function getHeadimgAttribute()
    {
        // 如果 image 字段本身就已经是完整的 url 就直接返回
        if (Str::startsWith($this->attributes['headimg'], ['http://', 'https://'])) {
            return $this->attributes['headimg'];
        }
        return Storage::disk('public')->url($this->attributes['headimg']);
    }

    public static function comments($classid){
        return self::query()->where("class_id","=",$classid)
            ->select(['id','pid','username','headimg','like','content','created_at','updated_at'])->get();
    }


    public static function createComments($data)
    {
        return self::query()->insertGetId($data);
    }


}
