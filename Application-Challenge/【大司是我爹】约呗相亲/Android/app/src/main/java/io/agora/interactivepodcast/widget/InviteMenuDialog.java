package io.agora.interactivepodcast.widget;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.agora.data.BaseError;
import com.agora.data.DataRepositroy;
import com.agora.data.model.Member;
import com.agora.data.model.User;
import com.agora.data.observer.DataCompletableObserver;
import com.bumptech.glide.Glide;

import io.agora.baselibrary.base.DataBindBaseDialog;
import io.agora.baselibrary.util.ToastUtile;
import io.agora.interactivepodcast.R;
import io.agora.interactivepodcast.databinding.DialogUserInviteBinding;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * 邀请菜单
 *
 * @author chenhengfei@agora.io
 */
public class InviteMenuDialog extends DataBindBaseDialog<DialogUserInviteBinding> implements View.OnClickListener {
    private static final String TAG = InviteMenuDialog.class.getSimpleName();

    private static final String TAG_USER = "user";

    private Member mMember;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Window win = getDialog().getWindow();
        WindowManager.LayoutParams params = win.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        win.setAttributes(params);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.Dialog_Bottom);
    }

    @Override
    public void iniBundle(@NonNull Bundle bundle) {
        mMember = (Member) bundle.getSerializable(TAG_USER);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_user_invite;
    }

    @Override
    public void iniView() {

    }

    @Override
    public void iniListener() {
        mDataBinding.btFuntion.setOnClickListener(this);
    }

    @Override
    public void iniData() {
        User mUser = mMember.getUserId();
        mDataBinding.tvName.setText(mUser.getName());
        Glide.with(this)
                .load(mUser.getAvatarRes())
                .placeholder(R.mipmap.default_head)
                .error(R.mipmap.default_head)
                .circleCrop()
                .into(mDataBinding.ivUser);
    }

    public void show(@NonNull FragmentManager manager, Member data) {
        Bundle intent = new Bundle();
        intent.putSerializable(TAG_USER, data);
        setArguments(intent);
        super.show(manager, TAG);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btFuntion) {
            invite();
        }
    }

    private void invite() {
        mDataBinding.btFuntion.setEnabled(false);
        DataRepositroy.Instance(requireContext())
                .inviteSeat(mMember)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mLifecycleProvider.bindToLifecycle())
                .subscribe(new DataCompletableObserver(requireContext()) {
                    @Override
                    public void handleError(@NonNull BaseError e) {
                        mDataBinding.btFuntion.setEnabled(true);
                        ToastUtile.toastShort(requireContext(), e.getMessage());
                    }

                    @Override
                    public void handleSuccess() {
                        mDataBinding.btFuntion.setEnabled(true);
                        dismiss();
                    }
                });
    }
}
