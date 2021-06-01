package io.agora.interactivepodcast.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.agora.data.model.Member;
import com.bumptech.glide.Glide;

import java.util.List;

import io.agora.baselibrary.base.BaseRecyclerViewAdapter;
import io.agora.interactivepodcast.R;
import io.agora.interactivepodcast.databinding.ItemHandsupListBinding;

/**
 * 举手列表
 *
 * @author chenhengfei@agora.io
 */
public class HandsUpListAdapter extends BaseRecyclerViewAdapter<Member, HandsUpListAdapter.ViewHolder> {

    public HandsUpListAdapter(@Nullable List<Member> datas, @Nullable Object listener) {
        super(datas, listener);
    }

    @Override
    public ViewHolder createHolder(View view, int viewType) {
        return new ViewHolder(view);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_handsup_list;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Member item = getItemData(position);
        if (item == null) {
            return;
        }

        Glide.with(holder.itemView.getContext())
                .load(item.getUserId().getAvatarRes())
                .placeholder(R.mipmap.default_head)
                .error(R.mipmap.default_head)
                .circleCrop()
                .into(holder.mDataBinding.ivUser);
        holder.mDataBinding.tvName.setText(item.getUserId().getName());
    }

    static class ViewHolder extends BaseRecyclerViewAdapter.BaseViewHolder<ItemHandsupListBinding> {

        ViewHolder(View view) {
            super(view);

            mDataBinding.btRefuse.setOnClickListener(this::onItemClick);
            mDataBinding.btAgree.setOnClickListener(this::onItemClick);
        }
    }
}
