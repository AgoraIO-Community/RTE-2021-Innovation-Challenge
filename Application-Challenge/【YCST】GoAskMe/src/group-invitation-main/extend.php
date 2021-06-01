<?php

namespace ClarkWinkelmann\GroupInvitation;

use Flarum\Extend;
use Illuminate\Contracts\Events\Dispatcher;

return [
    (new Extend\Frontend('forum'))
        ->js(__DIR__ . '/js/dist/forum.js')
        ->css(__DIR__ . '/resources/less/forum.less')
        ->route('/get-role/{code:[a-zA-Z0-9_-]+}', 'group-invitation.get'),

    (new Extend\Frontend('admin'))
        ->js(__DIR__ . '/js/dist/admin.js')
        ->css(__DIR__ . '/resources/less/admin.less'),

    new Extend\Locales(__DIR__ . '/resources/locale'),

    (new Extend\Routes('api'))
        ->get('/group-invitations', 'group-invitation.index', Controllers\IndexController::class)
        ->post('/group-invitations', 'group-invitation.store', Controllers\StoreController::class)
        ->get('/group-invitations/{code:[a-zA-Z0-9_-]+}', 'group-invitation.show', Controllers\ShowController::class)
        ->post('/group-invitations/{code:[a-zA-Z0-9_-]+}/apply', 'group-invitation.apply', Controllers\ApplyController::class)
        ->delete('/group-invitations/{id:[0-9]+}', 'group-invitation.delete', Controllers\DeleteController::class),

    function (Dispatcher $events) {
        $events->subscribe(Access\InvitationPolicy::class);
    },
];
