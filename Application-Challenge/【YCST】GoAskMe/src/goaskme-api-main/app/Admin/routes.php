<?php

use Illuminate\Routing\Router;

Admin::routes();

Route::group([
    'prefix'        => config('admin.route.prefix'),
    'namespace'     => config('admin.route.namespace'),
    'middleware'    => config('admin.route.middleware'),
    'as'            => config('admin.route.prefix') . '.',
], function (Router $router) {

    $router->get('/', 'HomeController@index')->name('home');
    $router->resource('classes', ClassesController::class);
    $router->resource('points-records', PointsRecordController::class);
    $router->resource('class-cates', ClassCateController::class);
    $router->resource('ads', AdsController::class);
});
