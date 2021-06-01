package com.qgmodel.qggame.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qgmodel.qggame.R;
import com.qgmodel.qggame.custom.MyRelativeLayout;

public class VideoUserStatusHolder extends RecyclerView.ViewHolder {
    public VideoUserStatusHolder(@NonNull View itemView) {
        super(itemView);
        //add
        countContainer = (MyRelativeLayout) itemView.findViewById(R.id.card_num_container);
        //add
        cardView = itemView.findViewById(R.id.card_container);

        catImage = itemView.findViewById(R.id.image_cat);

        forbidImage = itemView.findViewById(R.id.image_forbid);

        kickImage = itemView.findViewById(R.id.image_kick);

        readyImage = itemView.findViewById(R.id.image_have_ready);

    }

    //add
    public MyRelativeLayout countContainer;
    //add
    public MyRelativeLayout cardView;

    public ImageView catImage;

    public ImageView forbidImage;

    public ImageView kickImage;

    public ImageView readyImage;




}
