<?php

namespace ClarkWinkelmann\GroupInvitation\Controllers;

use ClarkWinkelmann\GroupInvitation\Invitation;
use Flarum\Api\Controller\AbstractDeleteController;
use Illuminate\Support\Arr;
use Psr\Http\Message\ServerRequestInterface;

class DeleteController extends AbstractDeleteController
{
    protected function delete(ServerRequestInterface $request)
    {
        $request->getAttribute('actor')->assertAdmin();

        $id = Arr::get($request->getQueryParams(), 'id');

        /**
         * @var $invitation Invitation
         */
        $invitation = Invitation::query()->findOrFail($id);

        $invitation->delete();
    }
}
