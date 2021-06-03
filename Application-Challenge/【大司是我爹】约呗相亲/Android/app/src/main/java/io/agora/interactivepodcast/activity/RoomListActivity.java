package io.agora.interactivepodcast.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.ObjectsCompat;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.agora.data.BaseError;
import com.agora.data.DataRepositroy;
import com.agora.data.RoomEventCallback;
import com.agora.data.manager.RoomManager;
import com.agora.data.manager.UserManager;
import com.agora.data.model.Member;
import com.agora.data.model.Room;
import com.agora.data.model.User;
import com.agora.data.observer.DataCompletableObserver;
import com.agora.data.observer.DataMaybeObserver;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import io.agora.baselibrary.base.DataBindBaseActivity;
import io.agora.baselibrary.base.OnItemClickListener;
import io.agora.baselibrary.util.ToastUtile;
import io.agora.interactivepodcast.R;
import io.agora.interactivepodcast.adapter.RoomListAdapter;
import io.agora.interactivepodcast.databinding.ActivityRoomListBinding;
import io.agora.interactivepodcast.widget.CreateRoomDialog;
import io.agora.interactivepodcast.widget.HandUpDialog;
import io.agora.interactivepodcast.widget.SpaceItemDecoration;
import io.reactivex.android.schedulers.AndroidSchedulers;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 房间列表
 *
 * @author chenhengfei@agora.io
 */
