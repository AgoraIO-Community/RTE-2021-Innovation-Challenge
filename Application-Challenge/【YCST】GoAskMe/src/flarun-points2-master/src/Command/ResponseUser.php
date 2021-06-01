<?php

/*
 * This file is part of Flarum.
 *
 * For detailed copyright and license information, please view the
 * LICENSE file that was distributed with this source code.
 */

namespace GoAskMe\Points\Command;

use Flarum\User\User;

class ResponseUser
{
    /**
     * The user performing the action.
     *
     * @var User
     */
    public $actor;

    /**
     * @param User $actor The user performing the action.
     */
    public function __construct(User $actor)
    {
        $this->userId = $actor->id;
        $this->actor = $actor;
    }
}
