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
import com.agora.data.manager.RoomManager;
import com.agora.data.model.Member;
import com.agora.data.model.User;
import com.agora.data.observer.DataCompletableObserver;
import com.bumptech.glide.Glide;

import io.agora.baselibrary.base.DataBindBaseDialog;
import io.agora.baselibrary.util.ToastUtile;
import io.agora.interactivepodcast.R;
import io.agora.interactivepodcast.databinding.DialogUserSeatMenuBinding;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * 房间用户菜单
 *
 * @author chenhengfei@agora.io
 */
public class UserSeatMenuDialog extends DataBindBaseDialog<DialogUserSeatMenuBinding> implements View.OnClickListener {
    private static final String TAG = UserSeatMenuDialog.class.getSimpleName();

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
        return R.layout.dialog_user_seat_menu;
    }

    @Override
    public void iniView() {

    }

    @Override
    public void iniListener() {
        mDataBinding.btSeatoff.setOnClickListener(this);
        mDataBinding.btAudio.setOnClickListener(this);
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
        refreshAudioView();
    }

    private void refreshAudioView() {
        if (mMember.getIsMuted() == 1) {
            mDataBinding.ivAudio.setImageResource(R.mipmap.icon_microphoneon);
            mDataBinding.btAudio.setText(R.string.room_dialog_open_audio);
        } else if (mMember.getIsSelfMuted() == 1) {
            mDataBinding.ivAudio.setImageResource(R.mipmap.icon_microphoneon);
            mDataBinding.btAudio.setText(R.string.room_dialog_open_audio);
        } else {
            mDataBinding.ivAudio.setImageResource(R.mipmap.icon_microphoneon);
            mDataBinding.btAudio.setText(R.string.room_dialog_close_audio);
        }
    }

    public void show(@NonNull FragmentManager manager, Member data) {
        Bundle intent = new Bundle();
        intent.putSerializable(TAG_USER, data);
        setArguments(intent);
        super.show(manager, TAG);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btSeatoff) {
            seatOff();
        } else if (v.getId() == R.id.btAudio) {
            toggleAudio();
        }
    }

    private void toggleAudio() {
        if (!RoomManager.Instance(requireContext()).isOwner()) {
            Member member = RoomManager.Instance(requireContext()).getMine();
            if (member == null) {
                return;
            }

            if (member.getIsMuted() == 1) {
                ToastUtile.toastShort(requireContext(), R.string.member_muted);
                return;
            }
        }

        mDataBinding.btAudio.setEnabled(false);
        RoomManager.Instance(requireContext())
                .toggleTargetAudio(mMember)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mLifecycleProvider.bindToLifecycle())
                .subscribe(new DataCompletableObserver(requireContext()) {
                    @Override
                    public void handleError(@NonNull BaseError e) {
                        mDataBinding.btAudio.setEnabled(true);
                        ToastUtile.toastShort(requireContext(), e.getMessage());
                    }

                    @Override
                    public void handleSuccess() {
                        mDataBinding.btAudio.setEnabled(true);
                        refreshAudioView();
                    }
                });
    }

    private void seatOff() {
        mDataBinding.btSeatoff.setEnabled(false);
        RoomManager.Instance(requireContext())
                .seatOff(mMember)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mLifecycleProvider.bindToLifecycle())
                .subscribe(new DataCompletableObserver(requireContext()) {
                    @Override
                    public void handleError(@NonNull BaseError e) {
                        mDataBinding.btSeatoff.setEnabled(true);
                        ToastUtile.toastShort(requireContext(), e.getMessage());
                    }

                    @Override
                    public void handleSuccess() {
                        mDataBinding.btSeatoff.setEnabled(true);
                        dismiss();
                    }
                });
    }
}
