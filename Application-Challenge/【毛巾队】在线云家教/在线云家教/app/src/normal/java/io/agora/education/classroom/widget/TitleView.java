package io.agora.education.classroom.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.agora.education.R;
import io.agora.education.api.statistics.NetworkQuality;
import io.agora.education.classroom.BaseClassActivity;
import io.agora.rtc.Constants;

import static io.agora.education.api.statistics.NetworkQuality.*;

public class TitleView extends ConstraintLayout {

    @Nullable
    @BindView(R.id.iv_quality)
    protected ImageView iv_quality;
    @BindView(R.id.tv_room_name)
    protected TextView tv_room_name;
    @Nullable
    @BindView(R.id.time_view)
    protected TimeView time_view;

    public TitleView(Context context) {
        this(context, null);
    }

    public TitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        ButterKnife.bind(this);
    }

    private void init() {
        int layoutResId;
        Configuration configuration = getResources().getConfiguration();
        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutResId = R.layout.layout_title_portrait;
        } else {
            layoutResId = R.layout.layout_title_landscape;
        }
        LayoutInflater.from(getContext()).inflate(layoutResId, this, true);
    }

    public void hideTime() {
        if (time_view != null) {
            time_view.setVisibility(GONE);
        }
    }

    public void setTitle(String title) {
        ((Activity) getContext()).runOnUiThread(() -> tv_room_name.setText(title));
    }

    public void setNetworkQuality(NetworkQuality quality) {
        ((Activity) getContext()).runOnUiThread(() -> {
            if (iv_quality != null) {
                switch (quality) {
                    case GOOD:
                        iv_quality.setImageResource(R.drawable.ic_signal_good);
                        break;
                    case BAD:
                        iv_quality.setImageResource(R.drawable.ic_signal_bad);
                        break;
                    case POOR:
                    case UNKNOWN:
                    default:
                        iv_quality.setImageResource(R.drawable.ic_signal_normal);
                        break;
                }
            }
        });
    }

    public void setTimeState(boolean start, long time) {
        ((Activity) getContext()).runOnUiThread(() -> {
            if (time_view != null) {
                if (start) {
                    if (!time_view.isStarted()) {
                        time_view.start();
                    }
//                    time_view.setTime(time);
                } else {
                    time_view.stop();
                }
                time_view.setTime(time);
            }
        });
    }

    @OnClick({R.id.iv_close, R.id.iv_uploadLog})
    public void onClock(View view) {
        Context context = getContext();
        if (context instanceof BaseClassActivity) {
            switch (view.getId()) {
                case R.id.iv_close:
                    ((BaseClassActivity) context).showLeaveDialog();
                    break;
                case R.id.iv_uploadLog:
                    ((BaseClassActivity) context).uploadLog();
                    break;
                default:
                    break;
            }
        }
    }

}
