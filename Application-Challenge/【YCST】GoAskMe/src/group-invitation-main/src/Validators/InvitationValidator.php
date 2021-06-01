<?php

namespace ClarkWinkelmann\GroupInvitation\Validators;

use Flarum\Foundation\AbstractValidator;

class InvitationValidator extends AbstractValidator
{
    protected $rules = [
        'groupId' => 'required|exists:groups,id',
        'maxUsage' => 'nullable|integer|min:1',
    ];
}
