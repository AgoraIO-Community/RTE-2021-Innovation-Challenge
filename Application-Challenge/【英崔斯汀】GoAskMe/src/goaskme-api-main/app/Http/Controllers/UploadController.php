<?php

namespace App\Http\Controllers;

use App\Result;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Storage;

class UploadController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function __invoke(Request $request)
    {
        $file = $request->file('file');
        $path = $file->storeAs(date("Ymd"),md5($file->getClientOriginalName().md5(rand(0,100))).'.'.$file->extension(),'public');
        return response()->json(Result::success([
            'path'=>Storage::disk('public')->url($path),
            'local_path'=>$path
        ]));
    }

}