public class RoomListActivity extends DataBindBaseActivity<ActivityRoomListBinding> implements View.OnClickListener,
        OnItemClickListener<Room>, EasyPermissions.PermissionCallbacks, SwipeRefreshLayout.OnRefreshListener, RoomEventCallback {

    private static final int TAG_PERMISSTION_REQUESTCODE = 1000;
    private static final String[] PERMISSTION = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO};

    private RoomListAdapter mAdapter;

    private Room mRoom;

    @Override
    protected void iniBundle(@NonNull Bundle bundle) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_room_list;
    }

    @Override
    protected void iniView() {
        mAdapter = new RoomListAdapter(null, this);
        mDataBinding.list.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mDataBinding.list.setAdapter(mAdapter);
        mDataBinding.list.addItemDecoration(new SpaceItemDecoration(this));
    }

    @Override
    protected void iniListener() {
        RoomManager.Instance(this).addRoomEventCallback(this);
        mDataBinding.swipeRefreshLayout.setOnRefreshListener(this);
        mDataBinding.ivHead.setOnClickListener(this);
        mDataBinding.btCrateRoom.setOnClickListener(this);

        mDataBinding.ivExit.setOnClickListener(this);
        mDataBinding.ivNews.setOnClickListener(this);
        mDataBinding.ivAudio.setOnClickListener(this);
        mDataBinding.ivHandUp.setOnClickListener(this);
        mDataBinding.llMin.setOnClickListener(this);
    }

    @Override
    protected void iniData() {
        mDataBinding.btCrateRoom.setVisibility(View.VISIBLE);
        mDataBinding.llMin.setVisibility(View.GONE);

        User user = UserManager.Instance(this).getUserLiveData().getValue();
        if (user != null) {
            setUser(user);
        }

        UserManager.Instance(this).getUserLiveData().observe(this, mUser -> {
            if (mUser == null) {
                return;
            }
            setUser(mUser);
        });

        mDataBinding.tvEmpty.setVisibility(mAdapter.getItemCount() <= 0 ? View.VISIBLE : View.GONE);
        loadRooms();
    }

    private void setUser(@NonNull User user) {
        Glide.with(RoomListActivity.this)
                .load(user.getAvatarRes())
                .placeholder(R.mipmap.default_head)
                .circleCrop()
                .error(R.mipmap.default_head)
                .into(mDataBinding.ivHead);
    }

    private void loadRooms() {
        DataRepositroy.Instance(this)
                .getRooms()
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mLifecycleProvider.bindToLifecycle())
                .subscribe(new DataMaybeObserver<List<Room>>(this) {
                    @Override
                    public void handleError(@NonNull BaseError e) {
                        mDataBinding.swipeRefreshLayout.setRefreshing(false);
                        ToastUtile.toastShort(RoomListActivity.this, e.getMessage());
                    }

                    @Override
                    public void handleSuccess(@Nullable List<Room> rooms) {
                        mDataBinding.swipeRefreshLayout.setRefreshing(false);

                        if (rooms == null) {
                            mAdapter.clear();
                        } else {
                            mAdapter.setDatas(rooms);
                        }
                        mDataBinding.tvEmpty.setVisibility(mAdapter.getItemCount() <= 0 ? View.VISIBLE : View.GONE);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btCrateRoom) {
            gotoCreateRoom();
        } else if (v.getId() == R.id.ivHead) {
            Intent intent = UserInfoActivity.newIntent(this);
            startActivity(intent);
        } else if (v.getId() == R.id.ivExit) {
            exitRoom();
        } else if (v.getId() == R.id.ivNews) {
            gotoHandUpListDialog();
        } else if (v.getId() == R.id.ivAudio) {
            toggleAudio();
        } else if (v.getId() == R.id.ivHandUp) {
            toggleHandUp();
        } else if (v.getId() == R.id.llMin) {
            rebackRoom();
        }
    }

    private void rebackRoom() {
        Room room = RoomManager.Instance(this).getRoom();
        if (room == null) {
            mDataBinding.btCrateRoom.setVisibility(View.VISIBLE);
            mDataBinding.llMin.setVisibility(View.GONE);
            return;
        }

        Intent intent = ChatRoomActivity.newIntent(this, room);
        startActivity(intent);
        overridePendingTransition(R.anim.chat_room_in, R.anim.chat_room_out);
    }

    private void exitRoom() {
        RoomManager.Instance(this).leaveRoom();

        mDataBinding.btCrateRoom.setVisibility(View.VISIBLE);
        mDataBinding.llMin.setVisibility(View.GONE);
    }

    private void gotoHandUpListDialog() {
        Room room = RoomManager.Instance(this).getRoom();
        if (room == null) {
            return;
        }
        new HandUpDialog().show(getSupportFragmentManager(), room);
    }

    private void refreshVoiceView() {
        Member member = RoomManager.Instance(this).getMine();
        if (member == null) {
            return;
        }

        if (RoomManager.Instance(this).isOwner()) {
            mDataBinding.ivAudio.setVisibility(View.VISIBLE);
            if (member.getIsMuted() == 1) {
                mDataBinding.ivAudio.setImageResource(R.mipmap.icon_microphoneoff);
            } else if (member.getIsSelfMuted() == 1) {
                mDataBinding.ivAudio.setImageResource(R.mipmap.icon_microphoneoff);
            } else {
                mDataBinding.ivAudio.setImageResource(R.mipmap.icon_microphoneon);
            }
        } else {
            if (member.getIsSpeaker() == 0) {
                mDataBinding.ivAudio.setVisibility(View.GONE);
            } else {
                mDataBinding.ivAudio.setVisibility(View.VISIBLE);
                if (member.getIsMuted() == 1) {
                    mDataBinding.ivAudio.setImageResource(R.mipmap.icon_microphoneoff);
                } else if (member.getIsSelfMuted() == 1) {
                    mDataBinding.ivAudio.setImageResource(R.mipmap.icon_microphoneoff);
                } else {
                    mDataBinding.ivAudio.setImageResource(R.mipmap.icon_microphoneon);
                }
            }
        }
    }

    private void toggleAudio() {
        if (!RoomManager.Instance(this).isOwner()) {
            Member mine = RoomManager.Instance(this).getMine();
            if (mine == null) {
                return;
            }

            if (mine.getIsMuted() == 1) {
                ToastUtile.toastShort(this, R.string.member_muted);
                return;
            }
        }

        mDataBinding.ivAudio.setEnabled(false);
        RoomManager.Instance(this)
                .toggleSelfAudio()
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mLifecycleProvider.bindToLifecycle())
                .subscribe(new DataCompletableObserver(this) {
                    @Override
                    public void handleError(@NonNull BaseError e) {
                        mDataBinding.ivAudio.setEnabled(true);
                        ToastUtile.toastShort(RoomListActivity.this, e.getMessage());
                    }

                    @Override
                    public void handleSuccess() {
                        mDataBinding.ivAudio.setEnabled(true);
                        refreshVoiceView();
                    }
                });
    }

    private void refreshHandUpView() {
        if (RoomManager.Instance(this).isOwner()) {
            mDataBinding.ivHandUp.setVisibility(View.GONE);
        } else {
            mDataBinding.ivHandUp.setImageResource(R.mipmap.icon_un_handup);

            Member member = RoomManager.Instance(this).getMine();
            if (member != null) {
                mDataBinding.ivHandUp.setVisibility(member.getIsSpeaker() == 0 ? View.VISIBLE : View.GONE);
            }
        }
    }

    private void toggleHandUp() {
        mDataBinding.ivHandUp.setEnabled(false);
        RoomManager.Instance(this).
                requestHandsUp()
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mLifecycleProvider.bindToLifecycle())
                .subscribe(new DataCompletableObserver(this) {
                    @Override
                    public void handleError(@NonNull BaseError e) {
                        ToastUtile.toastShort(RoomListActivity.this, e.getMessage());
                        mDataBinding.ivHandUp.setEnabled(true);
                    }

                    @Override
                    public void handleSuccess() {
                        refreshHandUpView();
                        mDataBinding.ivHandUp.setEnabled(true);

                        ToastUtile.toastShort(RoomListActivity.this, R.string.request_handup_success);
                    }
                });
    }

    private void gotoCreateRoom() {
        if (EasyPermissions.hasPermissions(this, PERMISSTION)) {
            new CreateRoomDialog().show(getSupportFragmentManager());
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.error_permisstion),
                    TAG_PERMISSTION_REQUESTCODE, PERMISSTION);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onItemClick(@NonNull Room data, View view, int position, long id) {
        if (!EasyPermissions.hasPermissions(this, PERMISSTION)) {
            EasyPermissions.requestPermissions(this, getString(R.string.error_permisstion),
                    TAG_PERMISSTION_REQUESTCODE, PERMISSTION);
            return;
        }

        Room roomCur = RoomManager.Instance(this).getRoom();
        if (roomCur != null) {
            if (!ObjectsCompat.equals(roomCur, data)) {
                ToastUtile.toastShort(this, "您已经加入了一个房间，请先退出");
                return;
            }
        }

        this.mRoom = data;
        Intent intent = ChatRoomActivity.newIntent(this, data);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

    @Override
    protected void onDestroy() {
        RoomManager.Instance(this).removeRoomEventCallback(this);
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        loadRooms();
    }

    @Override
    public void onOwnerLeaveRoom(@NonNull Room room) {
        mDataBinding.btCrateRoom.setVisibility(View.VISIBLE);
        mDataBinding.llMin.setVisibility(View.GONE);

        mAdapter.deleteItem(room);
        mDataBinding.tvEmpty.setVisibility(mAdapter.getItemCount() <= 0 ? View.VISIBLE : View.GONE);

        if (this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
            ToastUtile.toastShort(this, R.string.room_closed);
        }
    }

    @Override
    public void onLeaveRoom(@NonNull Room room) {
        mDataBinding.btCrateRoom.setVisibility(View.VISIBLE);
        mDataBinding.llMin.setVisibility(View.GONE);
    }

    @Override
    public void onMemberJoin(@NonNull Member member) {
        updateMinRoomInfo();
    }

    @Override
    public void onMemberLeave(@NonNull Member member) {
        updateMinRoomInfo();
    }

    @Override
    public void onRoleChanged(boolean isMine, @NonNull Member member) {
        if (!isMine && isMine(member)) {
            if (member.getIsSpeaker() == 0 && this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                ToastUtile.toastShort(this, R.string.member_speaker_to_listener);
            }
        }

        refreshVoiceView();
        refreshHandUpView();
    }

    @Override
    public void onAudioStatusChanged(boolean isMine, @NonNull Member member) {
        if (!isMine && isMine(member)) {
            if (member.getIsMuted() == 1 && this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                ToastUtile.toastShort(this, R.string.member_muted);
            }
        }

        refreshVoiceView();
    }

    @Override
    public void onReceivedHandUp(@NonNull Member member) {
        mDataBinding.ivNews.setCount(DataRepositroy.Instance(this).getHandUpListCount());
    }

    @Override
    public void onHandUpAgree(@NonNull Member member) {
        refreshHandUpView();
        mDataBinding.ivNews.setCount(DataRepositroy.Instance(this).getHandUpListCount());
    }

    @Override
    public void onHandUpRefuse(@NonNull Member member) {
        refreshHandUpView();
        mDataBinding.ivNews.setCount(DataRepositroy.Instance(this).getHandUpListCount());
    }

    @Override
    public void onReceivedInvite(@NonNull Member member) {

    }

    @Override
    public void onInviteAgree(@NonNull Member member) {

    }

    @Override
    public void onInviteRefuse(@NonNull Member member) {
        if (isOwner() && this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
            ToastUtile.toastShort(this, getString(R.string.invite_refuse, member.getUserId().getName()));
        }
    }

    @Override
    public void onEnterMinStatus() {
        mDataBinding.btCrateRoom.setVisibility(View.GONE);
        mDataBinding.llMin.setVisibility(View.VISIBLE);

        if (RoomManager.Instance(this).isOwner()) {
            mDataBinding.ivNews.setVisibility(View.VISIBLE);
        } else {
            mDataBinding.ivNews.setVisibility(View.GONE);
        }

        refreshVoiceView();
        refreshHandUpView();
        updateMinRoomInfo();
    }

    @Override
    public void onRoomError(int error) {
        if (this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED) == false) {
            return;
        }
    }

    private boolean isMine(Member member) {
        return RoomManager.Instance(this).isMine(member);
    }

    private boolean isOwner() {
        return RoomManager.Instance(this).isOwner();
    }

    private boolean isOwner(Member member) {
        return RoomManager.Instance(this).isOwner(member);
    }

    private void updateMinRoomInfo() {
        Room room = RoomManager.Instance(this).getRoom();
        if (room == null) {
            return;
        }

        List<Member> speakers = new ArrayList<>();
        List<Member> members = RoomManager.Instance(this).getMembers();
        for (Member member : members) {
            if (member.getIsSpeaker() == 1) {
                speakers.add(member);
            }
        }
        room.setSpeakers(speakers);
        room.setMembers(members.size());

        mDataBinding.members.setMemebrs(speakers);
        mDataBinding.tvNumbers.setText(String.format("%s/%s", room.getMembers(), speakers.size()));
    }
}
