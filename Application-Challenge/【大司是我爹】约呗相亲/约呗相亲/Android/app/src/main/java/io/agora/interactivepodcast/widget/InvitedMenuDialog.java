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

import io.agora.baselibrary.base.DataBindBaseDialog;
import io.agora.baselibrary.util.ToastUtile;
import io.agora.interactivepodcast.R;
import io.agora.interactivepodcast.databinding.DialogUserInvitedBinding;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * 被邀请菜单
 *
 * @author chenhengfei@agora.io
 */
public class InvitedMenuDialog extends DataBindBaseDialog<DialogUserInvitedBinding> implements View.OnClickListener {
    private static final String TAG = InvitedMenuDialog.class.getSimpleName();

    private static final String TAG_OWNER = "owner";

    private User owner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Window win = getDialog().getWindow();
        WindowManager.LayoutParams params = win.getAttributes();
        params.gravity = Gravity.TOP;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        win.setAttributes(params);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.Dialog_Top);
    }

    @Override
    public void iniBundle(@NonNull Bundle bundle) {
        owner = (User) bundle.getSerializable(TAG_OWNER);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_user_invited;
    }

    @Override
    public void iniView() {

    }

    @Override
    public void iniListener() {
        mDataBinding.btRefuse.setOnClickListener(this);
        mDataBinding.btAgree.setOnClickListener(this);
    }

    @Override
    public void iniData() {
        setCancelable(false);
        if (getDialog() != null) {
            getDialog().setCanceledOnTouchOutside(false);
        }
        mDataBinding.tvText.setText(getString(R.string.room_dialog_invited, owner.getName()));
    }

    public void show(@NonNull FragmentManager manager, User owner) {
        Bundle intent = new Bundle();
        intent.putSerializable(TAG_OWNER, owner);
        setArguments(intent);
        super.show(manager, TAG);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btRefuse) {
            doRefuse();
        } else if (v.getId() == R.id.btAgree) {
            doAgree();
        }
    }

    private void doAgree() {
        Member mine = RoomManager.Instance(requireContext()).getMine();
        if (mine == null) {
            dismiss();
            return;
        }

        mDataBinding.btAgree.setEnabled(false);
        RoomManager.Instance(requireContext())
                .agreeInvite(mine)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mLifecycleProvider.bindToLifecycle())
                .subscribe(new DataCompletableObserver(requireContext()) {
                    @Override
                    public void handleError(@NonNull BaseError e) {
                        mDataBinding.btAgree.setEnabled(true);
                        ToastUtile.toastShort(requireContext(), e.getMessage());
                    }

                    @Override
                    public void handleSuccess() {
                        mDataBinding.btAgree.setEnabled(true);
                        dismiss();
                    }
                });
    }

    private void doRefuse() {
        Member mine = RoomManager.Instance(requireContext()).getMine();
        if (mine == null) {
            dismiss();
            return;
        }

        mDataBinding.btRefuse.setEnabled(false);
        RoomManager.Instance(requireContext())
                .refuseInvite(mine)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mLifecycleProvider.bindToLifecycle())
                .subscribe(new DataCompletableObserver(requireContext()) {
                    @Override
                    public void handleError(@NonNull BaseError e) {
                        mDataBinding.btRefuse.setEnabled(true);
                        ToastUtile.toastShort(requireContext(), e.getMessage());
                    }

                    @Override
                    public void handleSuccess() {
                        mDataBinding.btRefuse.setEnabled(true);
                        dismiss();
                    }
                });
    }
}
