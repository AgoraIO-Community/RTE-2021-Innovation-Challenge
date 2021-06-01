<?php

namespace App\Http\Controllers;

use App\Helpers;
use App\Models\Ads;
use App\Models\ClassCate;
use App\Models\Classes;
use App\Models\ClassPassword;
use App\Models\PointsRecord;
use App\Models\User;
use App\Result;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Storage;
use Illuminate\Support\Str;

class UserController extends Controller
{

    /**
     * Notes: 积分兑换
     * User: niclalalla
     * DateTime: 2020/10/22 20:51
     * @param Request $request
     * @return array
     */
    public function exchange(Request $request)
    {
        $type = $request->input("type", 1);// 积分用途
        $obj_id = $request->input("obj_id");// 兑换对象
        $obj_name = $request->input("obj_name");// 兑换对象名称
        $username = self::$user->username;
        $user_id = self::$user->user_id;
        DB::beginTransaction();
        try {
            $points = Classes::getPoints($obj_id);
            $hasPoints = PointsRecord::checkPoint($user_id);
            if (ClassPassword::classBuy($obj_id, $user_id))
                return Result::error(Result::LIMITED, '您已购买过该课程');
            if ($hasPoints < $points)
                return Result::error(Result::LIMITED, '积分不足');
            $class = Classes::query()->where("id", "=", $obj_id)->first();
            if (!$class)
                return Result::error(Result::LIMITED, '课程不存在');

            // 课程时间开始后能否兑换课程
            $afterBuyClass = $class->afterBuyClass;
            if ($afterBuyClass == 1) {
                if (strtotime($class->end_time) < time())
                    return Result::error(Result::LIMITED, '课程已结束');
            } else {
                if (strtotime($class->start_time) < time())
                    return Result::error(Result::LIMITED, '课程已开始');
            }

            if (Classes::TYPE_LIMIT[$class->classtype] <= $class->sale)
                return Result::error(Result::LIMITED, '课程已兑换完毕');
            PointsRecord::addRecord($username, $user_id, $hasPoints, $points, $type, $obj_id, $obj_name);
            PointsRecord::reducePoints($user_id, $points);
            $password = ClassPassword::create($obj_id, $user_id, $username, $class->roomname);
            DB::commit();
            return response()->json(Result::success(['password' => $password, 'roomname' => $class->roomname, 'roomtype' => Classes::TYPE_NAME[$class->classtype]]));
        } catch (\Exception $exception) {
            DB::rollBack();
            return response()->json(Result::error(Result::UNKNOWN, $exception->getMessage()));
        }
    }

    public function login(Request $request)
    {
        $username = $request->input("username");
        $password = $request->input("password");
        $user = User::checkPassword($username, $password);
        if ($user === false)
            return response()->json(Result::error(Result::UNAUTHENTICATED, '用户信息错误'));
        return response()->json(Result::success(['user' => $user]));
    }



    /**
     *
     * 从这开始由panco开发
     * QQ:1129443982
     *
     */
    // 获取用户积分/是否教师用户组
    public function get_point(Request $request)
    {
        $user = $this->getUser($request);
        $isTeacher = 0;
        if (!$user) {
            $point = 0;
        } else {
            $user_luntan = DB::connection('flarum')->table('users')->where('id', $user['user_id'])->first();
            $point = $user_luntan->points_count;
            $user_group = DB::connection('flarum')->table('group_user')->where('user_id', $user['user_id'])->where('group_id', 11)->first();
            if ($user_group) {
                $isTeacher = 1;
            }
        }
        return response()->json(Result::success(['point' => $point, 'isTeacher' => $isTeacher]));
    }

