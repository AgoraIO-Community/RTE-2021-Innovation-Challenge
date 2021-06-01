<?php

namespace ClarkWinkelmann\GroupInvitation\Controllers;

use ClarkWinkelmann\GroupInvitation\Invitation;
use ClarkWinkelmann\GroupInvitation\Serializers\InvitationSerializer;
use Flarum\Api\Controller\AbstractListController;
use Psr\Http\Message\ServerRequestInterface;
use Tobscure\JsonApi\Document;

class IndexController extends AbstractListController
{
    public $serializer = InvitationSerializer::class;

    public $include = [
        'group',
    ];

    protected function data(ServerRequestInterface $request, Document $document)
    {
        $request->getAttribute('actor')->assertAdmin();

        return Invitation::query()->orderBy('created_at')->get("*");
    }
}
