package io.agora.openlive.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import io.agora.openlive.entitymodel.Has;
import io.agora.openlive.entitymodel.Point;

public class WritingBrushDrawUtil extends View {

    private Bitmap cacheBitmap = null;
    private Canvas customCanvas = null;
    private Paint mPaint = null;

    private static final float TOUCH_TOLERANCE = 4;

    //#############################################
    private int lineMax = 20;
    private int lineMin = 1;
    private double linePressure = 8;
    private int smoothness = 80;
    private boolean moveFlag = false;

    private Point upof;
    private double radius = 0;
    private List<Has> has = new ArrayList<>();

    private double x;
    private double y;
    private double r;

    private int drawWidth = 0;
    private int drawHeight = 0;
    private float displacement = 0;

    public WritingBrushDrawUtil(Context context) {
        super(context);
    }

    public WritingBrushDrawUtil(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WritingBrushDrawUtil(Context context, int width , int height) {
        super(context);
        drawWidth = width;
        drawHeight = height;
        displacement = (drawHeight - drawWidth) / 2;
        cacheBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        initDrawUtil();
    }

    private void initDrawUtil() {
        customCanvas = new Canvas();
        customCanvas.setBitmap(cacheBitmap);
        mPaint = new Paint(Paint.DITHER_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(1);
        mPaint.setColor(Color.argb(200, 0, 0 , 0));
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch(event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                moveFlag = true;
//                has.clear();
//                upof = new Point(event.getX(), event.getY() + displacement);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if(!moveFlag)
//                    break;
//                Point of = new Point(event.getX(), event.getY() + displacement);
//                Point up = upof;// 坐标集合
//                double ur = radius;
//                has.add(new Has(System.currentTimeMillis(), distance(up, of)));
//                double dis = 0;
//                long time = 0;
//                for(int i = has.size() - 1; i > 0; i--){
//                    dis += has.get(i).getDis();
//                    time += has.get(i).getTime() - has.get(i - 1).getTime();
//                    if(dis > smoothness)
//                        break;
//                }
//                double or = Math.min(time / dis * linePressure + lineMin, lineMax) / 2;
//                radius = or;
//                upof = of;
//                if(has.size() <= 4)
//                    break;
//                long len = Math.round(has.get(has.size() - 1).getDis() / 2) + 1;
//                for(long n = 0; n < len; n++){
//                    x = up.getX() + (of.getX() - up.getX()) / len * n;
//                    y = up.getY() + (of.getY() - up.getY()) / len * n;
//                    r = ur + (or - ur) / len * n;
//                    invalidate();
//                    mPaint.setStrokeWidth((float)(r));
//                    System.out.println("--->" + displacement);
//                    customCanvas.drawCircle((float)x, (float)y + displacement, (float)r, mPaint);// 小圆
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                moveFlag = false;
//                break;
//        }
//        return true;
//    }

    public void startDraw(Map<String, Object> data){
        double pointX = Double.parseDouble(data.get("x").toString()) * drawWidth / 640;
        double pointY = Double.parseDouble(data.get("y").toString()) * drawWidth / 640;
        switch(Integer.parseInt(data.get("p").toString())){
            case 0:
                moveFlag = true;
                has.clear();
                upof = new Point(pointX, pointY);
                break;
            case 1:
                if(!moveFlag)
                    break;
                Point of = new Point(pointX, pointY);
                Point up = upof;
                double ur = radius;
                has.add(new Has(System.currentTimeMillis(), distance(up, of)));
                double dis = 0;
                long time = 0;
                for(int i = has.size() - 1; i > 0; i--){
                    dis += has.get(i).getDis();
                    time += has.get(i).getTime() - has.get(i - 1).getTime();
                    if(dis > smoothness)
                        break;
                }
                double or = Math.min(time / dis * linePressure + lineMin, lineMax) / 2;
                radius = or;
                upof = of;
                if(has.size() <= 4)
                    break;
                long len = Math.round(has.get(has.size() - 1).getDis() / 2) + 1;
                for(long n = 0; n < len; n++){
                    x = up.getX() + (of.getX() - up.getX()) / len * n;
                    y = up.getY() + (of.getY() - up.getY()) / len * n;
                    r = ur + (or - ur) / len * n;
                    invalidate();
                    mPaint.setStrokeWidth((float)(r));
                    customCanvas.drawCircle((float)x, (float)y + displacement, (float)r, mPaint);// 小圆
                }
                break;
            case 2:
                moveFlag = false;
                break;
        }
    }

    public void clearDraw(){
        customCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    private double distance(Point a, Point b) {
        double x = b.getX()-a.getX();
        double y = b.getY()-a.getY();
        return Math.sqrt(x * x + y * y);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(cacheBitmap, 0, 0, mPaint);
    }
}
