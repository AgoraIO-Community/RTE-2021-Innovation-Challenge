package com.example.jialichun.client.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jialichun.client.R;
import com.example.jialichun.client.model.ChatModel;
import com.example.jialichun.client.model.ItemModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by WangChang on 2016/4/28.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.BaseAdapter> {

    private ArrayList<ItemModel> dataList = new ArrayList<>();

    public void replaceAll(ArrayList<ItemModel> list) {
        dataList.clear();
        if (list != null && list.size() > 0) {
            dataList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<ItemModel> list) {
        if (dataList != null && list != null) {
            dataList.addAll(list);
            notifyItemRangeChanged(dataList.size(),list.size());
        }

    }

    @Override
    public ChatAdapter.BaseAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ItemModel.CHAT_A:
                return new ChatAViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_a, parent, false));
            case ItemModel.CHAT_B:
                return new ChatBViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_b, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ChatAdapter.BaseAdapter holder, int position) {
        holder.setData(dataList.get(position).object);
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).type;
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    public class BaseAdapter extends RecyclerView.ViewHolder {

        public BaseAdapter(View itemView) {
            super(itemView);
        }

        void setData(Object object) {

        }
    }

    private class ChatAViewHolder extends BaseAdapter {
        private ImageView ic_user;
        private TextView tv;

        public ChatAViewHolder(View view) {
            super(view);
            ic_user = (CircleImageView) itemView.findViewById(R.id.ic_user);
            tv = (TextView) itemView.findViewById(R.id.tv);
        }

        @Override
        void setData(Object object) {
            super.setData(object);
            ChatModel model = (ChatModel) object;
            //Picasso.with(itemView.getContext()).load(model.getIcon()).placeholder(R.mipmap.ic_launcher).into(ic_user);
            ic_user.setImageResource(model.getIcon());
            tv.setText(model.getContent());
        }
    }

    private class ChatBViewHolder extends BaseAdapter {
        private ImageView ic_user;
        private TextView tv;

        public ChatBViewHolder(View view) {
            super(view);
            ic_user = (CircleImageView) itemView.findViewById(R.id.ic_user);
            tv = (TextView) itemView.findViewById(R.id.tv);

        }
        public synchronized Drawable StringToDrawable(String icon) {
            if (icon == null || icon.length() < 10)
                return null;
            byte[] img = Base64.decode(icon.getBytes(), Base64.DEFAULT);
            Bitmap bitmap;
            if (img != null) {
                bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
                @SuppressWarnings("deprecation")
                Drawable drawable = new BitmapDrawable(bitmap);

                return drawable;
            }
            return null;
        }

        @Override
        void setData(Object object) {
            super.setData(object);
            ChatModel model = (ChatModel) object;
            //Picasso.with(itemView.getContext()).load(model.getIcon()).placeholder(R.mipmap.ic_launcher).into(ic_user);
            ic_user.setImageResource(model.getIcon());
            tv.setText(model.getContent());
        }
    }
}
