<?php

/*
 * This file is part of hehongyuanlove/flarum-points.
 *
 * Copyright (c) 2020 GoAskMe.
 *
 * For the full copyright and license information, please view the LICENSE.md
 * file that was distributed with this source code.
 */

namespace GoAskMe\Points;

use Flarum\Extend;
use GoAskMe\Points\Api\Controller\TestAddController;
use GoAskMe\Points\Api\Controller\RankController;
use GoAskMe\Points\Api\Controller\ConvertController;
use GoAskMe\Points\Api\Controller\ConvertListController;
use GoAskMe\Points\Api\Controller\ConvertCheckController;
use Illuminate\Contracts\Events\Dispatcher;

return [
    (new Extend\Frontend('forum'))
        ->js(__DIR__.'/js/dist/forum.js')
        ->css(__DIR__.'/resources/less/forum.less'),
    (new Extend\Frontend('admin'))
        ->js(__DIR__.'/js/dist/admin.js')
        ->css(__DIR__.'/resources/less/admin.less'),
     new Extend\Locales(__DIR__.'/resources/locale'),
     
     // 用户前端兑换GAM
	(new Extend\Routes('api'))
    	->post('/goaskme_convert_gam_requests', 'points.convertgam',ConvertController::class),
    // 用户自己界面查询
    (new Extend\Routes('api'))
        ->post('/points', 'points.index', TestAddController::class),
    // 任何人 排序
    (new Extend\Routes('forum'))
        ->get('/points/rank', 'points.rank', RankController::class),

    // 管理员 查询所有充值列表
    (new Extend\Routes('api'))
    	->get('/points/list', 'points.list',ConvertListController::class),
    // 管理员 兑换到账
    (new Extend\Routes('api'))
    	->get('/points/check', 'points.check',ConvertCheckController::class),
    function (Dispatcher $events) {
        $events->subscribe(Listener\UserActions::class);
    }
];
