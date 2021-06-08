package com.ml.doctor.activity;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ml.doctor.R;
import com.ml.doctor.adapter.PatientDetailsAdapter;
import com.ml.doctor.bean.PatientDetailsBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecordListActivity extends BaseActivity {
    @BindView(R.id.list)
    RecyclerView list;
    private List<PatientDetailsBean> response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);
        ButterKnife.bind(this);
        setTopTitle("历史测量");
        response = getIntent().getParcelableArrayListExtra("data");
        list.setLayoutManager(new LinearLayoutManager(this));
        list.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        list.setAdapter(new PatientDetailsAdapter(R.layout.patient_details_item, response));
    }

    @Override
    protected void onLeftViewClick() {
        finish();
    }
}
