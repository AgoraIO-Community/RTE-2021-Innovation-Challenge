package com.qgmodel.qggame.utils;

import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.qgmodel.qggame.entity.UserStatusData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class VideoViewAdapterUtil {
    private static final String TAG = "VideoViewAdapterUtil**";

    public static void stripView(View view){
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent!=null){
            Log.d(TAG, "=== stripView: ");
            ((RelativeLayout)parent).removeView(view);
        }
    }

    public static void composeDataItem1(ArrayList<UserStatusData> users, HashMap<Integer, SurfaceView> uids, int localUid){
        for (HashMap.Entry<Integer,SurfaceView> entry : uids.entrySet()){
            SurfaceView surfaceView = entry.getValue();
            surfaceView.setZOrderOnTop(false);
            surfaceView.setZOrderMediaOverlay(false);
            searchUidsAndAppend(users,entry,localUid);
        }
        removeNotExisted(users,uids,localUid);
    }

    public static void searchUidsAndAppend(ArrayList<UserStatusData> users,HashMap.Entry<Integer,SurfaceView> entry,int localUid){
        if (entry.getKey() == 0||entry.getKey() == localUid){
            boolean found = false;
            for (UserStatusData user : users){
                if ((user.mUid == entry.getKey() && user.mUid == 0)||user.mUid == localUid){
                    user.mUid = localUid;
                    found = true;
                    break;
                }
            }
            if (!found){
                users.add(0,new UserStatusData(localUid,entry.getValue()));
            }
        }else {
            boolean found = false;
            for (UserStatusData user : users){
                if (user.mUid == entry.getKey()){
                    found = true;
                    break;
                }
            }
            if (!found){
                users.add(new UserStatusData(entry.getKey(),entry.getValue()));
            }
        }
    }

    private static void removeNotExisted(ArrayList<UserStatusData> users, HashMap<Integer, SurfaceView> uids, int localUid) {

        Iterator<UserStatusData> it = users.iterator();
        while (it.hasNext()) {
            UserStatusData user = it.next();
            if (uids.get(user.mUid) == null && user.mUid != localUid) {
                //add
                Log.d(TAG, "=== 移除退出的视图");
                it.remove();
            }
        }
    }


}
