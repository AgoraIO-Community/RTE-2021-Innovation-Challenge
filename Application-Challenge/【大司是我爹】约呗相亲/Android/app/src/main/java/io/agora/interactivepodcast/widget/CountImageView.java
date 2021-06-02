package io.agora.interactivepodcast.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;

import io.agora.interactivepodcast.R;
import io.agora.interactivepodcast.databinding.LayoutCountImageviewBinding;

/**
 * 带消息显示ImagerView
 *
 * @author chenhengfei@agora.io
 */
public class CountImageView extends ConstraintLayout {

    protected LayoutCountImageviewBinding mDataBinding;

    private int count = 0;

    public CountImageView(@NonNull Context context) {
        super(context);
        init(context, null, 0);
    }

    public CountImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CountImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        mDataBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_count_imageview, this, true);
        if (attrs == null) {
            return;
        }

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.count_iamageview);
        setCount(typedArray.getInteger(R.styleable.count_iamageview_iv_count, 0));

        Drawable d = typedArray.getDrawable(R.styleable.count_iamageview_iv_src);
        if (d != null) {
            setImageResource(d);
        }
        typedArray.recycle();
    }

    public void addCount() {
        count = count + 1;

        mDataBinding.tvCount.setText(String.valueOf(count));
        mDataBinding.tvCount.setVisibility(View.VISIBLE);
    }

    public void setCount(int count) {
        this.count = count;
        mDataBinding.tvCount.setText(String.valueOf(count));
        if (count <= 0) {
            mDataBinding.tvCount.setVisibility(View.GONE);
        } else {
            mDataBinding.tvCount.setVisibility(View.VISIBLE);
        }
    }

    public void setImageResource(@DrawableRes int resId) {
        mDataBinding.iv.setImageResource(resId);
    }

    public void setImageResource(@Nullable Drawable drawable) {
        mDataBinding.iv.setImageDrawable(drawable);
    }
}