    // 用户课程列表：分类型
    // class表需要添加字段 active_status  tinyint	1 激活状态，1已激活，0未激活
    public function classes(Request $request)
    {
        $type = $request->post('type');
        $user = $this->getUser($request);
        $classes = [];
        switch ($type) {
            case 1: // 购买课程记录
                $list = DB::table('point_record')->where('user_id', $user['user_id'])->where('type', 1)->orderBy('created_at', 'desc')->select('obj_id', 'amount', 'created_at')->get()->toArray();
                foreach ($list as &$v) {
                    $class = Classes::find($v->obj_id);
                    if ($class) {
                        $classes[] = [
                            'obj_id' => $v->obj_id,
                            'amount' => $v->amount,
                            'created_at' => $v->created_at,
                            'class' => $class,
                        ];
                    }
                }
                break;
            case 2: // 正在进行课程
                $list = DB::table('point_record')->where('user_id', $user['user_id'])->where('type', 1)->orderBy('created_at', 'desc')->select('obj_id', 'amount', 'created_at')->get()->toArray();
                foreach ($list as &$v) {
                    $now = date("Y-m-d H:i:s", time());
                    $class = Classes::where('start_time', '<=', $now)->where('end_time', '>=', $now)->find($v->obj_id);
                    if ($class) {
                        $classes[] = [
                            'obj_id' => $v->obj_id,
                            'amount' => $v->amount,
                            'created_at' => $v->created_at,
                            'class' => $class,
                        ];
                    }
                }
                break;
            case 3: // 已完成课程
                $list = DB::table('point_record')->where('user_id', $user['user_id'])->where('type', 1)->orderBy('created_at', 'desc')->select('obj_id', 'amount', 'created_at')->get()->toArray();
                foreach ($list as &$v) {
                    $now = date("Y-m-d H:i:s", time());
                    $class = Classes::where('end_time', '<=', $now)->find($v->obj_id);
                    if ($class) {
                        $classes[] = [
                            'obj_id' => $v->obj_id,
                            'amount' => $v->amount,
                            'created_at' => $v->created_at,
                            'class' => $class,
                        ];
                    }
                }
                break;
            case 4: // 已发布课程
                $list = Classes::where('user_id', $user['user_id'])->get();
                foreach ($list as &$v) {
                    $can_active = 0;
                    $now = time();
                    $start_time = strtotime($v['start_time']);
                    $end_time = strtotime($v['end_time']);
                    if ($now >= $start_time && $now <= $end_time) {
                        $can_active = 1;
                    }
                    $classes[] = [
                        'obj_id' => $v->id,
                        'amount' => $v->price,
                        'created_at' => $v->create_time,
                        'class' => $v,
                        'can_active' => $can_active // 教师能否激活标识
                    ];
                }
                break;
        }
        return response()->json(Result::success([
            'id' => $user['user_id'],
            'classes' => $classes
        ]));
    }

    // 激活/取消激活教室
    public function class_active(Request $request)
    {
        $user = $this->getUser($request);
        $class_id = $request->post('class_id', 0);
        $active_status = $request->post('status', 0);
        if ($active_status == 1) {
            $roomname = Str::random(5);
        } else {
            $roomname = '';
        }
        $update = DB::table('classes')->where('id', $class_id)->where('user_id', $user['user_id'])->where('deleted_at', '=', null)->update(['active_status' => $active_status, 'roomname' => $roomname]);
        if ($update) {
            return response()->json(Result::success([
                'roomname' => $roomname,
                'active_status' => $active_status,
            ]));
        } else {
            return response()->json(Result::error(Result::ERROR, '发生错误，操作失败！'));
        }
    }

    // 获取广告列表
    public function ad_list()
    {
        return response()->json(Result::success(['list' => Ads::where('status', 1)->orderBy('sort', 'asc')->get()->toArray()]));
    }

    // 用户观看广告奖励
    public function view_ad(Request $request)
    {
        $user = $this->getUser($request);
        $ad_id = $request->post('ad_id', 0);
        $date = date("Y-m-d");
        $times = DB::table('ads_record')->where('user_id', $user['user_id'])->where('date', $date)->count();
        if ($times < 3) {
            // 可以视频奖励
            $rand = mt_rand(1, 10);
            if ($rand == 1 || $rand == 3 || $rand == 5 || $rand == 7 || $rand == 9) {
                DB::table('ads_record')->insert(['ad_id' => $ad_id, 'user_id' => $user['user_id'], 'date' => $date]);
                $user_luntan = DB::connection('flarum')->table('users')->where('id', $user['user_id'])->first();
                DB::connection('flarum')->table('users')->where('id', $user['user_id'])->update(['points_count' => $user_luntan->points_count + 5]);
                DB::connection('flarum')->table('gam_points_logs')->insert([
                    'owner_id' => $user['user_id'],
                    // flarum/packages/points/resources/locale/en.yml 添加 ad_income:广告收入
                    'type' => 'ad_income',
                    'amount' => 5,
                    'current' => $user_luntan->points_count + 5,
                    'created_at' => date('Y-m-d H:i:s')
                ]);
                return response()->json(Result::success([]));
            }
        }
        return response()->json(Result::fail([]));
    }

}
