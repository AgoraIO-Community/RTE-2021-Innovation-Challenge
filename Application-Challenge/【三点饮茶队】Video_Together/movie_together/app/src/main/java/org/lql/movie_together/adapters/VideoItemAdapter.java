package org.lql.movie_together.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.lql.movie_together.R;
import org.lql.movie_together.model.UserStatusData;
import org.lql.movie_together.model.VideoInfoData;
import org.lql.movie_together.ui.layout.ItemTouchHelperAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.zip.Inflater;

public class VideoItemAdapter extends RecyclerView.Adapter<VideoItemAdapter.VideoItemViewholder> implements ItemTouchHelperAdapter {

    Context mContext;
    int mLocalId;
    ArrayList<UserStatusData> statusDataList;
    HashMap<Integer, SurfaceView> mUidsList;
    protected HashMap<Integer, VideoInfoData> mVideoInfo;

    public VideoItemAdapter(Context context, int localId, HashMap<Integer, SurfaceView> uidsList) {
        this.mContext = context;
        this.mLocalId = localId;
        this.statusDataList = new ArrayList<>();
        this.mUidsList = uidsList;
        composeData(statusDataList, mUidsList, mLocalId);
    }

    @NonNull
    @Override
    public VideoItemAdapter.VideoItemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_video_item, parent, false);
        return new VideoItemViewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoItemViewholder holder, int position) {
        final UserStatusData user = statusDataList.get(position);
        FrameLayout holderView = holder.fm;
        SurfaceView target = user.mView;
        ViewParent parent = target.getParent();
        if (parent != null) {
            ((FrameLayout) parent).removeView(target);
        }
        holderView.addView(target, 0, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void notifyUiChanged(int localId, HashMap<Integer, SurfaceView> uidsList) {
        composeData(statusDataList, uidsList, localId);
        notifyDataSetChanged();
    }

    public void notifyUiChanged(HashMap<Integer, SurfaceView> uids, int localUid, HashMap<Integer, Integer> status, HashMap<Integer, Integer> volume) {
        mLocalId = localUid;
        composeDataItem(statusDataList, uids, localUid, status, volume, mVideoInfo);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return statusDataList.size();
    }

    public UserStatusData getItem(int position) {
        return statusDataList.get(position);
    }

    public long getItemId(int position) {
        UserStatusData user = statusDataList.get(position);

        SurfaceView view = user.mView;
        if (view == null) {
            throw new NullPointerException("SurfaceView destroyed for user " + (user.mUid & 0xFFFFFFFFL) + " " + user.mStatus + " " + user.mVolume);
        }
        return (String.valueOf(user.mUid) + System.identityHashCode(view)).hashCode();
    }

    public void addVideoInfo(int uid, VideoInfoData video) {
        if (mVideoInfo == null) {
            mVideoInfo = new HashMap<>();
        }
        mVideoInfo.put(uid, video);
    }

    public void cleanVideoInfo() {
        mVideoInfo = null;
    }

    public void setLocalUid(int uid) {
        mLocalId = uid;
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(statusDataList,fromPosition,toPosition);
        notifyItemMoved(fromPosition,toPosition);
    }

    @Override
    public void onItemDissmiss(int position) {
        notifyItemRemoved(position);
    }

    class VideoItemViewholder extends RecyclerView.ViewHolder{

        FrameLayout fm;

        public VideoItemViewholder(@NonNull View itemView) {
            super(itemView);
            fm = itemView.findViewById(R.id.item_video_container);
        }
    }

    // 数据整合
    private void composeData(ArrayList<UserStatusData> users, HashMap<Integer, SurfaceView> uids, int localUid) {
        for (HashMap.Entry<Integer, SurfaceView> entry : uids.entrySet()) {
            SurfaceView surfaceV = entry.getValue();
            surfaceV.setZOrderOnTop(false);
            surfaceV.setZOrderMediaOverlay(false);
            searchUidsAndAppend(users, entry, localUid, UserStatusData.DEFAULT_STATUS, UserStatusData.DEFAULT_VOLUME, null);
        }
        removeNotExisted(users,uids,localUid);
    }

    private static void searchUidsAndAppend(ArrayList<UserStatusData> users, HashMap.Entry<Integer, SurfaceView> entry,
                                            int localUid, Integer status, int volume, VideoInfoData i) {
        // 用户自己
        if (entry.getKey() == 0 || entry.getKey() == localUid) {
            boolean found = false;
            for (UserStatusData user : users) {
                if ((user.mUid == entry.getKey() && user.mUid == 0) || user.mUid == localUid) { // first time
                    user.mUid = localUid;
                    if (status != null) {
                        user.mStatus = status;
                    }
                    user.mVolume = volume;
                    user.setVideoInfo(i);
                    found = true;
                    break;
                }
            }
            if (!found) {
                users.add(0, new UserStatusData(localUid, entry.getValue(), status, volume, i));
            }
        } else {
            boolean found = false;
            for (UserStatusData user : users) {
                if (user.mUid == entry.getKey()) {
                    if (status != null) {
                        user.mStatus = status;
                    }
                    user.mVolume = volume;
                    user.setVideoInfo(i);
                    found = true;
                    break;
                }
            }
            if (!found) {
                users.add(new UserStatusData(entry.getKey(), entry.getValue(), status, volume, i));
            }
        }
    }

    public void composeDataItem(final ArrayList<UserStatusData> users, HashMap<Integer, SurfaceView> uids,
                                       int localUid,
                                       HashMap<Integer, Integer> status,
                                       HashMap<Integer, Integer> volume,
                                       HashMap<Integer, VideoInfoData> video) {
        composeDataItem(users, uids, localUid, status, volume, video, 0);
    }

    public void composeDataItem(final ArrayList<UserStatusData> users, HashMap<Integer, SurfaceView> uids,
                                       int localUid,
                                       HashMap<Integer, Integer> status,
                                       HashMap<Integer, Integer> volume,
                                       HashMap<Integer, VideoInfoData> video, int uidExcepted) {
        for (HashMap.Entry<Integer, SurfaceView> entry : uids.entrySet()) {
            int uid = entry.getKey();

            if (uid == uidExcepted && uidExcepted != 0) {
                continue;
            }

            boolean local = uid == 0 || uid == localUid;

            Integer s = null;
            if (status != null) {
                s = status.get(uid);
                if (local && s == null) { // check again
                    s = status.get(uid == 0 ? localUid : 0);
                }
            }
            Integer v = null;
            if (volume != null) {
                v = volume.get(uid);
                if (local && v == null) { // check again
                    v = volume.get(uid == 0 ? localUid : 0);
                }
            }
            if (v == null) {
                v = UserStatusData.DEFAULT_VOLUME;
            }
            VideoInfoData i;
            if (video != null) {
                i = video.get(uid);
                if (local && i == null) { // check again
                    i = video.get(uid == 0 ? localUid : 0);
                }
            } else {
                i = null;
            }
            searchUidsAndAppend(users, entry, localUid, s, v, i);
        }

        removeNotExisted(users, uids, localUid);
    }

    private void removeNotExisted(ArrayList<UserStatusData> users, HashMap<Integer, SurfaceView> uids, int localUid) {
        Iterator<UserStatusData> it = users.iterator();
        while (it.hasNext()) {
            UserStatusData user = it.next();
            if (uids.get(user.mUid) == null && user.mUid != localUid) {
                it.remove();
            }
        }
    }
}
