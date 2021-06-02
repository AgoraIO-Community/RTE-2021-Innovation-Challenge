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
 *
 * @property Group $group
 */
class Invitation extends AbstractModel
{
    protected $table = 'clarkwinkelmann_group_invitations';

    public $timestamps = true;

    protected $casts = [
        'usage_count' => 'int',
        'max_usage' => 'int',
        'created_at' => 'datetime',
    ];

    public function group()
    {
        return $this->belongsTo(Group::class);
    }

    public function hasUsagesLeft(): bool
    {
        return !$this->max_usage || $this->usage_count < $this->max_usage;
    }
}
