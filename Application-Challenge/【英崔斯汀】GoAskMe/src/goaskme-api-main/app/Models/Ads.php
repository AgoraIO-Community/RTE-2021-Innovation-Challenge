<?php

namespace App\Models;

use Encore\Admin\Traits\DefaultDatetimeFormat;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;
use Illuminate\Foundation\Auth\User as Authenticatable;
use Illuminate\Notifications\Notifiable;
use Illuminate\Support\Facades\Storage;
use Illuminate\Support\Str;

class Ads extends Authenticatable
{
    use HasFactory, Notifiable, SoftDeletes, DefaultDatetimeFormat;

    public function getImgAttribute()
    {
        // 如果 image 字段本身就已经是完整的 url 就直接返回
        if (Str::startsWith($this->attributes['img'], ['http://', 'https://'])) {
            return $this->attributes['img'];
        }
        return Storage::disk('public')->url($this->attributes['img']);
    }

    public function getVideoAttribute()
    {
        // 如果 image 字段本身就已经是完整的 url 就直接返回
        if (Str::startsWith($this->attributes['video'], ['http://', 'https://'])) {
            return $this->attributes['video'];
        }
        return Storage::disk('public')->url($this->attributes['video']);
    }

}
