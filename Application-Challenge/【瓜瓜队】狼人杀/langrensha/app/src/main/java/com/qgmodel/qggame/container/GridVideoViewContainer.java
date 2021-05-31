package com.qgmodel.qggame.container;


import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qgmodel.qggame.custom.MyRelativeLayout;
import com.qgmodel.qggame.entity.UserStatusData;
import com.qgmodel.qggame.view.adapter.GridVideoViewContainerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GridVideoViewContainer extends RecyclerView {
    private static final String TAG = "GridVideoViewContainer";

    private GridVideoViewContainerAdapter gridVideoViewContainerAdapter;


    public GridVideoViewContainer(@NonNull Context context) {
        this(context,null);
    }

    public GridVideoViewContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GridVideoViewContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean initAdapter(Activity activity, int localUid, HashMap<Integer, SurfaceView> uids){
        if (gridVideoViewContainerAdapter == null){
            gridVideoViewContainerAdapter = new GridVideoViewContainerAdapter(activity,localUid,uids);
            gridVideoViewContainerAdapter.setHasStableIds(true);
            return true;
        }
        return false;

    }

    public void initGridVideoViewContainer(Activity activity,int localUid,HashMap<Integer,SurfaceView> uids){
        try {
            boolean created =  initAdapter(activity,localUid,uids);
            if (!created){
                gridVideoViewContainerAdapter.setmLocalUid(localUid);
                gridVideoViewContainerAdapter.customizedInit(uids,true);
            }
            this.setAdapter(gridVideoViewContainerAdapter);
            int count = uids.size();


            if (count<=2){
                this.setLayoutManager(new LinearLayoutManager(activity.getApplicationContext(), RecyclerView.VERTICAL,false){
                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }
                });
            }else if (count>2){
                int itemSpanCount = getNearestSqrt(count);
                this.setLayoutManager(new GridLayoutManager(activity.getApplicationContext(),itemSpanCount, RecyclerView.VERTICAL,false){
                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }
                });
            }

            gridVideoViewContainerAdapter.notifyDataSetChanged();
        }catch (Exception e){
            Log.d(TAG, "initGridVideoViewContainer: exception --> "+e);
        }




    }

    private int getNearestSqrt(int n) {
        return (int) Math.sqrt(n);
    }

    public UserStatusData getItem(int position) {
        return gridVideoViewContainerAdapter.getItem(position);
    }

    public List<MyRelativeLayout> getCountContainers(){
        return  gridVideoViewContainerAdapter.getRelativeLayouts();
    }

    public void clearCountContainers(){
        gridVideoViewContainerAdapter.clearCountContainers();
    }

    public List<MyRelativeLayout> getWordLayouts(){
        return gridVideoViewContainerAdapter.getWordLayouts();
    }

    public void clearWords(){
        gridVideoViewContainerAdapter.clearWordLayouts();
    }

    public ArrayList<UserStatusData> getUsers(){
        return  gridVideoViewContainerAdapter.getmUsers();
    }

    public int getItemNum(){
        return gridVideoViewContainerAdapter.getItemCount();
    }

    public List<ImageView> getCats(){
        return gridVideoViewContainerAdapter.getCats();
    }

    public void clearCats(){
        gridVideoViewContainerAdapter.clearCats();
    }

    public List<ImageView> getForbids(){
        return gridVideoViewContainerAdapter.getForbids();
    }

    public void clearForbids(){
        gridVideoViewContainerAdapter.clearForbids();
    }

    public List<ImageView> getKicks(){
        return gridVideoViewContainerAdapter.getKicks();
    }

    public void clearKicks(){
        gridVideoViewContainerAdapter.clearKicks();
    }

    public List<ImageView> getReadys(){
        return gridVideoViewContainerAdapter.getReadys();
    }

    public void clearReadys(){
        gridVideoViewContainerAdapter.clearReadys();
    }


}
