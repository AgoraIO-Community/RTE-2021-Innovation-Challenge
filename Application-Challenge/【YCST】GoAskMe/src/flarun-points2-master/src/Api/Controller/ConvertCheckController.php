<?php

namespace GoAskMe\Points\Api\Controller;

use Flarum\Settings\SettingsRepositoryInterface;
use GoAskMe\Points\PointsRepository;
use Laminas\Diactoros\Response\JsonResponse;
use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;
use Psr\Http\Server\RequestHandlerInterface;

use GoAskMe\Points\GamPointsLog;

class ConvertCheckController implements RequestHandlerInterface
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
         	$id = (int)(isset($request->getQueryParams()['id'])?$request->getQueryParams()['id']:null);
         	if(is_null($id)){
         		return new JsonResponse(['msg'=>'Conver Check id is Null ','status'=>false]);
         	}
         	
         	$type  = (string)(isset($request->getQueryParams()['type'])?$request->getQueryParams()['type']:null);
         	if(is_null($type) || !in_array($type,['converDown','converReject']) ){
         		return new JsonResponse(['msg'=>'Conver Check type out of expect |'. $type,'status'=>false]);
         	}
         	
         
     
        		
        	$updat = GamPointsLog::where('id',$id )->update(['type' => $type ]);
        		
        		
         	// 若是退回 converRejectReturn
         	if($updat && $type === "converReject" ){
         		
         		$num =  GamPointsLog::with('owner')->where('id',$id )->get(['type','amount','owner_id']);
       
         		$this->points->changePoints(abs($num[0]->amount), $num[0]->owner, 'converRejectReturn',null,null,null);
         	}
        		
       
         	
         	$res = [
         		'status'=>true,
         		'id'=>$id,
         		'type'=>$type,
         		'num'=>$updat->amount
         		];
         	
         	return new JsonResponse($res);
         	
         }
         
          return new JsonResponse(['msg'=>'Conver Check 你不是管理员','status'=>false]);
         
    }
}
