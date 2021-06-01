package io.agora.interactivepodcast.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;

import java.util.List;

import io.agora.interactivepodcast.R;
import io.agora.interactivepodcast.databinding.LayoutRoomListMemebrsBinding;
import com.agora.data.model.Member;

/**
 * 房间列表显示成员view
 *
 * @author chenhengfei@agora.io
 */
public class RoomListItemMembersView extends ConstraintLayout {

    protected LayoutRoomListMemebrsBinding mDataBinding;

    public RoomListItemMembersView(@NonNull Context context) {
        super(context);
        init(context, null, 0);
    }

    public RoomListItemMembersView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public RoomListItemMembersView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        mDataBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_room_list_memebrs, this, true);
    }

    public void setMemebrs(List<Member> users) {
        mDataBinding.iv1.setVisibility(GONE);
        mDataBinding.iv2.setVisibility(GONE);
        mDataBinding.iv3.setVisibility(GONE);

        mDataBinding.ivName1.setVisibility(GONE);
        mDataBinding.ivName2.setVisibility(GONE);
        mDataBinding.ivName3.setVisibility(GONE);

        mDataBinding.ivName1.setText("");
        mDataBinding.ivName2.setText("");
        mDataBinding.ivName3.setText("");

        if (users == null) {
            return;
        }

        if (users.size() >= 1) {
            Glide.with(this)
                    .load(users.get(0).getUserId().getAvatarRes())
                    .placeholder(R.mipmap.default_head)
                    .error(R.mipmap.default_head)
                    .circleCrop()
                    .into(mDataBinding.iv1);
            mDataBinding.iv1.setVisibility(VISIBLE);

            mDataBinding.ivName1.setText(users.get(0).getUserId().getName());
            mDataBinding.ivName1.setVisibility(VISIBLE);
        }

        if (users.size() >= 2) {
            Glide.with(this)
                    .load(users.get(1).getUserId().getAvatarRes())
                    .placeholder(R.mipmap.default_head)
                    .error(R.mipmap.default_head)
                    .circleCrop()
                    .into(mDataBinding.iv2);
            mDataBinding.iv2.setVisibility(VISIBLE);

            mDataBinding.ivName2.setText(users.get(1).getUserId().getName());
            mDataBinding.ivName2.setVisibility(VISIBLE);
        }

        if (users.size() >= 3) {
            Glide.with(this)
                    .load(users.get(2).getUserId().getAvatarRes())
                    .placeholder(R.mipmap.default_head)
                    .error(R.mipmap.default_head)
                    .circleCrop()
                    .into(mDataBinding.iv3);
            mDataBinding.iv3.setVisibility(VISIBLE);

            mDataBinding.ivName3.setText(users.get(2).getUserId().getName());
            mDataBinding.ivName3.setVisibility(VISIBLE);
        }
    }
}
