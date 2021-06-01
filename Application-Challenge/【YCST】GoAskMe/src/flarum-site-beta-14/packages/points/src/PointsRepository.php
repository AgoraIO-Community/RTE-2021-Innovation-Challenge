<?php

namespace GoAskMe\Points;

use Carbon\Carbon;
use Flarum\Discussion\Discussion;
use Flarum\Discussion\Event\Started as DiscussionStarted;
use Flarum\Likes\Event\PostWasLiked;
use Flarum\Post\Post;
use Flarum\Post\Event\Posted;
use Flarum\User\User;
use Illuminate\Database\ConnectionInterface;
use Psr\Log\LoggerInterface;

/**
 * "Box–Muller transform" based random deviate generator.
 *
 * @ref https://en.wikipedia.org/wiki/Box%E2%80%93Muller_transform
 *
 * @param  float|int $av Average/Mean
 * @param  float|int $sd Standard deviation
 * @return float
 */
if (!function_exists('stats_rand_gen_normal')) {
    function stats_rand_gen_normal($av, $sd): float
    {
        $x = mt_rand() / mt_getrandmax();
        $y = mt_rand() / mt_getrandmax();

        return sqrt(-2 * log($x)) * cos(2 * pi() * $y) * $sd + $av;
    }
}

class PointsRepository
{
    protected $db;
    protected $logger;

    /**
     * 不同类型事件的概率配置
     */
    public static $pointsBaseConfig = [
        Posted::class => [
            'probability' => 0.05,
        ],
        DiscussionStarted::class => [
            'probability' => 0.1,
        ],
        PostWasLiked::class => [
            'probability' => 0.25,
        ],
    ];

    public function __construct(ConnectionInterface $db, LoggerInterface $logger)
    {
        $this->db = $db;
        $this->logger = $logger;
    }

    /**
     * 检测用户此次是否可以获奖
     *
     * @param User $user 执行操作的用户
     * @param $eventType 事件类型
     * @return int 返回需增加的 Points，没有则为 0
     */
    public function checkUserProbability(User $user, $eventType = '')
    {
        if ($prob = $user->points_probability) {
            $prob = json_decode($prob, true);
        }
        if (!$prob) {
            $this->resetUserProbability($user);
            $prob = self::$pointsBaseConfig;
        }
        $currentEv = $prob[$eventType];

        $points = 0;

        // Mersenne Twister，均匀分布
        if (mt_rand(0, 100) / 100 < $currentEv['probability']) {
            // 加积分数，服从正态分布，保留四位小数，这里存为整数
            // 期望：150 标准差：50 / 3
            // $points = (int) floor(stats_rand_gen_normal(150, 50 / 3) * 10000);
            $points = (int) floor(stats_rand_gen_normal(5, 1));
        }

        $this->logger->info("check user probability. user: {$user->id}, evType: $eventType, points: $points");
        return $points;
    }

    /**
     * 成功得到积分后重置概率
     *
     * @param User $user 执行操作的用户
     */
    public function resetUserProbability(User $user)
    {
        $user->points_probability = json_encode(self::$pointsBaseConfig);
        $user->save();
        $this->logger->info("reset probability. user: {$user->id}");
    }

    /**
     * 未得到积分，概率叠加至 80%
     *
     * @param User $user 执行操作的用户
     * @param $eventType 事件类型
     */
    public function accumulateUserProbability(User $user, $eventType = '')
    {
        $prob = json_decode($user->points_probability, true); 
        $currentProb = $prob[$eventType]['probability'];
        $finalProb = $currentProb + self::$pointsBaseConfig[$eventType]['probability'];
        if ($finalProb > 0.8) {
            $finalProb = 0.8;
        }
        if ($currentProb < 0.8) {
            $prob[$eventType]['probability'] = $finalProb;
            $user->points_probability = json_encode($prob);
            $user->save();
        }
        $this->logger->info("accumulate probability. user: {$user->id}, init: $currentProb, final: $finalProb, evType: $eventType");
    }

    /**
     * 修改 GAM 点数，留下变更历史记录
     *
     * @param int $points
     * @param User $user
     * @param $type
     * @param Discussion $discussion
     * @param Post $post
     * @param $extra
     */
    public function changePoints($points, User $user, $type = 'test', Discussion $discussion = null, Post $post = null, $extra = '')
    {
        $this->db->beginTransaction();
        try {
            $pointsLog = new GamPointsLog();
            $pointsLog->owner_id = $user->id;
            if ($discussion) {
                $pointsLog->discussion_id = $discussion->id;
            }
            if ($post) {
                $pointsLog->post_id = $post->id;
            }
            $pointsLog->type = $type;
            $pointsLog->amount = $points;
            $pointsLog->current = $user->points_count + $points;
            $pointsLog->extra = $extra;
            $pointsLog->created_at = Carbon::now();
            $pointsLog->save();
            
            if($points > 0 && $type != 'converRejectReturn' ){
            	$user->points_total += $points;
            }
            
            $user->points_count += $points;
            $user->save();

            $this->db->commit();
        } catch (\Throwable $th) {
            $this->db->rollBack();
            throw $th;
        }
    }
}
