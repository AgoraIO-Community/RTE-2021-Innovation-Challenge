<?php

namespace ClarkWinkelmann\GroupInvitation\Controllers;

use ClarkWinkelmann\GroupInvitation\Invitation;
use ClarkWinkelmann\GroupInvitation\Serializers\InvitationSerializer;
use ClarkWinkelmann\GroupInvitation\Validators\InvitationValidator;
use Flarum\Api\Controller\AbstractCreateController;
use Flarum\Group\Group;
use Flarum\Settings\SettingsRepositoryInterface;
use Flarum\User\User;
use Illuminate\Support\Arr;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Str;
use Psr\Http\Message\ServerRequestInterface;
use Tobscure\JsonApi\Document;

class StoreController extends AbstractCreateController
{
    public $serializer = InvitationSerializer::class;

    public $include = [
        'group',
    ];

    protected $validator;

    public function __construct(InvitationValidator $validator)
    {
        $this->validator = $validator;
    }

    protected function data(ServerRequestInterface $request, Document $document)
    {
        $request->getAttribute('actor')->assertAdmin();

        $data = $request->getParsedBody();

        $this->validator->assertValid($data);

        $invitation = new Invitation();
        $invitation->code = Str::random();
        $invitation->usage_count = 0;
        $invitation->max_usage = Arr::get($data, 'maxUsage') ?: null;
        $invitation->inviter = Arr::get($data, 'inviter') ?: null;
        $user = new User();
        $inviter =$user->query()->where("username","=",$invitation->inviter)->first();
        $invitation->inviter_id = $inviter->id;
        $invitation->group()->associate(Arr::get($data, 'groupId'));
        $invitation->save();
        $group = Group::query()->where("id","=",Arr::get($data, 'groupId'))->value("name_singular");
        $settings = app(SettingsRepositoryInterface::class);
        $emailGate = "https://api.sendgrid.com/v3/mail/send";
//$data = '{"personalizations":[{"to":[{"email":"niclalalla@163.com","name":"John Doe"}],"subject":"Hello, World!"}],"content": [{"type": "text/plain", "value": "Heya!"}],"from":{"email":"info@in-ycst.com","name":"Sam Smith"},"reply_to":{"email":"info@in-ycst.com","name":"Sam Smith"}}';
        $data = [
            'personalizations' => [
                [
                    'to'=>[
                        [
                            'email' => $inviter->email,
                            'name' => $inviter->username
                        ]
                    ],
                    'subject'=>'Hello, '.$inviter->username.'!'
                ]
            ],
            'content' => [
                [
                    'type' => 'text/plain',
                    'value' => '请查收‘'.$group.'社区小组’入组专属邀请码 '.$invitation->code.'，请妥善保存。'
                ]
            ],
            'from' => [
                'email' => $settings->get("mail_from"),
                'name' => 'GAM',
            ],
            'reply_to' => [
                'email' => $settings->get("mail_from"),
                'name' => 'GAM',
            ],
        ];

        $data = json_encode($data);
        $headerArray[] ="Authorization:";
        $headerArray[] ="Content-Type:application/json";
        $curl = curl_init();
        curl_setopt($curl, CURLOPT_URL, $emailGate);
        curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, FALSE);
        curl_setopt($curl, CURLOPT_SSL_VERIFYHOST,FALSE);
        curl_setopt($curl, CURLOPT_POST, 1);
        curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
        curl_setopt($curl,CURLOPT_HTTPHEADER,$headerArray);
        curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);
        $output = curl_exec($curl);
        curl_close($curl);
        var_dump(json_decode($output,true));

        return $invitation;
    }
}
