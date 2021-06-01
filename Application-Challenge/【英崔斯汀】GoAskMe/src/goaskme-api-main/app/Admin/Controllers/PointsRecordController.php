<?php

namespace App\Admin\Controllers;

use App\Models\PointsRecord;
use Encore\Admin\Controllers\AdminController;
use Encore\Admin\Form;
use Encore\Admin\Grid;
use Encore\Admin\Show;

class PointsRecordController extends AdminController
{
    /**
     * Title for current resource.
     *
     * @var string
     */
    protected $title = 'PointsRecord';

    /**
     * Make a grid builder.
     *
     * @return Grid
     */
    protected function grid()
    {
        $grid = new Grid(new PointsRecord());
        $grid->disableCreateButton();
        $grid->column('id', __('Id'));
        $grid->column('username', __('Username'));
        $grid->column('type', __('Type'))->using(PointsRecord::$exchangeType);
        $grid->column('obj_name', __('使用对象'));
        $grid->column('deleted_at', __('Deleted at'));

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
        $show = new Show(PointsRecord::findOrFail($id));

        $show->field('id', __('Id'));
        $show->field('username', __('Username'));
        $show->field('type', __('Type'))->using(PointsRecord::$exchangeType);
        $show->field('obj_name', __('使用对象'));

        return $show;
    }

    /**
     * Make a form builder.
     *
     * @return Form
     */
    protected function form()
    {
        $form = new Form(new PointsRecord());

        $form->text('username', __('Username'));
        $form->number('user_id', __('User id'));
        $form->switch('type', __('Type'));
        $form->number('obj_id', __('Obj id'));

        return $form;
    }
}
