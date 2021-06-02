/**
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

 package mobi.yiyin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mobi.yiyin.R;

public class TtsStyleAdapter extends RecyclerView.Adapter<TtsStyleAdapter.LanguageViewHolder> {
    private final Context context;
    private final List<String> list;

    public TtsStyleAdapter(FragmentActivity ttsAudioActivity, List<String> styleList) {
        this.context = ttsAudioActivity;
        this.list = styleList;
    }


    @NonNull
    @Override
    public LanguageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.tts_style_item, parent, false);
        LanguageViewHolder languageViewHolder = new LanguageViewHolder(inflate);
        return languageViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageViewHolder holder, final int position) {

        holder.ttsStyle.setText(list.get(position));

        if (null != onItemClickListener) {
            // Callback click event
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Callback
                    onItemClickListener.setOnStyleItemClick(v, position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public interface OnItemClickListener {
        // Entry click event
        void setOnStyleItemClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class LanguageViewHolder extends RecyclerView.ViewHolder {

        private final TextView ttsStyle;

        public LanguageViewHolder(@NonNull View itemView) {
            super(itemView);
            ttsStyle = itemView.findViewById(R.id.tts_style);
        }
    }
}
