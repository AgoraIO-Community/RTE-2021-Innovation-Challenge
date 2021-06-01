<?php


namespace App\Models;


use Encore\Admin\Traits\AdminBuilder;
use Encore\Admin\Traits\ModelTree;
use Illuminate\Database\Eloquent\Model;

class ClassCate extends Model
{
    use ModelTree, AdminBuilder;
    protected $table = 'classes_cate';

    protected $fillable = ['pid', 'cate_name', 'sort'];

    protected $with = [
        'child'
    ];


    public function __construct(array $attributes = [])
    {
        parent::__construct($attributes);
        $this->setParentColumn('pid');  // 父ID
        $this->setOrderColumn('sort'); // 排序
        $this->setTitleColumn('cate_name'); // 标题
    }

    public static function indexCate($num=5){
        $cate = self::query()->where("show","=","1")
            ->where("pid","=",0)
            ->limit($num)->orderBy("sort")->get();
        foreach ($cate as $k=>$v){
            $indexClass = Classes::query()->where("cate1","=",$v->id)->where("show","=",1)
                ->select(['id','name','roomname','title','cate1','cate2','show','img','status','start_time','end_time'])
                ->orderBy("start_time","asc")->get();
            $indexClass = $indexClass?$indexClass->toArray():[];
            $nextClass = Classes::query()->where("cate1",$v->id)->where("show","=",2)
                ->select(['id','name','roomname','title','cate1','cate2','show','img','status','start_time','end_time'])
                ->orderBy("start_time","asc")->get();
            $nextClass = $nextClass?$nextClass->toArray():[];
            $v->classes = array_merge($indexClass,$nextClass);
        }
        return  $cate;
    }


    public static function classTree(){
        return self::query()
            ->with('child')
            ->where("pid","=",0)
            ->orderBy("sort")->get();
    }

//    // 该分类下的课程
    public function classes()
    {
        return $this->hasMany(Classes::class, 'cate1', $this->getKeyName())
            ->where("status","=",2)
            ->orderBy("sort");
    }


    /**
     * 该分类的子分类
     */
    public function child()
    {
        return $this->hasMany(get_class($this), 'pid', $this->getKeyName());
    }

//    /**
//     * 该分类的父分类
//     */
//    public function parent()
//    {
//        return $this->hasOne(get_class($this), $this->getKeyName(), 'pid');
//    }
}
