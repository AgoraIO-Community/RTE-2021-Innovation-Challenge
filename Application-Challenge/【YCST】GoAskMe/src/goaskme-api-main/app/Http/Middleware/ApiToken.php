<?php

namespace App\Http\Middleware;

use App\Result;
use App\Models\Marketer;
use Closure;
use Illuminate\Support\Facades\DB;

class ApiToken
{
    /**
     * Handle an incoming request.
     *
     * @param \Illuminate\Http\Request $request
     * @param \Closure $next
     * @return mixed
     */
    public function handle($request, Closure $next)
    {
        $token = $request->header("x-access-token");
        if (!$token) {
            return response()->json(Result::error(Result::UNAUTHENTICATED));
        }
        $user = DB::table('users')->where('token', $token)->first();
        if (!$user) {
            return response()->json(Result::error(Result::UNAUTHENTICATED));
        }
        return $next($request);
    }
}
