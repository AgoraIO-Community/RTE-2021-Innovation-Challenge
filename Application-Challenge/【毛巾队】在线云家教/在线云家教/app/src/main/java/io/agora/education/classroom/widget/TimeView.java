package io.agora.education.classroom.widget;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;

import androidx.appcompat.widget.AppCompatTextView;

import io.agora.education.R;
import io.agora.education.util.TimeUtil;

public class TimeView extends AppCompatTextView {

    private Handler handler;
    private long time = 0;
    private Runnable updateTimeRunnable = () -> {
        setText(TimeUtil.stringForTimeHMS(time, "%02d:%02d:%02d"));
        time++;
        handler.postDelayed(this.updateTimeRunnable, 1000);
    };
    private boolean isStarted = false;

    public TimeView(Context context) {
        this(context, null);
    }

    public TimeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        handler = new Handler();
        setGravity(Gravity.CENTER);
        setText(R.string.time_default);
        Resources resources = getResources();
        setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.sp_12));
        setTextColor(resources.getColor(R.color.gray_333333));
        setCompoundDrawablesRelativeWithIntrinsicBounds(resources.getDrawable(R.drawable.ic_time), null, null, null);
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time / 1000;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void start() {
        time = 0;
        handler.removeCallbacks(updateTimeRunnable);
        isStarted = true;
        updateTimeRunnable.run();
    }

    public void stop() {
        if (handler != null) {
            handler.removeCallbacks(updateTimeRunnable);
        }
        isStarted = false;
    }

}
