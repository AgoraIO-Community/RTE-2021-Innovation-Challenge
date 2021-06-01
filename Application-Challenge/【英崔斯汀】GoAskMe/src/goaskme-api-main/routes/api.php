<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/
//Route::middleware('auth:api')->get('/user', function (Request $request) {
//    return $request->user();
//});
//登录
Route::post('/user/login', [\App\Http\Controllers\UserController::class, 'login']);
//验证密码
Route::get('/classes/checkpassword', [\App\Http\Controllers\ClassesController::class, 'checkPassword']);
//课程相关路由
Route::get('/index', [\App\Http\Controllers\IndexController::class, 'index']);
Route::get('/email', [\App\Http\Controllers\IndexController::class, 'email']);
Route::get('/classes/cate/{id}', [\App\Http\Controllers\IndexController::class, 'search']);
Route::get('/classes/search', [\App\Http\Controllers\IndexController::class, 'search']);
Route::get('/classes/index', [\App\Http\Controllers\ClassesController::class, 'index']);
Route::get('/classes/{id}', [\App\Http\Controllers\ClassesController::class, 'info']);

//定时结算
Route::get('/plan/points', function () {
    $exitCode = Artisan::call('points:send');
    //
});


Route::group([
    'middleware' => 'api.auth'
], function () {
    Route::post('/classes/add', [\App\Http\Controllers\ClassesController::class, 'create']);
    Route::put('/classes/edit', [\App\Http\Controllers\ClassesController::class, 'update']);
    Route::delete('/classes:uuid', [\App\Http\Controllers\ClassesController::class, 'destroy']);
});

//课程分类
Route::group([

], function () {
    Route::get('/classescate', [\App\Http\Controllers\ClassCateController::class, 'index']);
});

// 图片上传
Route::group([
    'middleware' => 'api.auth'
], function () {
    Route::post('/upload', \App\Http\Controllers\UploadController::class);
});

//添加评论
Route::group([
    'middleware' => 'api.auth'
], function () {
    Route::post('/comments', [\App\Http\Controllers\CommentsController::class, 'create']);
});

//兑换
Route::group([
    'middleware' => 'api.auth'
], function () {
    Route::post('/user/exchange', [\App\Http\Controllers\UserController::class, 'exchange']);
    Route::post('/user/classes', [\App\Http\Controllers\UserController::class, 'classes']);

    // 获取积分
    Route::post('/user/get_point', [\App\Http\Controllers\UserController::class, 'get_point']);
    // 激活/取消激活教室
    Route::post('/user/class_active', [\App\Http\Controllers\UserController::class, 'class_active']);
    // 阅读广告获得积分奖励
    Route::post('/user/view_ad', [\App\Http\Controllers\UserController::class, 'view_ad']);
});

// 广告列表
Route::post('/user/ad_list', [\App\Http\Controllers\UserController::class, 'ad_list']);
