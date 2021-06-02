<?php

namespace GoAskMe\Points\Listener;

use Flarum\Discussion\Event\Hidden as DiscussionHidden;
use Flarum\Discussion\Event\Restored as DiscussionRestored;
use Flarum\Discussion\Event\Started as DiscussionStarted;
use Flarum\Likes\Event\PostWasLiked;
use Flarum\Likes\Event\PostWasUnliked;
use Flarum\Post\Event\Posted;
use Flarum\Post\Event\Hidden as PostHidden;
use Flarum\Post\Event\Restored as PostRestored;
use GoAskMe\Points\GamPointsLog;
use GoAskMe\Points\PointsRepository;
use Illuminate\Contracts\Events\Dispatcher;
use Illuminate\Database\ConnectionInterface;

use Flarum\Api\Event\Serializing;
use Flarum\Api\Serializer\UserSerializer;

class UserActions
{
    protected $db;
    protected $points;

    public function __construct(ConnectionInterface $db, PointsRepository $points)
    {
        $this->db = $db;
        $this->points = $points;
    }

    public function subscribe(Dispatcher $events)
    {
        // Post
        $events->listen(Posted::class, [$this, 'postedHandler']);
        $events->listen(PostHidden::class, [$this, 'postHiddenHandler']);
        $events->listen(PostRestored::class, [$this, 'postRestoredHandler']);

        // Discussion
        $events->listen(DiscussionStarted::class, [$this, 'discussionStartedHandler']);
        $events->listen(DiscussionHidden::class, [$this, 'discussionHiddenHandler']);
        $events->listen(DiscussionRestored::class, [$this, 'discussionRestoredHandler']);

        // Like
        $events->listen(PostWasLiked::class, [$this, 'postWasLikedHandler']);
        $events->listen(PostWasUnliked::class, [$this, 'postWasUnlikedHandler']);

        // load
        $events->listen(Serializing::class, [$this, 'prepareApiAttributes']);
        
        // TODO: 注册后给邀请人加分
    }

    public function prepareApiAttributes(Serializing $event) {
        if ($event->isSerializer(UserSerializer::class)) {
            $event->attributes['pointsCount'] = $event->model->points_count;
            $event->attributes['pointsRank'] = $event->model->points_rank;
        }

    }

    public function postedHandler(Posted $event)
    {
        // If it's not the first post of a discussion
        if ($event->post['number'] > 1) {
            $actor = $event->actor;
            $post = $event->post;
            $this->db->beginTransaction();
            try {
                $points = $this->points->checkUserProbability($actor, Posted::class);
                if ($points > 0) {
                    $this->points->changePoints($points, $actor, Posted::class, null, $post);
                    $this->points->resetUserProbability($actor);
                } else {
                    $this->points->accumulateUserProbability($actor, Posted::class);
                }
                $this->db->commit();
            } catch (\Throwable $th) {
                $this->db->rollBack();
                throw $th;
            }
        }
    }

    public function postHiddenHandler(PostHidden $event)
    {
        $post = $event->post;
        $lastRecord = GamPointsLog::query()
            ->where('post_id', $post->id)
            ->where('type', Posted::class)
            ->first();
        // 扣除对应积分
        if ($lastRecord) {
            // last ammount
            $amount = $lastRecord->amount;
            $this->points->changePoints(-$amount, $post->user, PostHidden::class, null, $post);
        }
    }

    public function postRestoredHandler(PostRestored $event)
    {
        $post = $event->post;
        $lastRecord = GamPointsLog::query()
            ->where('post_id', $post->id)
            ->where('type', PostHidden::class)
            ->first();
        // 恢复对应积分
        if ($lastRecord) {
            // last ammount
            $amount = $lastRecord->amount;
            $this->points->changePoints(-$amount, $post->user, PostRestored::class, null, $post);
        }
    }

    public function discussionStartedHandler(DiscussionStarted $event)
    {
        $actor = $event->actor;
        $discussion = $event->discussion;
        $this->db->beginTransaction();
        try {
            $points = $this->points->checkUserProbability($actor, DiscussionStarted::class);
            if ($points > 0) {
                $this->points->changePoints($points, $actor, DiscussionStarted::class, $discussion, null);
                $this->points->resetUserProbability($actor);
            } else {
                $this->points->accumulateUserProbability($actor, DiscussionStarted::class);
            }
            $this->db->commit();
        } catch (\Throwable $th) {
            $this->db->rollBack();
            throw $th;
        }
    }

    public function discussionHiddenHandler(DiscussionHidden $event)
    {
        $discussion = $event->discussion;
        $lastRecord = GamPointsLog::query()
            ->where('discussion_id', $discussion->id)
            ->where('type', DiscussionStarted::class)
            ->first();
        // 扣除对应积分
        if ($lastRecord) {
            // last ammount
            $amount = $lastRecord->amount;
            $this->points->changePoints(-$amount, $discussion->user, DiscussionHidden::class, $discussion, null);
        }
    }

    public function discussionRestoredHandler(DiscussionRestored $event)
    {
        $discussion = $event->discussion;
        $lastRecord = GamPointsLog::query()
            ->where('discussion_id', $discussion->id)
            ->where('type', DiscussionHidden::class)
            ->first();
        // 恢复对应积分
        if ($lastRecord) {
            // last ammount
            $amount = $lastRecord->amount;
            $this->points->changePoints(-$amount, $discussion->user, DiscussionRestored::class, $discussion, null);
        }
    }

    public function postWasLikedHandler(PostWasLiked $event)
    {
        if ($event->post->user && $event->post->user->id != $event->user->id) {
            //$actor = $event->user;;
            $actor = $event->post->user; // bei dian zan huo qu ji fen
            $post = $event->post;
            $this->db->beginTransaction();
            try {
                $points = $this->points->checkUserProbability($actor, PostWasLiked::class);
                if ($points > 0) {
                    $this->points->changePoints($points, $actor, PostWasLiked::class, null, $post);
                    $this->points->resetUserProbability($actor);
                } else {
                    $this->points->accumulateUserProbability($actor, PostWasLiked::class);
                }
                $this->db->commit();
            } catch (\Throwable $th) {
                $this->db->rollBack();
                throw $th;
            }
        }
    }

    public function postWasUnlikedHandler(PostWasUnliked $event)
    {
        $post = $event->post;
        $actor = $event->actor;
        $lastRecord = GamPointsLog::query()
            ->where('post_id', $post->id)
            ->where('owner_id', $actor->id)
            ->where('type', PostWasLiked::class)
            ->first();
        // 扣除对应积分
        if ($lastRecord) {
            // last ammount
            $amount = $lastRecord->amount;
            $this->points->changePoints(-$amount, $post->user, DiscussionHidden::class, null, $post);
        }
    }
}
