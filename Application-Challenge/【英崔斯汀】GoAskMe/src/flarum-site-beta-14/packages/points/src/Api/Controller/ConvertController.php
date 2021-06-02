<?php

namespace GoAskMe\Points\Api\Controller;

use Laminas\Diactoros\Response\JsonResponse;
use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;
use Psr\Http\Server\RequestHandlerInterface;
use GoAskMe\Points\PointsRepository;

use GoAskMe\Points\Exception\InvalidArgumentException;

use Illuminate\Support\Arr;


use GoAskMe\Points\Command\ResponseUser;

class ConvertController implements RequestHandlerInterface {

  public $type = 'goaskme_convert_gam_requests';
  public $id;
  protected $points;

  public function __construct( PointsRepository $points)
  {

      $this->points = $points;
  }

  public function handle(ServerRequestInterface $request): ResponseInterface
  {

    $actor = $request->getAttribute('actor');
    $ConverNumber =(int) Arr::get($request->getParsedBody(), 'data.attributes.pointsConverNumber', 0);
    $Count = (string) Arr::get($request->getParsedBody(), 'data.attributes.pointsCount', ''); // 钱包地址
    if($ConverNumber<=0 ||(int)$actor->points_count < $ConverNumber*5 || $Count === '' ){
    	 throw new InvalidArgumentException;
    	 //throw new PermissionDeniedException;
    }
    $this->points->changePoints(-$ConverNumber*5, $actor, 'Convering',null,null,$Count);
 
    
    //return new JsonResponse([$this->settings,$this->points]);
    /**
     * type: "goaskme_convert_gam_requests"
     * attributes: {pointsConverNumber: "1"}
     */
    /**
     * type: "users"
     * id: "1"
     * attributes: {
     *  username: "YCCLY"
     *  displayName: "YCCLY"
     *  avatarUrl: "http://fl2.himi3d.cn/assets/avatars/a1sXGoXSdw2ZHs3T.png"
     *  joinTime: "2020-08-01T08:51:52+00:00"
     *  discussionCount: 11
     *  commentCount: 27
     *  canEdit: true
     *  canDelete: true
     *  lastSeenAt: "2020-08-11T14:20:02+00:00"
     *  isEmailConfirmed: true
     *  email: "101682318@qq.com"
     *  canSuspend: false
     *  blocksPd: false
     *  cannotBeDirectMessaged: true
     *  pointsCount: 0
     *  pointsRank: 3
     */

     $res = [
      'type'=> 'goaskme_convert_gam_requests',
      'id'=> $actor->id,
      'attributes'=> [
      	'pointsCount'=>$actor->points_count
      	]
    ];
    return new JsonResponse(['data'=>$res]);
  }
}
