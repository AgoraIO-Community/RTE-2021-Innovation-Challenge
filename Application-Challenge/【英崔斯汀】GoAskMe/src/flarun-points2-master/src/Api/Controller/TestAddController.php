<?php

namespace GoAskMe\Points\Api\Controller;

use Flarum\Settings\SettingsRepositoryInterface;
use GoAskMe\Points\PointsRepository;
use Laminas\Diactoros\Response\JsonResponse;
use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;
use Psr\Http\Server\RequestHandlerInterface;

use GoAskMe\Points\GamPointsLog;

class TestAddController implements RequestHandlerInterface
{

    protected $settings;
    protected $points;

    public function __construct(SettingsRepositoryInterface $settings, PointsRepository $points)
    {
        $this->settings = $settings;
        $this->points = $points;
    }

    public function handle(ServerRequestInterface $request): ResponseInterface
    {
         $actor = $request->getAttribute('actor');
         $log = GamPointsLog::query()
            ->where('owner_id', $actor->id)
            ->orderBy('created_at', 'desc')
            ->limit(20)
            ->get(['owner_id','type','amount','current','created_at']);
         // $this->points->changePoints(-5, $actor, 'test');
         //return new JsonResponse([$this->settings,$this->points]);
         $data = [
         	'type'=>'points',
         	'id'=> $actor->id,
         	'list'=>$log
         	];
         return new JsonResponse(['data'=>$data]);
    }
}
