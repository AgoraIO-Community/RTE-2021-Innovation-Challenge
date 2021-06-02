package io.agora.interactivepodcast.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;

import io.agora.interactivepodcast.R;
import io.agora.interactivepodcast.databinding.LayoutMenuTextBinding;

/**
 * 菜单显示veiw
 *
 * @author chenhengfei@agora.io
 */
public class MenuTextView extends ConstraintLayout {

    protected LayoutMenuTextBinding mDataBinding;

    public MenuTextView(@NonNull Context context) {
        super(context);
        init(context, null, 0);
    }

    public MenuTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public MenuTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        mDataBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_menu_text, this, true);

        if (attrs == null) {
            return;
        }

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.menu_textiew);
        setTitle(typedArray.getString(R.styleable.menu_textiew_menu_title));
        setValue(typedArray.getString(R.styleable.menu_textiew_menu_value));
        setTitleColor(typedArray.getColor(R.styleable.menu_textiew_menu_title_color, Color.WHITE));
        setValueColor(typedArray.getColor(R.styleable.menu_textiew_menu_value_color, Color.WHITE));
        showNextIcon(typedArray.getBoolean(R.styleable.menu_textiew_menu_show_next, true));
        typedArray.recycle();
    }

    public void setTitle(@StringRes int resId) {
        mDataBinding.tvTitle.setText(resId);
    }

    public void setTitle(String value) {
        mDataBinding.tvTitle.setText(value);
    }

    public void setValue(@StringRes int resId) {
        mDataBinding.tvValue.setText(resId);
    }

    public void setValue(String value) {
        mDataBinding.tvValue.setText(value);
    }

    public void setValueColor(@ColorInt int color) {
        mDataBinding.tvValue.setTextColor(color);
    }

    public void setTitleColor(@ColorInt int color) {
        mDataBinding.tvTitle.setTextColor(color);
    }

    public void showNextIcon(boolean isShown) {
        if (isShown) {
            mDataBinding.ivNext.setVisibility(View.VISIBLE);
        } else {
            mDataBinding.ivNext.setVisibility(View.GONE);
        }
    }
}
