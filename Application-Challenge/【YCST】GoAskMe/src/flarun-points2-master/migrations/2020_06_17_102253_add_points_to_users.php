<?
/*
 * This file is part of fof/byobu.
 *
 * Copyright (c) 2019 FriendsOfFlarum.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Schema\Builder;

return [
    'up' => function (Builder $schema) {
        $schema->table('users', function (Blueprint $table) {
            $table->unsignedInteger('points_count')->default(0);    // 当前值
            $table->unsignedInteger('points_rank')->default(1);     // 星级排名 1->10
            $table->char('points_probability')->default('{}'); // 获得积分概率
            $table->integer('points_total')->default(0); // 获得积分概率

        });
    },
    'down' => function (Builder $schema) {
        $schema->table('users', function (Blueprint $table) {
            $table->dropColumn('points_count');
            $table->dropColumn('points_rank');
            $table->dropColumn('points_probability');
            $table->dropColumn('points_total');
        });
    },
];

/**
 * 
 * use Flarum\Database\Migration;
 * return Migration::addColumns('users', [
 *      'points_count' => ['integer'],
 *      'points_rank' => ['integer','nullable'],
 *      'points_probability' => ['text', 'nullable'],
 * ]);
 */