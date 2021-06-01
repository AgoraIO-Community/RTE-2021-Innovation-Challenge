package com.qgmodel.qggame.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qgmodel.qggame.R;
import com.qgmodel.qggame.custom.MyRelativeLayout;
import com.qgmodel.qggame.entity.UserStatusData;
import com.qgmodel.qggame.holder.VideoUserStatusHolder;
import com.qgmodel.qggame.utils.VideoViewAdapterUtil;

import org.intellij.lang.annotations.JdkConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class VideoViewAdapter extends RecyclerView.Adapter {
    private static final String TAG = "VideoViewAdapter";

    ArrayList<UserStatusData> mUsers = new ArrayList<>();
    int mLocalUid = 0;
    public final LayoutInflater inflater;
    public final Context context;

    protected int mItemWidth;
    protected int mItemHeight;
    private int mDefaultChildItem = 0;

    //add
    private List<MyRelativeLayout> relativeLayouts = new ArrayList<>();
    private List<MyRelativeLayout> wordLayouts = new ArrayList<>();
    private List<ImageView> cats = new ArrayList<>();
    private List<ImageView> forbids = new ArrayList<>();
    private List<ImageView> kicks = new ArrayList<>();
    private List<ImageView> readys = new ArrayList<>();


    public VideoViewAdapter(Activity activity, int localUid, HashMap<Integer, SurfaceView> uids){
        inflater = activity.getLayoutInflater();
        context = activity.getApplicationContext();
        mLocalUid = localUid;
        init(uids);
    }

    private void init(HashMap<Integer, SurfaceView> uids) {
        mUsers.clear();
        customizedInit(uids,true);
    }

    protected abstract void customizedInit(HashMap<Integer, SurfaceView> uids,boolean force);

    public void setmLocalUid(int localUid){
        mLocalUid = localUid;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.video_view_container,parent,false);
        viewGroup.getLayoutParams().width = mItemWidth;
        viewGroup.getLayoutParams().height = mItemHeight;

        mDefaultChildItem = viewGroup.getChildCount();


        return  new VideoUserStatusHolder(viewGroup);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        VideoUserStatusHolder myHolder = (VideoUserStatusHolder) holder;

        UserStatusData user = mUsers.get(position);

        try {
            //add
            MyRelativeLayout numLayout = myHolder.countContainer;
            setCardNumView(numLayout,position);


            //add
            MyRelativeLayout cardLayout = myHolder.cardView;
            setWordLayouts(cardLayout,position);


            ImageView cat = myHolder.catImage;
            setCats(cat,position);

            ImageView forbid = myHolder.forbidImage;
            setForbid(forbid,position);

            ImageView kick = myHolder.kickImage;


            setKick(kick,position);

            ImageView ready = myHolder.readyImage;
            setReady(ready,position);

        }catch (Exception e){
            Log.d(TAG, "onBind exception --> "+e);
        }

        RelativeLayout holderView = (RelativeLayout) myHolder.itemView;



        if (holderView.getChildCount() == mDefaultChildItem){
            SurfaceView target = user.mView;
            VideoViewAdapterUtil.stripView(target);
            holderView.addView(target,0,new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }

    }

    private void setReady(ImageView ready, int position) {
        if (readys.size() == 0 &&position == 0){
            ready.setVisibility(View.INVISIBLE);
            readys.add(ready);
        }else if (position == kicks.size()){
            readys.add(ready);
        }
    }

    private void setKick(ImageView kick, int position) {
        if (kicks.size() == 0 &&position == 0){
            kick.setVisibility(View.INVISIBLE);
            kicks.add(kick);
        }else if (position == kicks.size()){
            kicks.add(kick);
        }
    }

    private void setForbid(ImageView forbid, int position) {
        if (forbids.size() == 0 &&position == 0){
            forbid.setVisibility(View.INVISIBLE);
            forbids.add(forbid);
            Log.d(TAG, "=== uuuuu postion--> "+position);
        }else if (position == forbids.size()){
            forbids.add(forbid);
            Log.d(TAG, "=== uuuuu postion--> "+position);
        }
    }


    private void setCats(ImageView cat, int position) {
        if (cats.size()==0 && position == 0){
            cats.add(cat);
        }else if (position ==cats.size()){
            cats.add(cat);
        }
    }


    protected void setWordLayouts(MyRelativeLayout cardLayout, int position){
        if (wordLayouts.size()==0 && position ==0){
            TextView textView = cardLayout.findViewById(R.id.text_word);
            textView.setText("自己的");
            wordLayouts.add(cardLayout);
        }else if (position==wordLayouts.size()){
            TextView textView = cardLayout.findViewById(R.id.text_word);
            textView.setText("别人的");
            wordLayouts.add(cardLayout);
        }

    }



    public void setCardNumView(MyRelativeLayout cardNumLayout,int position){
        if (relativeLayouts.size() == 0&&position == 0){
            TextView numText = cardNumLayout.findViewById(R.id.card_num_text);
            numText.setText(String.valueOf(10));
            relativeLayouts.add(cardNumLayout);
        }else if (position == relativeLayouts.size()){
            TextView numText = cardNumLayout.findViewById(R.id.card_num_text);
            numText.setText(String.valueOf(10));
            relativeLayouts.add(cardNumLayout);
        }
    }



    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    @Override
    public long getItemId(int position) {
        UserStatusData user = mUsers.get(position);

        SurfaceView view = user.mView;

        return (String.valueOf(user.mUid) + System.identityHashCode(view)).hashCode();
    }

    public ArrayList<UserStatusData> getmUsers() {
        return mUsers;
    }

    public List<MyRelativeLayout> getRelativeLayouts() {
        return relativeLayouts;
    }

    public void clearCountContainers(){
        if (relativeLayouts!=null){
            relativeLayouts.clear();
        }

    }

    public List<MyRelativeLayout> getWordLayouts() {
        return wordLayouts;
    }

    public void clearWordLayouts(){
        if (wordLayouts!=null){
            wordLayouts.clear();
        }
    }

    public List<ImageView> getCats() {
        return cats;
    }

    public void clearCats() {
        cats.clear();
    }

    public List<ImageView> getForbids() {
        return forbids;
    }

    public void clearForbids() {
        forbids.clear();
    }

    public List<ImageView> getKicks() {
        return kicks;
    }

    public void clearKicks() {
        kicks.clear();
    }

    public List<ImageView> getReadys() {
        return readys;
    }

    public void clearReadys() {
        readys.clear();
    }


}
