<?php

namespace App\Http\Controllers;

use App\Models\ClassCate;
use App\Result;
use Encore\Admin\Traits\DefaultDatetimeFormat;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\SoftDeletes;
use Illuminate\Http\Request;
use Illuminate\Notifications\Notifiable;

class ClassCateController extends Controller
{
    use HasFactory, Notifiable,SoftDeletes,DefaultDatetimeFormat;

    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        //
        $list = ClassCate::classTree();
        return response()->json(Result::success($list));
    }

    /**
     * Show the form for creating a new resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function create()
    {
        //
    }

    /**
     * Store a newly created resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return \Illuminate\Http\Response
     */
    public function store(Request $request)
    {
        //
    }

    /**
     * Display the specified resource.
     *
     * @param  \App\Models\ClassCate  $classCate
     * @return \Illuminate\Http\Response
     */
    public function show(ClassCate $classCate)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  \App\Models\ClassCate  $classCate
     * @return \Illuminate\Http\Response
     */
    public function edit(ClassCate $classCate)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \App\Models\ClassCate  $classCate
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, ClassCate $classCate)
    {
        //
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  \App\Models\ClassCate  $classCate
     * @return \Illuminate\Http\Response
     */
    public function destroy(ClassCate $classCate)
    {
        //
    }
}
