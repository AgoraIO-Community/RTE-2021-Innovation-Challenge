<?php

namespace ClarkWinkelmann\GroupInvitation\Access;

use ClarkWinkelmann\GroupInvitation\Invitation;
use Flarum\User\AbstractPolicy;
use Flarum\User\User;

class InvitationPolicy extends AbstractPolicy
{
    protected $model = Invitation::class;

    public function use(User $actor, Invitation $invitation)
    {
        return $actor->hasPermission('clarkwinkelmann-group-invitation.use') && $invitation->hasUsagesLeft();
    }
}
