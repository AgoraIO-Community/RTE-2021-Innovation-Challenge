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
import io.agora.interactivepodcast.databinding.LayoutRoomListMemebrs2Binding;
import com.agora.data.model.Member;

/**
 * 房间列表最小化显示成员view
 *
 * @author chenhengfei@agora.io
 */
public class RoomListMinMembersView extends ConstraintLayout {

    protected LayoutRoomListMemebrs2Binding mDataBinding;

    public RoomListMinMembersView(@NonNull Context context) {
        super(context);
        init(context, null, 0);
    }

    public RoomListMinMembersView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public RoomListMinMembersView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        mDataBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_room_list_memebrs2, this, true);
    }

    public void setMemebrs(List<Member> users) {
        mDataBinding.iv1.setVisibility(GONE);
        mDataBinding.iv2.setVisibility(GONE);
        mDataBinding.iv3.setVisibility(GONE);

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
        }

        if (users.size() >= 2) {
            Glide.with(this)
                    .load(users.get(1).getUserId().getAvatarRes())
                    .placeholder(R.mipmap.default_head)
                    .error(R.mipmap.default_head)
                    .circleCrop()
                    .into(mDataBinding.iv2);
            mDataBinding.iv2.setVisibility(VISIBLE);
        }

        if (users.size() >= 3) {
            Glide.with(this)
                    .load(users.get(2).getUserId().getAvatarRes())
                    .placeholder(R.mipmap.default_head)
                    .error(R.mipmap.default_head)
                    .circleCrop()
                    .into(mDataBinding.iv3);
            mDataBinding.iv3.setVisibility(VISIBLE);
        }
    }
}
