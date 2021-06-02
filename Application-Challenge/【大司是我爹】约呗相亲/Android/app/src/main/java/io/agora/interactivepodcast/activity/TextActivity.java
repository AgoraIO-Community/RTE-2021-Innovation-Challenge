package io.agora.interactivepodcast.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import io.agora.baselibrary.base.DataBindBaseActivity;
import io.agora.interactivepodcast.R;
import io.agora.interactivepodcast.databinding.ActivityTextBinding;


/**
 * 文本显示界面
 *
 * @author chenhengfei@agora.io
 */
public class TextActivity extends DataBindBaseActivity<ActivityTextBinding> {

    private static final String TAG_TITLE = "title";
    private static final String TAG_TEXT = "text";

    public static Intent newIntent(Context context, String title, String text) {
        Intent intent = new Intent(context, TextActivity.class);
        intent.putExtra(TAG_TITLE, title);
        intent.putExtra(TAG_TEXT, text);
        return intent;
    }

    @Override
    protected void iniBundle(@NonNull Bundle bundle) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_text;
    }

    @Override
    protected void iniView() {

    }

    @Override
    protected void iniListener() {

    }

    @Override
    protected void iniData() {
        mDataBinding.titleBar.setTitle(getIntent().getStringExtra(TAG_TITLE));
        mDataBinding.tvText.setText(getIntent().getStringExtra(TAG_TEXT));
    }
}
