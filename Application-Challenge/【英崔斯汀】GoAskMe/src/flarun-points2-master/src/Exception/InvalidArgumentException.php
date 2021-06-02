<?php
/*
 * This file is part of Flarum.
 *
 * For detailed copyright and license information, please view the
 * LICENSE file that was distributed with this source code.
 */

namespace GoAskMe\Points\Exception;

use Exception;
use Flarum\Foundation\KnownError;

class InvalidArgumentException extends Exception implements KnownError
{
    public function getType(): string
    {
        return 'invalid_Argument';
    }
}