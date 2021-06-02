<?php

namespace GoAskMe\Points\Api\Controller;

use Flarum\Settings\SettingsRepositoryInterface;
use GoAskMe\Points\PointsRepository;
use Laminas\Diactoros\Response\JsonResponse;
use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;
use Psr\Http\Server\RequestHandlerInterface;

use GoAskMe\Points\GamPointsLog;

class ConvertListController implements RequestHandlerInterface
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
         // if(null !== $actor && $actor->isAdmin() && 'POST' === $request->getMethod()){
         if(null !== $actor && $actor->isAdmin()){
         $offset = (int)(isset($request->getQueryParams()['offset'])?$request->getQueryParams()['offset']:0);
         $limit  = (int)(isset($request->getQueryParams()['limit'])?$request->getQueryParams()['limit']:0);
        	$num =  GamPointsLog::query()->where('type', 'Convering')->count();
        	
        	$next = ($offset+$limit)<$num;
        	
        	$prev = $offset>0; 
        	
            $logList = GamPointsLog::where('type', 'Convering')
            ->orderBy('created_at', 'desc')
            ->offset($offset)
            ->take($limit)
            ->with(['owner'=>function($query){
            $query->select('id', 'username'); // 需要同时查询关联外键的字段
        	}])
            ->get(['id','owner_id','type','amount','current','created_at','extra']);
            
            $data = [
         	'type'=>'points/list',
         	'id'=> $actor->id,
         	'list'=>$logList,
         	'next'=>$next,
         	'prev'=>$prev,
         	];
         	
         	return new JsonResponse(['data'=>$data]);
         	   // return new JsonResponse($logList);
         }
         return new JsonResponse(['msg'=>" 你不是管理员"]);
        
    }
}
