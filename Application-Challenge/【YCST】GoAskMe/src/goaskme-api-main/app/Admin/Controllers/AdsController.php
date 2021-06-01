<?php


namespace App\Admin\Controllers;

use App\Models\Ads;
use Encore\Admin\Controllers\AdminController;
use Encore\Admin\Form;
use Encore\Admin\Grid;
use Encore\Admin\Show;

class AdsController extends AdminController
{

    protected $title = '广告列表';

    protected function grid()
    {
        $grid = new Grid(new Ads());

        $grid->column('id', __('Id'));
        $grid->column('img', __('封面图'));
        $grid->column('video', __('广告视频'));
        $grid->column('sort', __('排序'));
        $grid->column('status', __('状态'))->switch([
            'on' => ['value' => 1, 'text' => '开启', 'color' => 'success'],
            'off' => ['value' => 0, 'text' => '关闭', 'color' => 'danger'],
        ]);
        $grid->column('created_at', '创建时间');
        return $grid;
    }

    protected function detail($id)
    {
        $show = new Show(Ads::findOrFail($id));

        $show->field('id', __('Id'));
        $show->field('img', __('封面图'));
        $show->field('video', __('广告视频'));
        $show->field('sort', __('排序'));
        return $show;
    }

    protected function form()
    {
        $form = new Form(new Ads());

        $form->image('img', __('封面图'))->required()->name(function ($file) {
            return md5(microtime()) . '.' . $file->extension();
        });
        $form->text('video', __('广告视频地址'));
//        $form->file('video', __('广告视频'))->required()->name(function ($file) {
//            return md5(microtime()) . '.' . $file->extension();
//        });
        $form->number('sort', __('排序'));
        $states = [
            'on' => ['value' => 1, 'text' => '开启', 'color' => 'success'],
            'off' => ['value' => 0, 'text' => '关闭', 'color' => 'danger'],
        ];
        $form->switch('status', __('状态'))->states($states);

        return $form;
    }


}
