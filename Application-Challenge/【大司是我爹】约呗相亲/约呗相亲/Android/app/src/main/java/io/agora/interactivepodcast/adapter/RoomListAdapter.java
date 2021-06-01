package io.agora.interactivepodcast.adapter;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.agora.data.BaseError;
import com.agora.data.DataRepositroy;
import com.agora.data.model.Member;
import com.agora.data.model.Room;
import com.agora.data.observer.DataMaybeObserver;
import com.agora.data.observer.DataObserver;

import java.util.List;

import io.agora.baselibrary.base.BaseRecyclerViewAdapter;
import io.agora.interactivepodcast.R;
import io.agora.interactivepodcast.databinding.ItemRoomsBinding;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 房间列表
 *
 * @author chenhengfei@agora.io
 */
public class RoomListAdapter extends BaseRecyclerViewAdapter<Room, RoomListAdapter.ViewHolder> {

    public RoomListAdapter(@Nullable List<Room> datas, @Nullable Object listener) {
        super(datas, listener);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_rooms;
    }

    @Override
    public ViewHolder createHolder(View view, int viewType) {
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Room item = getItemData(position);
        if (item == null) {
            return;
        }

        holder.mDataBinding.tvName.setText(item.getChannelName());

        List<Member> speakers = item.getSpeakers();
        holder.mDataBinding.members.setMemebrs(speakers);
        holder.mDataBinding.tvNumbers.setText(String.format("%s/%s", item.getMembers(), speakers == null ? 0 : speakers.size()));

        Context context = holder.itemView.getContext();
        DataRepositroy.Instance(context)
                .getRoomCountInfo(item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DataObserver<Room>(context) {
                    @Override
                    public void handleError(@NonNull BaseError e) {

                    }

                    @Override
                    public void handleSuccess(@NonNull Room room) {
                        List<Member> speakers = room.getSpeakers();
                        holder.mDataBinding.tvNumbers.setText(String.format("%s/%s", room.getMembers(), speakers == null ? 0 : speakers.size()));
                    }
                });

        DataRepositroy.Instance(context)
                .getRoomSpeakersInfo(item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DataMaybeObserver<Room>(context) {
                    @Override
                    public void handleError(@NonNull BaseError e) {

                    }

                    @Override
                    public void handleSuccess(@Nullable Room room) {
                        if (room != null) {
                            holder.mDataBinding.members.setMemebrs(room.getSpeakers());
                        }
                    }
                });
    }

    class ViewHolder extends BaseRecyclerViewAdapter.BaseViewHolder<ItemRoomsBinding> {

        public ViewHolder(View view) {
            super(view);
        }
    }
}
