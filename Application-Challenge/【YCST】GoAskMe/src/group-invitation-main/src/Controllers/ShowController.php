<?php

namespace ClarkWinkelmann\GroupInvitation\Controllers;

use ClarkWinkelmann\GroupInvitation\Invitation;
use ClarkWinkelmann\GroupInvitation\Serializers\InvitationSerializer;
use Flarum\Api\Controller\AbstractShowController;
use Illuminate\Support\Arr;
use Psr\Http\Message\ServerRequestInterface;
use Tobscure\JsonApi\Document;

class ShowController extends AbstractShowController
{
    public $serializer = InvitationSerializer::class;

    public $include = [
        'group',
    ];

    protected function data(ServerRequestInterface $request, Document $document)
    {
        // Intentionally no authorization assertion, anyone can check an invitation

        $code = Arr::get($request->getQueryParams(), 'code');

        return Invitation::query()->where('code', $code)->firstOrFail();
    }
}
