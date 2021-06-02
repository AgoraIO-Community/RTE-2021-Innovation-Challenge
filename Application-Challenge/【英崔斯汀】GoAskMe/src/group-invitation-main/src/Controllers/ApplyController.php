<?php

namespace ClarkWinkelmann\GroupInvitation\Controllers;

use ClarkWinkelmann\GroupInvitation\Gampoints;
use ClarkWinkelmann\GroupInvitation\Invitation;
use Flarum\Foundation\ValidationException;
use Flarum\Locale\Translator;
use Flarum\User\User;
use Illuminate\Support\Arr;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Str;
use Laminas\Diactoros\Response\EmptyResponse;
use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;
use Psr\Http\Server\RequestHandlerInterface;

class ApplyController implements RequestHandlerInterface
{
    public function handle(ServerRequestInterface $request): ResponseInterface
    {
        $code = Arr::get($request->getQueryParams(), 'code');

        /**
         * @var $invitation Invitation
         */
        $invitation = Invitation::query()->where('code', $code)->first();

        if (!$invitation) {
            throw new ValidationException([
                'code' => app(Translator::class)->trans('clarkwinkelmann-group-invitation.api.error.not-found'),
            ]);
        }

        if (!$invitation->hasUsagesLeft()) {
            throw new ValidationException([
                'code' => app(Translator::class)->trans('clarkwinkelmann-group-invitation.api.error.no-usages-left'),
            ]);
        }

        /**
         * @var $actor User
         */
        $actor = $request->getAttribute('actor');
        $actor->assertCan('use', $invitation);

        if (!$actor->groups->contains('id', $invitation->group->id)) {
            $actor->groups()->save($invitation->group);

            $invitation->usage_count++;
            $invitation->save();
        }
        $inviter = $invitation->inviter_id;
        $inviter = User::query()->where("id","=",$inviter)->first();
        $user = User::query()->where("id","=",$actor->id)->first();
        //被邀请人增加20积分
        User::query()->where("id","=",$actor->id)->update([
            'points_count' => $user->points_count + 20,
            'points_total' => $user->points_total + 20,
        ]);
        //邀请人增加10积分
        User::query()->where("id","=",$inviter->id)->update([
            'points_count' => $inviter->points_count + 10,
            'points_total' => $inviter->points_total + 10,
        ]);
        $pointsLog = new Gampoints();
        $pointsLog->owner_id = $actor->id;
        $pointsLog->type = 'be-invited';
        $pointsLog->amount =20;
        $pointsLog->current = $user->points_count + 20;
        $pointsLog->created_at = date("Y-m-d H:i:s");
        $pointsLog->save();


        $pointsLog = new Gampoints();
        $pointsLog->owner_id = $inviter->id;
        $pointsLog->type = 'invite';
        $pointsLog->amount =10;
        $pointsLog->current = $inviter->points_count + 10;
        $pointsLog->created_at = date("Y-m-d H:i:s");
        $pointsLog->save();
        return new EmptyResponse();
    }
}
