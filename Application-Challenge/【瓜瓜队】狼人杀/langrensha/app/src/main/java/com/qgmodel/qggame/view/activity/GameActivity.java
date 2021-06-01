package com.qgmodel.qggame.view.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.qgmodel.qggame.R;
import com.qgmodel.qggame.databinding.ActivityGameBinding;
import com.qgmodel.qggame.utils.ViewUtils;

/**
 * Created by HeYanLe on 2020/8/29 0029 14:29.
 * https://github.com/heyanLE
 */
public class GameActivity extends AppCompatActivity {

    private ActivityGameBinding mBinding ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityGameBinding.inflate(LayoutInflater.from(this));
        setContentView(mBinding.getRoot());

        ViewUtils.setTitleCenter(mBinding.toolbar);
        mBinding.toolbar.setNavigationIcon(R.mipmap.back);
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 游戏界面返回

            }
        });
    }
}
