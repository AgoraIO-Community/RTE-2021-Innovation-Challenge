package io.agora.interactivepodcast.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import io.agora.baselibrary.base.DataBindBaseActivity;
import io.agora.interactivepodcast.R;
import io.agora.interactivepodcast.databinding.ActivityAboutInfoBinding;
import io.agora.rtc.RtcEngine;

/**
 * 关于信息界面
 *
 * @author chenhengfei@agora.io
 */
public class AboutInfoActivity extends DataBindBaseActivity<ActivityAboutInfoBinding> implements View.OnClickListener {

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, AboutInfoActivity.class);
        return intent;
    }

    @Override
    protected void iniBundle(@NonNull Bundle bundle) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about_info;
    }

    @Override
    protected void iniView() {

    }

    @Override
    protected void iniListener() {
        mDataBinding.tvMenu1.setOnClickListener(this);
        mDataBinding.tvMenu2.setOnClickListener(this);
        mDataBinding.tvMenu3.setOnClickListener(this);
    }

    @Override
    protected void iniData() {
        titleBar.setTitle(R.string.about_title);

        mDataBinding.tvMenu4.setValue("2021/03/19");
        mDataBinding.tvMenu5.setValue(RtcEngine.getSdkVersion());
        mDataBinding.tvMenu6.setValue("1.0.0");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tvMenu1) {
            clickMenu1();
        } else if (id == R.id.tvMenu2) {
            clickMenu2();
        } else if (id == R.id.tvMenu3) {
            clickMenu3();
        }
    }

    private void clickMenu1() {
        Uri uri = Uri.parse("https://www.agora.io/cn/privacy-policy/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void clickMenu2() {
        startActivity(TextActivity.newIntent(this, getString(R.string.about_product_title), getString(R.string.about_product_text)));
    }

    private void clickMenu3() {
        Uri uri = Uri.parse("https://sso.agora.io/cn/v3/signup");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
