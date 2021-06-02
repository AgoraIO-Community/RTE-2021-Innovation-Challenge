<?php

namespace App\Models;

use Encore\Admin\Traits\DefaultDatetimeFormat;
use Illuminate\Contracts\Auth\MustVerifyEmail;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\SoftDeletes;
use Illuminate\Foundation\Auth\User as Authenticatable;
use Illuminate\Notifications\Notifiable;
use Illuminate\Support\Facades\Storage;
use Illuminate\Support\Str;

class Classes extends Authenticatable
{
    use HasFactory, Notifiable, SoftDeletes, DefaultDatetimeFormat;

    const TYPE_LIMIT = [
        1 => 1,
        2 => 16,
        3 => 999999,
        4 => 16
    ];
    const TYPE_NAME = [
        1 => '一对一',
        2 => '小班课',
        3 => '大班课',
        4 => '超级小班课',
    ];

    public function getImgAttribute()
    {
        // 如果 image 字段本身就已经是完整的 url 就直接返回
        if (Str::startsWith($this->attributes['img'], ['http://', 'https://'])) {
            return $this->attributes['img'];
        }
        return Storage::disk('public')->url($this->attributes['img']);
    }

    /**
     * Notes: 关键词搜索
     * User: niclalalla
     * DateTime: 2020/10/21 16:33
     * @param $keywords
     * @return \Illuminate\Database\Eloquent\Builder[]|\Illuminate\Database\Eloquent\Collection
     */
    public static function search($keywords, $cate1, $cate2)
    {
        $query = self::query()->select(['id', 'name', 'title', 'img', 'description', 'start_time', 'end_time'])
            ->where("status", "=", 2)
            ->where("name", "like", "%$keywords%");
        if ($cate1) $query = $query->where("cate1", "=", $cate1);
        if ($cate2) $query = $query->where("cate2", "=", $cate2);
        return $query->orderBy("start_time")->get();
    }


    public static function createClass($data)
    {
        return self::query()->insertGetId($data);
    }


    public static function getPoints($classid)
    {
        return self::query()->where("id", '=', $classid)->value('price');
    }


}
