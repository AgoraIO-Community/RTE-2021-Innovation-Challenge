<?php

namespace App;


class Result
{
    const OK = 0;

    const SYSTEM_ERROR = 100000;
    const ERROR = 200000;
    const UNAUTHENTICATED = 201001;
    const NOT_FOUNT  = 200404;
    const UNKNOWN  = 200405;
    const LIMITED  = 200406;
    const PARAMA_ERROR = 200403;

    const MESSAGES = [
        self::OK => '成功',
        self::UNAUTHENTICATED => '未授权',
        self::NOT_FOUNT => '对象不存在',
        self::UNKNOWN => '未知错误',
        self::LIMITED => '请先申请成为教师',
        self::PARAMA_ERROR => '字段错误'
    ];

    public static function ok($data = null)
    {
        $result = [
            'status' => self::OK,
            'message' => self::MESSAGES[self::OK],
        ];
        if ($data) {
            $result['data'] = $data;
        }
        return $result;
    }

    public static function success($data = null)
    {
        return [
            'status' => self::OK,
            'message' => self::MESSAGES[self::OK],
            'data'=>$data
        ];
    }

    public static function fail($message = null)
    {
        return [
            'status' => Result::ERROR,
            'message' => $message ?? self::MESSAGES[Result::ERROR]
        ];
    }

    public static function error($status, $message = null)
    {
        return [
            'status' => $status,
            'message' => $message ? $message : (self::MESSAGES[$status] ?? '错误'),
        ];
    }

    public static function crash()
    {
        return Result::error(Result::SYSTEM_ERROR);
    }

}
