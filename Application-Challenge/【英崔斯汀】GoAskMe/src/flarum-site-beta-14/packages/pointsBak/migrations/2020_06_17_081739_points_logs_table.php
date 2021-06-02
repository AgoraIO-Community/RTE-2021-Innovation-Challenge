<?php

/**
 * points log table
 */

use Flarum\Database\Migration;
use Illuminate\Database\Schema\Blueprint;

return Migration::createTable('gam_points_logs', function (Blueprint $table) {
    $table->increments('id');
    $table->unsignedInteger('owner_id'); // owner user id
    $table->unsignedInteger('discussion_id')->nullable(); // related discussion id
    $table->unsignedInteger('post_id')->nullable(); // related post id
    $table->string('type', 50); // action type
    $table->integer('amount'); // amount points
    $table->integer('current'); // current points
    $table->text('extra')->nullable(); // extra data
    $table->timestamp('created_at'); // timestamp
});
