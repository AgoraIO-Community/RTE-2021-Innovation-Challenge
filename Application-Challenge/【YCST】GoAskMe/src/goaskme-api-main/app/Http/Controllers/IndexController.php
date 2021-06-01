<?php

namespace App\Http\Controllers;

use App\Mail\TestEmail;
use App\Models\ClassCate;
use App\Models\Classes;
use App\Result;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Mail;

class IndexController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        //
        $cate = ClassCate::indexCate();
        return response()->json(Result::success($cate));
    }

    /**
     * Notes: 首页搜索
     * User: niclalalla
     * DateTime: 2020/10/21 16:33
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function search(Request $request){
        $cate1 = $request->input("cate1");
        $cate2 = $request->input("cate2");
        $keywords = $request->input("keyword");
        $lists = Classes::search($keywords,$cate1,$cate2);
        return response()->json(Result::success($lists));
    }



    public function email(Request $request){
        $code = $request->input("code");
        $group = $request->input("group");
        $username = $request->input("username");
        $email = $request->input("email");
        $data = ['code'=>$code,'group'=>$group,'username'=>$username,'email'=>$email];
        Mail::to($email)->send(new TestEmail($data));
        return response()->json(Result::success([]));
    }
}
