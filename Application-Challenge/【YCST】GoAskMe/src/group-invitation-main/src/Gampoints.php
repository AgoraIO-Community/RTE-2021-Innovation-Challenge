<?php

namespace ClarkWinkelmann\GroupInvitation;

use Carbon\Carbon;
use Flarum\Database\AbstractModel;
use Flarum\Group\Group;

/**
 * @property int $id
 * @property string $code
 * @property int $usage_count
 * @property int $max_usage
 * @property string $inviter
 * @property Carbon $created_at
 * @property Carbon $updated_at
 * @property Group $group
 */
class Gampoints extends AbstractModel
{
    protected $table = 'gam_points_logs';


}
