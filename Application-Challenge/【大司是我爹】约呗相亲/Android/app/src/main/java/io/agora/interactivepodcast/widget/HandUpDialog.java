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
import com.agora.data.manager.RoomManager;
import com.agora.data.model.Member;
import com.agora.data.model.Room;
import com.agora.data.observer.DataCompletableObserver;
import com.agora.data.observer.DataObserver;

import java.util.List;

import io.agora.baselibrary.base.DataBindBaseDialog;
import io.agora.baselibrary.base.OnItemClickListener;
import io.agora.baselibrary.util.ToastUtile;
import io.agora.interactivepodcast.R;
import io.agora.interactivepodcast.adapter.HandsUpListAdapter;
import io.agora.interactivepodcast.databinding.DialogHandUpBinding;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * 举手列表
 *
 * @author chenhengfei@agora.io
 */
public class HandUpDialog extends DataBindBaseDialog<DialogHandUpBinding> implements OnItemClickListener<Member> {
    private static final String TAG = HandUpDialog.class.getSimpleName();

    private HandsUpListAdapter mAdapter;

    private static final String TAG_ROOM = "room";

    private Room room;

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
        room = (Room) bundle.getSerializable(TAG_ROOM);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_hand_up;
    }

    @Override
    public void iniView() {

    }

    @Override
    public void iniListener() {

    }

    @Override
    public void iniData() {
        mAdapter = new HandsUpListAdapter(null, this);
        mDataBinding.rvList.setAdapter(mAdapter);

        loadData();
    }

    public void loadData() {
        DataRepositroy.Instance(requireContext())
                .getHandUpList()
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mLifecycleProvider.bindToLifecycle())
                .subscribe(new DataObserver<List<Member>>(requireContext()) {

                    @Override
                    public void handleError(@NonNull BaseError e) {

                    }

                    @Override
                    public void handleSuccess(@NonNull List<Member> members) {
                        mAdapter.setDatas(members);
                    }
                });
    }

    public void show(@NonNull FragmentManager manager, @NonNull Room room) {
        Bundle intent = new Bundle();
        intent.putSerializable(TAG_ROOM, room);
        setArguments(intent);
        super.show(manager, TAG);
    }

    @Override
    public void onItemClick(@NonNull Member data, View view, int position, long id) {
        if (view.getId() == R.id.btRefuse) {
            clickRefuse(position, data);
        } else if (view.getId() == R.id.btAgree) {
            clickAgree(position, data);
        }
    }

    private void clickRefuse(int index, Member data) {
        RoomManager.Instance(requireContext())
                .refuseHandsUp(data)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mLifecycleProvider.bindToLifecycle())
                .subscribe(new DataCompletableObserver(requireContext()) {
                    @Override
                    public void handleError(@NonNull BaseError e) {
                        ToastUtile.toastShort(requireContext(), e.getMessage());
                    }

                    @Override
                    public void handleSuccess() {
                        mAdapter.deleteItem(index);
                    }
                });
    }

    private void clickAgree(int index, Member data) {
        RoomManager.Instance(requireContext())
                .agreeHandsUp(data)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mLifecycleProvider.bindToLifecycle())
                .subscribe(new DataCompletableObserver(requireContext()) {
                    @Override
                    public void handleError(@NonNull BaseError e) {
                        ToastUtile.toastShort(requireContext(), e.getMessage());
                    }

                    @Override
                    public void handleSuccess() {
                        mAdapter.deleteItem(index);
                    }
                });
    }
}
