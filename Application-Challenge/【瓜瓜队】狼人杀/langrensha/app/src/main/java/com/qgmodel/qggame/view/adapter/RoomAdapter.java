package com.qgmodel.qggame.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.qgmodel.qggame.R;
import com.qgmodel.qggame.entity.RoomInfo;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


/**
 * Created by HeYanLe on 2020/9/4 0004 10:48.
 * https://github.com/heyanLE
 */
public class RoomAdapter extends BaseQuickAdapter<RoomInfo, BaseViewHolder> {

    public RoomAdapter(@Nullable List<RoomInfo> data) {
        super(R.layout.item_room_layout, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, RoomInfo roomInfo) {
        baseViewHolder.setText(R.id.title, roomInfo.getTitle());
        baseViewHolder.setText(R.id.roomer, "房主："+roomInfo.getRoomerId());
        baseViewHolder.setText(R.id.player, roomInfo.getPlayer()+"/"+RoomInfo.MAX_PLAYER);
    }
}
