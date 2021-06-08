package com.ml.doctor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.ml.doctor.R;
import com.ml.doctor.adapter.PatientListAdapter;
import com.ml.doctor.bean.PatientListBean;
import com.ml.doctor.network.NetworkApi;
import com.ml.doctor.network.NetworkManager;
import com.ml.doctor.utils.LocalShared;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {


    @BindView(R.id.list)
    RecyclerView list;
    private int limit=500;
    private int start_index=0,end_index=500;
    private static String TAG="MainActivity";
    private PatientListAdapter adapter;
    private List<PatientListBean> mData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setHat();
        getData(null);
        setClick();
    }

    private void setClick() {

    }

    private void setHat() {
        setTopTitle("患者列表");

    }

    @Override
    protected void onLeftViewClick() {
        finish();
    }

    //请求网络数据
    private void getData(String bname) {
//        showLoadingDialog();
//        NetworkApi.patientList(LocalShared.getInstance(this).getUserId(),bname, start_index, end_index,
//                new NetworkManager.SuccessCallback<List<PatientListBean>>() {
//            @Override
//            public void onSuccess(List<PatientListBean> response) {
//                start_index+=limit;
//                end_index+=limit;
//                Log.e(TAG,response.toString());
//                mData=response;
//                setAdapter(response);
//                hideLoadingDialog();
//            }
//        }, new NetworkManager.FailedCallback() {
//            @Override
//            public void onFailed(String message) {
//                Log.e(TAG,message);
//                hideLoadingDialog();
//            }
//        });
//
//        start_index+=limit;
//        end_index+=limit;
//        Log.e(TAG,response.toString());
        String s = "{\"exercise_habits\":\"1\",\"sfz\":\"sfz\",\"sex\":\"男\",\"eqid\":\"eqid\",\"smoke\":\"1\",\"doid\":\"doid\",\"weight\":\"70\",\"drink\":\"1\",\"xfid\":\"xfid\",\"bname\":\"病人1\",\"dz\":\"dz\",\"blood_type\":\"A\",\"tel\":\"tel\",\"mh\":\"mh\",\"state\":\"state\",\"eating_habits\":\"1\",\"bid\":\"bid\",\"user_photo\":\"user_photo\",\"age\":\"22\",\"categoryid\":\"categoryid\",\"height\":\"180\"}\n";
        List<PatientListBean> response = Arrays.asList(new Gson().fromJson(s, PatientListBean.class));
        mData=response;
        setAdapter(response);
        hideLoadingDialog();

    }

    private void setAdapter(List<PatientListBean> response) {
        list.setLayoutManager(new LinearLayoutManager(this));
//        list.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        list.setAdapter(adapter=new PatientListAdapter(R.layout.patient_item,response));

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //跳转到患者详情页
                startActivity(new Intent(MainActivity.this,PatientDetailsActivity.class)
                        .putExtra("data",mData.get(position)));
            }
        });
    }

    @Override
    protected void hideLeftImg(ImageView mLeftImage) {
        mLeftImage.setVisibility(View.GONE);
    }
}
