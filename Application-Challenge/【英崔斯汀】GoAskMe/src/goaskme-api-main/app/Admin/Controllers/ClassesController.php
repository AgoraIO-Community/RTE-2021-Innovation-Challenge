<?php

namespace App\Admin\Controllers;

use App\Models\Classes;
use Encore\Admin\Controllers\AdminController;
use Encore\Admin\Form;
use Encore\Admin\Grid;
use Encore\Admin\Show;

class ClassesController extends AdminController
{
    /**
     * Title for current resource.
     *
     * @var string
     */
    protected $title = 'Classes';

    /**
     * Make a grid builder.
     *
     * @return Grid
     */
    protected function grid()
    {
        $grid = new Grid(new Classes());

        $grid->column('id', __('Id'));
        $grid->column('name', __('名称'));
        $grid->column('title', __('标题'));
        $grid->column('sort', __('排序'));
        $grid->column('show',__('首页展示'))->switch([
            'on'  => ['value' => 1, 'text' => '是', 'color' => 'success'],
            'off' => ['value' => 2, 'text' => '否', 'color' => 'danger'],
        ]);
        $grid->column('status',__('状态'))->switch([
            'on'  => ['value' => 2, 'text' => '审核通过', 'color' => 'success'],
            'off' => ['value' => 3, 'text' => '审核不通过', 'color' => 'danger'],
        ]);
        $grid->column('created_at', '创建时间');
        return $grid;
    }

    /**
     * Make a show builder.
     *
     * @param mixed $id
     * @return Show
     */
    protected function detail($id)
    {
        $show = new Show(Classes::findOrFail($id));

        $show->field('id', __('Id'));
        $show->field('name', __('名称'));
        $show->field('title', __('标题'));
        $show->field('sort', __('排序'));
        return $show;
    }

    /**
     * Make a form builder.
     *
     * @return Form
     */
    protected function form()
    {
        $form = new Form(new Classes());

        $form->text('name', __('名称'));
        $form->text('title', __('标题'));
        $form->image('img', __('封面图'))->required()->name(function($file) {
            //为用户用户文件名添加前缀
            return md5(microtime()).'.' . $file->extension();
        });

        $states = [
            'on'  => ['value' => 1, 'text' => '是', 'color' => 'success'],
            'off' => ['value' => 2, 'text' => '否', 'color' => 'danger'],
        ];
        $form->switch('show',__('是否首页展示'))->states($states);
        $form->number('sort', __('排序'));
        $states = [
            'on'  => ['value' => 2, 'text' => '审核通过', 'color' => 'success'],
            'off' => ['value' => 3, 'text' => '审核不通过', 'color' => 'danger'],
        ];
        $form->switch('status',__('状态'))->states($states);

        return $form;
    }
}
