package m_diary.controls;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.VideoView;

public class AlbumVideoView extends VideoView implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private static final String TAG = "AlbumVideoView";
    private static final float MAX_SCALE = 4;
    private static final float MIN_SCALE = 1;
    private GestureDetector mGestureDetector;
    private boolean isHaveScrolled;
    private boolean isHaveScale;
    private float oldTwoPointerDistance;

    public PointF currentPosition = null;//当前的位置
    private PointF moveStartPosition = new PointF(0, 0);//手指触摸起点坐标
    private PointF moveEndPosition = new PointF(0, 0);//当前手指位置坐标
    private boolean isMove = false;
    public int width; //视频宽度
    public int height; //视频高度


    public AlbumVideoView(Context context) {
        super(context);
        mGestureDetector = new GestureDetector(getContext(), this);
        currentPosition = new PointF(300, 300);
    }
    public AlbumVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        Log.d(getClass().toString(), "onTouch: ");
        super.onTouchEvent(event);


        if (event.getPointerCount() == 1) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    isMove = false;
                    moveStartPosition.x = event.getX();
                    moveStartPosition.y = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    isMove = true;
                    moveEndPosition.x = event.getX();
                    moveEndPosition.y = event.getY();
                    //刷新
                    this.postInvalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    if (isMove) {
                        if(currentPosition == null){ currentPosition = new PointF(getLeft(), getTop()); }
                        currentPosition.x += (moveEndPosition.x - moveStartPosition.x);
                        currentPosition.y += (moveEndPosition.y - moveStartPosition.y);
                        moveStartPosition.x = moveEndPosition.x;
                        moveStartPosition.y = moveEndPosition.y;
                        setLocation(currentPosition.x, currentPosition.y);
                    }
                    break;
                default:
            }
            return mGestureDetector.onTouchEvent(event);
        } else if (event.getPointerCount() == 2) {
            return onScaleEvent(event);
        }
        return false;
    }

    private boolean onScaleEvent(MotionEvent event) {
        if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_DOWN) {
            oldTwoPointerDistance = spacing(event);//两点按下时的距离
//            Log.d(TAG, "onScaleEvent: ACTION_POINTER_DOWN");
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                oldTwoPointerDistance = spacing(event);//两点按下时的距离
//                Log.d(TAG, "onScaleEvent: ACTION_DOWN");
                return true;
            case MotionEvent.ACTION_MOVE:
                //只有2个手指的时候才有放大缩小的操作
                float currentDist = spacing(event);
//                Log.d(TAG, "onScaleEvent: currentDist = " + currentDist + " oldTwoPointerDistance = " + oldTwoPointerDistance);
                float scale = currentDist / oldTwoPointerDistance;
                setScale(scale);
                return true;
        }
        return false;
    }

    private float spacing(MotionEvent event) {
        if (event.getPointerCount() == 2) {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return (float) Math.sqrt(x * x + y * y);
        } else {
            return 0;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
//        setMeasuredDimension(width, height);
//        // 默认高度，为了自动获取到focus
//        int width = MeasureSpec.getSize(widthMeasureSpec);
//        int height = width;
        // 之前是默认的拉伸图像
        if ((this.width > 0) && (this.height > 0)) {
            width = this.width;
            height = this.height;
        }
        setMeasuredDimension(width, height);
    }

    public void setFixedVideoSize(int width, int height) {
        getHolder().setFixedSize(width, height);
    }

    /**
     * 触摸使用的移动事件
     *
     * @param lessX
     * @param lessY
     */
    private void setSelfPivot(float lessX, float lessY) {
        float setPivotX = 0;
        float setPivotY = 0;
        setPivotX = getPivotX() + lessX;
        setPivotY = getPivotY() + lessY;
//        Log.e("lawwingLog", "setPivotX:" + setPivotX + "  setPivotY:" + setPivotY
//                + "  getWidth:" + getWidth() + "  getHeight:" + getHeight());
        if (setPivotX < 0 && setPivotY < 0) {
            setPivotX = 0;
            setPivotY = 0;
        } else if (setPivotX > 0 && setPivotY < 0) {
            setPivotY = 0;
            if (setPivotX > getWidth()) {
                setPivotX = getWidth();
            }
        } else if (setPivotX < 0 && setPivotY > 0) {
            setPivotX = 0;
            if (setPivotY > getHeight()) {
                setPivotY = getHeight();
            }
        } else {
            if (setPivotX > getWidth()) {
                setPivotX = getWidth();
            }
            if (setPivotY > getHeight()) {
                setPivotY = getHeight();
            }
        }
        setPivot(setPivotX, setPivotY);
    }

    /**
     * 平移画面，当画面的宽或高大于屏幕宽高时，调用此方法进行平移
     *
     * @param x
     * @param y
     */
    public void setPivot(float x, float y) {
        setPivotX(x);
        setPivotY(y);
    }

    /**
     * 设置放大缩小
     *
     * @param scale
     */
    public void setScale(float scale) {
//        Log.d(TAG, "setScale: scale = " + scale + " getScaleX() * scale = " + getScaleX() * scale);
        float currentScaleX = getScaleX() * scale;
        float currentScaleY = getScaleY() * scale;
        if (currentScaleX > MAX_SCALE || currentScaleY > MAX_SCALE) {
            currentScaleX = MAX_SCALE;
            currentScaleY = MAX_SCALE;
        } else if (currentScaleX < MIN_SCALE || currentScaleY < MIN_SCALE) {
            currentScaleX = MIN_SCALE;
            currentScaleY = MIN_SCALE;
        }
        setScaleX(currentScaleX);
        setScaleY(currentScaleY);
    }

    /**
     * 移动位置
     **/
    public void setLocation(float x, float y) {
        setX(x);
        setY(y);
    }

    /**
     * 设置大小
     **/
    public void setMeasure(int width, int height) {
        this.width = width;
        this.height = height;
    }


    @Override
    public boolean onDown(MotionEvent e) {
        isHaveScrolled = false;
        isHaveScale = false;
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        isHaveScrolled = true;
        setSelfPivot(e1.getX() - e2.getX(), e1.getY() - e2.getY());
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
//        Log.d(TAG, "onDoubleTap: ");
        if (getScaleX() > 1) {
            startScaleAnimation(getScaleX(), 1);
        } else {
            startScaleAnimation(getScaleX(), 2);
        }
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    private void startScaleAnimation(float start, float end) {
        ValueAnimator anim = ValueAnimator.ofFloat(start, end);
        anim.setDuration(200);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (float) animation.getAnimatedValue();
                setScaleX(currentValue);
                setScaleY(currentValue);
//                Log.d("TAG", "cuurent value is " + currentValue);
            }
        });
        anim.start();
    }

}


