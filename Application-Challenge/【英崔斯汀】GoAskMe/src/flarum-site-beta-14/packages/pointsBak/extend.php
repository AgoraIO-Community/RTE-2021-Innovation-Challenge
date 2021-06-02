<?php

namespace GoAskMe\Points;

use Flarum\Extend;
use GoAskMe\Points\Api\Controller\TestAddController;
use Illuminate\Contracts\Events\Dispatcher;

return [
    (new Extend\Routes('forum'))
        ->get('/points', 'points.index', TestAddController::class),

    function (Dispatcher $events) {
        $events->subscribe(Listener\UserActions::class);
    }
];
