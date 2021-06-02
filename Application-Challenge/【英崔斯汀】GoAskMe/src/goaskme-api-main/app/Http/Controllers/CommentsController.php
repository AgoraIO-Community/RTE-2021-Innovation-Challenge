<?php

namespace App\Http\Controllers;

use App\Helpers;
use App\Models\ClassComment;
use App\Models\Classes;
use App\Models\User;
use App\Result;
use Illuminate\Http\Request;

class CommentsController extends Controller
{


    /**
     * 添加评论
     * @param  \Illuminate\Http\Request  $request
     * @return \Illuminate\Http\Response
     */
    public function create(Request $request)
    {
        //
        $username = self::$user->username;
        $headimg = self::$user->headimg;
        $type = $request->input("type");  //评论类型
        $class_id = $request->input("class_id");  //课程标题
        $content = $request->input("content");  //课程封面图
        $pid = $request->input("pid");  //上级评论ID
        if ($type == 1){
            try {
                $id =
                    ClassComment::createComments([
                        'class_id'=>$class_id,
                        'content'=>$content,
                        'pid'=>$pid,
                        'username'=>$username,
                        'headimg'=>$headimg,
                    ]);
                return response()->json(Result::success(['id'=>$id]));
            }catch (\Exception $exception){
                return response()->json(Result::error(Result::UNKNOWN,$exception->getMessage()));
            }
        }else{
            return response()->json(Result::error(Result::NOT_FOUNT));
        }
    }


}
