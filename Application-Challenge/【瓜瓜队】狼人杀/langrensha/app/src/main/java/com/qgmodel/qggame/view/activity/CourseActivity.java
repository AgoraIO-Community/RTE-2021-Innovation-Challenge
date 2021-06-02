package com.qgmodel.qggame.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.bumptech.glide.Glide;
import com.qgmodel.qggame.R;
import com.qgmodel.qggame.databinding.ActivityCourseBinding;
import java.util.ArrayList;

public class CourseActivity extends AppCompatActivity {

    private ActivityCourseBinding courseBinding;
    private ArrayList<View> pageViews;

    private ImageView[] imageViews;
    private ImageView imageView;
    private int[] productPhotos = new int[]{
            R.mipmap.course_1,R.mipmap.course_2,R.mipmap.course_3,
            R.mipmap.course_4,R.mipmap.course_5,R.mipmap.course_6,
            R.mipmap.course_7};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        courseBinding = ActivityCourseBinding.inflate(LayoutInflater.from(this));
        setContentView(courseBinding.getRoot());
        initView();
    }

    private void initView() {

        // 将要分页显示的View装入数组中
        pageViews = new ArrayList<View>();
        LayoutInflater inflater = LayoutInflater.from(this);

        for(int i=0;i<productPhotos.length;i++){
            View v = inflater.inflate(R.layout.trend_descript_navigation_item, null);
            ImageView imageView = v.findViewById(R.id.img_navigation_page);
            Glide.with(this).load(productPhotos[i]).into(imageView);
            pageViews.add(v);

        }

        // 创建imageviews数组，大小是要显示的图片的数量
        imageViews = new ImageView[pageViews.size()];
        // 实例化小圆点的linearLayout和viewpager

        // 添加小圆点的图片
        for (int i = 0; i < pageViews.size(); i++) {
            imageView = new ImageView(this);

            // 设置小圆点imageview的参数
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
            layoutParams.setMargins(5, 0, 5, 0);
            imageView.setLayoutParams(layoutParams);// 创建一个宽高均为20 的布局

            // 将小圆点layout添加到数组中
            imageViews[i] = imageView;

            // 默认选中的是第一张图片，此时第一个小圆点是选中状态，其他不是
            if (i == 0) {
                imageViews[i].setBackgroundResource(R.drawable.goods_indicator_focused);
            } else {
                imageViews[i].setBackgroundResource(R.drawable.goods_indicator_unfocused);
            }

            // 将imageviews添加到小圆点视图组
            courseBinding.layoutDot.addView(imageViews[i]);
        }

        // 设置viewpager的适配器和监听事件
        courseBinding.viewpager.setAdapter(new NavigationPageAdapter());
        courseBinding.viewpager.setOnPageChangeListener(new GuidePageChangeListener());

        courseBinding.skip.setOnClickListener(view -> finish());
    }


    private class NavigationPageAdapter extends PagerAdapter {

        // 销毁position位置的界面
        @Override
        public void destroyItem(View v, int position, Object arg2) {
            ((ViewPager) v).removeView((View) arg2);
        }

        // 获取当前窗体界面数
        @Override
        public int getCount() {
            return pageViews.size();
        }

        // 初始化position位置的界面
        @Override
        public Object instantiateItem(View v, int position) {
            View contentView = pageViews.get(position);
            /**
             *显示页面的相关操作
             **/
            ((ViewPager) v).addView(contentView, 0);
            return pageViews.get(position);
        }

        @Override
        public boolean isViewFromObject(View v, Object arg1) {
            return v == arg1;
        }

        @Override
        public void startUpdate(View arg0) {
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

    }
    //设置viewpager滑动的事件，实现导航点的滚动

    private class GuidePageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onPageSelected(int arg0) {
            // 遍历数组让当前选中图片下的小圆点设置颜色
            for (int i = 0; i < imageViews.length; i++) {
                imageViews[arg0].setBackgroundResource(R.drawable.goods_indicator_focused);
                if (arg0 != i) {
                    imageViews[i].setBackgroundResource(R.drawable.goods_indicator_unfocused);
                }
            }
        }
    }
}