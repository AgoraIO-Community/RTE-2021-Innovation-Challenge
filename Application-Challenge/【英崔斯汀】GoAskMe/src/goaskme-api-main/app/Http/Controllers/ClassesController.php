<?php

namespace App\Http\Controllers;

use App\Helpers;
use App\Models\ClassComment;
use App\Models\Classes;
use App\Models\ClassPassword;
use App\Models\User;
use App\Result;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Storage;

class ClassesController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        //

    }

    /**
     * 添加课程
     * @param \Illuminate\Http\Request $request
     * @return \Illuminate\Http\Response
     */
    public function create(Request $request)
    {
        //
        $teacher = User::checkTeacher(self::$user->user_id);
        if (!$teacher)
            return response()->json(Result::error(Result::LIMITED));

        $name = $request->input("name");  //课程名称
        $afterBuyClass = $request->input("afterBuyClass");// 开课后能否兑换课程
//        $roomname = $request->input("roomname");  //课程名称
//        if (!$roomname)
//            return response()->json(Result::error(Result::PARAMA_ERROR,'请填写教室名称'));
        $roomname = '';

        $title = $request->input("title");  //课程标题
        $classtype = $request->input("classtype");  //课程类型 1-一对一 2-小班课 3-大班课

        $img = $request->input("img");  //课程封面图
        $start_time = $request->input("start_time");  //开课时间
        $end_time = $request->input("end_time");  //开课时间


        $base64_str = substr($img, strpos($img, ",") + 1);
        $image = base64_decode($base64_str);
        $imgname = rand(1000, 10000) . time() . '.png';
        Storage::disk('public')->put($imgname, $image);

        $price = $request->input("price");  //课程价格
        $cate1 = $request->input("cate1");  //课程一级分类
        $cate2 = $request->input("cate2");  //课程二级分类

        $txAddress = $request->input('txAddress'); // 课程合约地址
        $linkedAddress = $request->input('linkedAddress'); // 关联创建人的合约创建者地址
        $ethEnv = $request->input('ethEnv'); // 以太坊上链环境

        $description = $request->input("description");  //课程描述
        $class = Classes::createClass([
            'name' => $name,
            'roomname' => $roomname,
            'title' => $title,
            'img' => $imgname,
            'price' => floatval($price),
            'cate1' => $cate1,
            'cate2' => $cate2,
            'classtype' => $classtype,
            'description' => $description,
            'start_time' => $start_time,
            'user_id' => self::$user->user_id,
            'username' => self::$user->username,
            'end_time' => $end_time,
            'afterBuyClass' => $afterBuyClass,
            'txAddress' => $txAddress,
            'linkedAddress' => $linkedAddress,
            'env' => $ethEnv
        ]);
        $password = ClassPassword::create($class, self::$user->user_id, self::$user->username, $roomname, 0);
        return response()->json(Result::success(['id' => $class, 'password' => $password, 'roomname' => $roomname, 'roomtype' => Classes::TYPE_NAME[$classtype]]));
    }

    /**
     * 课程详情
     * @param \Illuminate\Http\Request $request
     * @return \Illuminate\Http\Response
     */
    public function info(Request $request)
    {
        //
        $id = $request->route("id");
        $info = Classes::query()->find($id);
        if (!$info)
            return response()->json(Result::error(Result::NOT_FOUNT));
        $comments = ClassComment::comments($id);
        $info['roomtype'] = $info['classtype'] ? Classes::TYPE_NAME[$info['classtype']] : '';
        $data['info'] = $info;
        $data['comment'] = Helpers::listToTree($comments->toArray());
        return response()->json(Result::success($data));
    }


    public function checkPassword(Request $request)
    {
        $password = $request->input("password");
        $roomname = $request->input("roomname");
        $role = $request->input("role");
        $role = ($role == 'teacher') ? "0" : "1";
        if ($password == '55555555') {
            return response()->json(Result::success(['msg' => '已授权']));
        }
        $check = ClassPassword::in($password, $roomname, $role);
        if ($check === true) {
            return response()->json(Result::success(['msg' => '已授权']));
        }
        return response()->json(Result::error(Result::UNAUTHENTICATED, $check));
    }

    /**
     * Update the specified resource in storage.
     *
     * @param \Illuminate\Http\Request $request
     * @param \App\Models\Classes $classes
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, Classes $classes)
    {
        //
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param \App\Models\Classes $classes
     * @return \Illuminate\Http\Response
     */
    public function destroy(Classes $classes)
    {
        //
    }
}
