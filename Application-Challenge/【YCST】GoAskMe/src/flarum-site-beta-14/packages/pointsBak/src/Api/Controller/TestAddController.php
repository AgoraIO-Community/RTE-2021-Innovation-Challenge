<?php

namespace GoAskMe\Points\Api\Controller;

use Flarum\Settings\SettingsRepositoryInterface;
use GoAskMe\Points\PointsRepository;
use Laminas\Diactoros\Response\JsonResponse;
use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;
use Psr\Http\Server\RequestHandlerInterface;

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
        $this->points->changePoints(-5, $actor, 'test');
        return new JsonResponse('success');
    }
}
