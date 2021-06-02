package com.ml.doctor.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ml.doctor.R;
import com.ml.doctor.bean.PatientDetailsBean;

import java.util.List;

/**
 * Created by gzq on 2017/11/22.
 */

public class PatientDetailsAdapter extends BaseQuickAdapter<PatientDetailsBean,BaseViewHolder>{
    public PatientDetailsAdapter(int layoutResId, @Nullable List<PatientDetailsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PatientDetailsBean item) {
        helper.setText(R.id.case_number,"病例单号："+item.getZid());
        helper.setText(R.id.time,"测量时间："+item.getTime());
        helper.setText(R.id.temperature,"体温："+item.getTemper_ature());
        helper.setText(R.id.heart_rate,"心率："+item.getHeart_rate());
        helper.setText(R.id.blood_sugar,"血糖："+item.getBlood_sugar());
        helper.setText(R.id.oxygen,"血氧："+item.getBlood_oxygen());
        helper.setText(R.id.systolic_pressure,"收缩压："+item.getHigh_pressure());
        helper.setText(R.id.diastolic_pressure,"舒张压："+item.getLow_pressure());
        helper.setText(R.id.pulse,"脉搏："+item.getHigh_pressure());
    }
}
