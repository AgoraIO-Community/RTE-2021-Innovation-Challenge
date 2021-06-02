<?php

namespace App\Http\Controllers;

use App\Models\User;
use Illuminate\Foundation\Auth\Access\AuthorizesRequests;
use Illuminate\Foundation\Bus\DispatchesJobs;
use Illuminate\Foundation\Validation\ValidatesRequests;
use Illuminate\Http\Request;
use Illuminate\Routing\Controller as BaseController;

class Controller extends BaseController
{
    use AuthorizesRequests, DispatchesJobs, ValidatesRequests;
    protected static $user;
    public function __construct(Request $request)
    {
        self::$user = $this->getUser($request);
    }

    public function getUser($request){
        $token = $request->header("x-access-token");
        if ($token)
            return User::getUserByToken($token);
        return  null;
    }

}
