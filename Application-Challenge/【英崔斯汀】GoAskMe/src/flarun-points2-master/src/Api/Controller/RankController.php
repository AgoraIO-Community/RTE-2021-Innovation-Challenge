<?php

namespace GoAskMe\Points\Api\Controller;

use Laminas\Diactoros\Response\JsonResponse;
use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;
use Psr\Http\Server\RequestHandlerInterface;
use Flarum\User\UserRepository;


class RankController implements RequestHandlerInterface
{
	protected $users;
	
	/*
	- 1%-2% 	=> 5星 =10
	- 2%-5%    =>4.5星 =9
	- 5%-10% =>4星		8
	- 10%-20% =>3.5星	7
	- 20%-30% =>3星 	6
	- 30%-45% =>2.5星	5
	- 45-%60% =>2星 	4
	- 60%-75% =>1.5星	3
	- 75%-90% =>1星 	2
	- 90%-100%=>0.5星	1
	*/
	
	public function __construct(UserRepository $users)
    {
        $this->users = $users;
    }
    
    public function handle(ServerRequestInterface $request): ResponseInterface
    {
        $query =  
         $this->users->query()
        	->select('id')
            ->orderBy('points_total','DESC')
            ->get();
            
        $count = $query->count();
        $points_rank = 0;
        $rankRul = [2,5,10,20,30,45,60,75,90,100];
        
        foreach ($query as	$key => $user) {
    		$rank_percentage = ($key+1)*100/($count+1);
    		
    		 foreach ($rankRul as $rulKey =>$rulValue ) {
    		 	
    		 	if($rank_percentage<$rulValue){
    		 		$points_rank=$rulKey;
    		 		break;
    		 	}
    		 
    		 }
    		 
    		$query[$key]->points_rank = 10-$points_rank;
    		$query[$key]->save();
       }
       
        return new JsonResponse('success');
    }
}
