package com.qgmodel.qggame.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qgmodel.qggame.entity.UserStatusData;
import com.qgmodel.qggame.rtmtutorial.AGApplication;
import com.qgmodel.qggame.utils.VideoViewAdapterUtil;

import java.util.HashMap;

public class GridVideoViewContainerAdapter extends VideoViewAdapter {

    private static final String TAG = "GridVideoViewContainerA";
    private int actionBarHeight = 0;
    private int navigationHeight = 0;

    public GridVideoViewContainerAdapter(Activity activity, int localUid, HashMap<Integer, SurfaceView> uids) {
        super(activity, localUid, uids);
    }

    @Override
    public void customizedInit(HashMap<Integer, SurfaceView> uids,boolean force) {
        VideoViewAdapterUtil.composeDataItem1(mUsers,uids,mLocalUid);
        if (force || mItemHeight == 0|| mItemWidth == 0){
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics metrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(metrics);

            if (AGApplication.getContext()!=null){
                actionBarHeight = dpTopx(AGApplication.getContext(),56);
            }


            int width = metrics.widthPixels;
            int height = metrics.heightPixels - actionBarHeight;
            int count = uids.size();

            int divideX = 1;
            int divideY = 1;

            if (count == 2){
                divideY = 2;
            }else if (count>=3){
                divideX = getNearestSqrt(count);
                divideY = (int) Math.ceil(count*1f/divideX);
            }

            if (height>width){
                mItemHeight = height/divideY;
                mItemWidth = width/divideX;
            }else{
                mItemHeight = height/divideX;
                mItemWidth = width/divideY;
            }


        }
    }


    public int dpTopx(Context context,int dpVaule){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (scale*dpVaule);
    }

    private int getNearestSqrt(int count){
        return (int) Math.sqrt(count);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public long getItemId(int position) {
        UserStatusData user = mUsers.get(position);

        SurfaceView view = user.mView;

        return (String.valueOf(user.mUid) + System.identityHashCode(view)).hashCode();
    }

    public UserStatusData getItem(int position) {
        return mUsers.get(position);
    }

    public static int getNavigationBarHeightIfRoom(Context context) {
        if(navigationGestureEnabled(context)){
            return 0;
        }
        return getCurrentNavigationBarHeight(((Activity) context));
    }


    public static boolean navigationGestureEnabled(Context context) {
        int val = Settings.Global.getInt(context.getContentResolver(), getDeviceInfo(), 0);
        return val != 0;
    }

    public static String getDeviceInfo() {
        String brand = Build.BRAND;
        if(TextUtils.isEmpty(brand)) return "navigationbar_is_min";

        if (brand.equalsIgnoreCase("HUAWEI")) {
            return "navigationbar_is_min";
        } else if (brand.equalsIgnoreCase("XIAOMI")) {
            return "force_fsg_nav_bar";
        } else if (brand.equalsIgnoreCase("VIVO")) {
            return "navigation_gesture_on";
        } else if (brand.equalsIgnoreCase("OPPO")) {
            return "navigation_gesture_on";
        } else {
            return "navigationbar_is_min";
        }
    }

    public static int getCurrentNavigationBarHeight(Activity activity){
        if(isNavigationBarShown(activity)){
            return getNavigationBarHeight(activity);
        } else{
            return 0;
        }
    }

    public static boolean isNavigationBarShown(Activity activity){
        //虚拟键的view,为空或者不可见时是隐藏状态
        View view  = activity.findViewById(android.R.id.navigationBarBackground);
        if(view == null){
            return false;
        }
        int visible = view.getVisibility();
        if(visible == View.GONE || visible == View.INVISIBLE){
            return false ;
        }else{
            return true;
        }
    }

    public static int getNavigationBarHeight(Context context){
        int result = 0;
        int resourceId = context.getResources().getIdentifier("navigation_bar_height","dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


}
