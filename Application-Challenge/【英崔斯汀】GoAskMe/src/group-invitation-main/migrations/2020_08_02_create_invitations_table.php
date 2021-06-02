<?php

use Flarum\Database\Migration;
use Illuminate\Database\Schema\Blueprint;

return Migration::createTable(
    'clarkwinkelmann_group_invitations',
    function (Blueprint $table) {
        $table->increments('id');
        $table->unsignedInteger('group_id');
        $table->string('code')->unique();
        $table->string('inviter');
        $table->unsignedInteger('inviter_id');
        $table->unsignedInteger('usage_count')->default(0);
        $table->unsignedInteger('max_usage')->nullable();
        $table->timestamps();

        $table->foreign('group_id')->references('id')->on('groups')->onDelete('cascade');
    }
);
