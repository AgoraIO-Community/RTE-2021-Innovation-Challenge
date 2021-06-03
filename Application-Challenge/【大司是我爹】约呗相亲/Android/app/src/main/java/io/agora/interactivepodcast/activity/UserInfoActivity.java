package io.agora.interactivepodcast.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.agora.data.BaseError;
import com.agora.data.DataRepositroy;
import com.agora.data.manager.RoomManager;
import com.agora.data.manager.UserManager;
import com.agora.data.model.User;
import com.agora.data.observer.DataObserver;
import com.bumptech.glide.Glide;

import io.agora.baselibrary.base.DataBindBaseActivity;
import io.agora.baselibrary.util.ToastUtile;
import io.agora.interactivepodcast.R;
import io.agora.interactivepodcast.databinding.ActivityUserInfoBinding;
import io.agora.interactivepodcast.widget.MenuTextView;
import io.agora.rtc.Constants;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * 用户信息界面
 *
 * @author chenhengfei@agora.io
 */
public class UserInfoActivity extends DataBindBaseActivity<ActivityUserInfoBinding> implements View.OnClickListener {

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        return intent;
    }

    @Override
    protected void iniBundle(@NonNull Bundle bundle) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void iniView() {

    }

    @Override
    protected void iniListener() {
        mDataBinding.tvMenuName.setOnClickListener(this);
        mDataBinding.tvMenuAbout.setOnClickListener(this);
        mDataBinding.audienceLatencyLevel.setOnClickListener(this);
    }

    @Override
    protected void iniData() {
        User mUser = UserManager.Instance(this).getUserLiveData().getValue();
        if (mUser == null) {
            finish();
            return;
        }
        setUserInfo(mUser);

        UserManager.Instance(this).getUserLiveData().observe(this, temp -> {
            if (temp == null) {
                return;
            }

            setUserInfo(temp);
        });

        int audienceLatencyLevel = PreferenceManager.getDefaultSharedPreferences(this).getInt(RoomManager.TAG_AUDIENCELATENCYLEVEL, Constants.AUDIENCE_LATENCY_LEVEL_ULTRA_LOW_LATENCY);
        if (audienceLatencyLevel == Constants.AUDIENCE_LATENCY_LEVEL_ULTRA_LOW_LATENCY) {
            mDataBinding.audienceLatencyLevel.setChecked(true);
        } else {
            mDataBinding.audienceLatencyLevel.setChecked(false);
        }
    }

    private void setUserInfo(@NonNull User user) {
        Glide.with(this)
                .load(user.getAvatarRes())
                .placeholder(R.mipmap.default_head)
                .error(R.mipmap.default_head)
                .circleCrop()
                .into(mDataBinding.ivUser);
        mDataBinding.tvName.setText(user.getName());
        mDataBinding.tvMenuName.setValue(user.getName());
    }

    private void showInputDialog(MenuTextView view) {
        User mUser = UserManager.Instance(this).getUserLiveData().getValue();
        if (mUser == null) {
            return;
        }

        final EditText editText = new EditText(this);
        editText.setSingleLine();
        editText.setMaxLines(1);
        editText.setLines(1);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        editText.setText(mUser.getName());
        new AlertDialog.Builder(this)
                .setView(editText)
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    String name = editText.getText().toString().trim();
                    if (TextUtils.isEmpty(name)) {
                        return;
                    }

                    User temp = mUser.clone();
                    temp.setName(name);
                    update(temp);
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void update(User user) {
        DataRepositroy.Instance(this)
                .update(user)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mLifecycleProvider.bindToLifecycle())
                .subscribe(new DataObserver<User>(this) {
                    @Override
                    public void handleError(@NonNull BaseError e) {
                        ToastUtile.toastShort(UserInfoActivity.this, e.getMessage());
                    }

                    @Override
                    public void handleSuccess(@NonNull User user) {
                        UserManager.Instance(UserInfoActivity.this).update(user);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tvMenuName) {
            showInputDialog((MenuTextView) v);
        } else if (id == R.id.tvMenuAbout) {
            gotoAboutMenu();
        } else if (id == R.id.audienceLatencyLevel) {
            boolean value = mDataBinding.audienceLatencyLevel.isChecked();
            PreferenceManager.getDefaultSharedPreferences(this)
                    .edit()
                    .putInt(RoomManager.TAG_AUDIENCELATENCYLEVEL, value ? Constants.AUDIENCE_LATENCY_LEVEL_ULTRA_LOW_LATENCY : Constants.AUDIENCE_LATENCY_LEVEL_LOW_LATENCY)
                    .apply();
        }
    }

    private void gotoAboutMenu() {
        startActivity(AboutInfoActivity.newIntent(this));
    }
}
