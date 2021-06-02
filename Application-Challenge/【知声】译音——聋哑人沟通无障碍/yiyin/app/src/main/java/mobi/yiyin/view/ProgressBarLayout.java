package mobi.yiyin.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import mobi.yiyin.R;


/**
 * @author wlk
 */
public class ProgressBarLayout extends RelativeLayout {
	public ProgressBarLayout(Context context) {
		super(context);
		addProgressView(context);
	}

	public ProgressBarLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		addProgressView(context);
	}
	private void addProgressView(Context context){
		View view = View.inflate(context, R.layout.progress_bar_layout, null);
		addView(view);
	}
	public void show(){
		setVisibility(VISIBLE);
	}
	public void hide(){
		setVisibility(GONE);
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return true;
	}
}
