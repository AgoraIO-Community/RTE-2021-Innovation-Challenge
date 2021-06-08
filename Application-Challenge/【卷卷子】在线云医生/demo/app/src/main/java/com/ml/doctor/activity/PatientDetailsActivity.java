package com.ml.doctor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ml.doctor.R;
import com.ml.doctor.bean.PatientDetailsBean;
import com.ml.doctor.bean.PatientListBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by gzq on 2017/11/22.
 * 患者详情页
 */

public class PatientDetailsActivity extends BaseActivity {
    private static String TAG = "PatientDetailsActivity";
    @BindView(R.id.head)
    CircleImageView head;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.height)
    TextView height;
    @BindView(R.id.weight)
    TextView weight;
    @BindView(R.id.blood)
    TextView blood;
    @BindView(R.id.ll1)
    LinearLayout ll1;
    @BindView(R.id.line1)
    View line1;
    @BindView(R.id.line2)
    View line2;
    @BindView(R.id.eating)
    TextView eating_t;
    @BindView(R.id.ll_hatis1)
    LinearLayout llHatis1;
    @BindView(R.id.smoke)
    TextView smoke;
    @BindView(R.id.ll_hatis2)
    LinearLayout llHatis2;
    @BindView(R.id.drinking)
    TextView drinking_t;
    @BindView(R.id.ll_hatis3)
    LinearLayout llHatis3;
    @BindView(R.id.sports)
    TextView sports;
    @BindView(R.id.ll_hatis4)
    LinearLayout llHatis4;
    @BindView(R.id.line3)
    View line3;
    @BindView(R.id.line4)
    View line4;
    @BindView(R.id.maibo)
    TextView maibo;
    @BindView(R.id.xinlv)
    TextView xinlv;
    @BindView(R.id.ll2)
    LinearLayout ll2;
    @BindView(R.id.line5)
    View line5;
    @BindView(R.id.xuetang)
    TextView xuetang;
    @BindView(R.id.xueyang)
    TextView xueyang;
    @BindView(R.id.ll3)
    LinearLayout ll3;
    @BindView(R.id.line6)
    View line6;
    @BindView(R.id.gaoya)
    TextView gaoya;
    @BindView(R.id.diya)
    TextView diya;
    @BindView(R.id.ll4)
    LinearLayout ll4;
    @BindView(R.id.line7)
    View line7;
    @BindView(R.id.tiwen)
    TextView tiwen;
    @BindView(R.id.more)
    TextView more;
    @BindView(R.id.ll5)
    LinearLayout ll5;


    private int limit = 10;
    private int start_index = 0, end_index = 9;
    private PatientListBean patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        patient = getIntent().getParcelableExtra("data");
        setHat();
        setData();
        getData();
    }

    private void setData() {
        height.setText("患者身高：" + patient.getHeight() + "cm");
        weight.setText("患者体重：" + patient.getWeight() + "Kg");

        Glide.with(this)
                .load(patient.getUser_photo())
                .error(R.color.textColor_undertint)
                .into(head);
        name.setText(patient.getBname());
        height.setText(patient.getHeight() + "cm");
        weight.setText(patient.getWeight() + "Kg");
        blood.setText(patient.getBlood_type() + "型");
        String eating = "", smoking = "", drinking = "", motioning = "";
        if (!TextUtils.isEmpty(patient.getEating_habits()))
            switch (patient.getEating_habits()) {

                case "1":
                    eating = "荤素搭配";
                    break;
                case "2":
                    eating = "偏好吃荤";
                    break;
                case "3":
                    eating = "偏好吃素";
                    break;
                case "4":
                    eating = "偏好吃咸";
                    break;
                case "5":
                    eating = "偏好油腻";
                    break;
                case "6":
                    eating = "偏好甜食";
                    break;
            }

        eating_t.setText(eating);

        if (!TextUtils.isEmpty(patient.getSmoke()))
            switch (patient.getSmoke()) {
                case "1":
                    smoking = "经常抽烟";
                    break;
                case "2":
                    smoking = "偶尔抽烟";
                    break;
                case "3":
                    smoking = "从不抽烟";
                    break;
            }
        smoke.setText(smoking);
        if (!TextUtils.isEmpty(patient.getDrink()))
            switch (patient.getDrink()) {
                case "1":
                    drinking = "经常喝酒";
                    break;
                case "2":
                    drinking = "偶尔喝酒";
                    break;
                case "3":
                    drinking = "从不喝酒";
                    break;
            }
        drinking_t.setText(drinking);
        if (!TextUtils.isEmpty(patient.getExercise_habits()))
            switch (patient.getExercise_habits()) {
                case "1":
                    motioning = "每天一次";
                    break;
                case "2":
                    motioning = "每周几次";
                    break;
                case "3":
                    motioning = "偶尔运动";
                    break;
                case "4":
                    motioning = "从不运动";
                    break;
            }
        sports.setText(motioning);
    }


    private void getData() {
        showLoadingDialog();
//        NetworkApi.patientDetails(patient.getBid(), start_index, end_index,
//                new NetworkManager.SuccessCallback<List<PatientDetailsBean>>() {
//                    @Override
//                    public void onSuccess(List<PatientDetailsBean> response) {
//                        Log.e(TAG, response.toString());
//                        setAdapter(response);
//                        hideLoadingDialog();
//                    }
//                }, new NetworkManager.FailedCallback() {
//                    @Override
//                    public void onFailed(String message) {
//                        ToastUtil.showShort(PatientDetailsActivity.this, message);
//                        maibo.setText(getString(R.string.noData));
//                        xinlv.setText(getString(R.string.noData));
//                        xuetang.setText(getString(R.string.noData));
//                        xueyang.setText(getString(R.string.noData));
//                        gaoya.setText(getString(R.string.noData));
//                        diya.setText(getString(R.string.noData));
//                        tiwen.setText(getString(R.string.noData));
//                        hideLoadingDialog();
//                    }
//                });
        maibo.setText(getString(R.string.noData));
        xinlv.setText(getString(R.string.noData));
        xuetang.setText(getString(R.string.noData));
        xueyang.setText(getString(R.string.noData));
        gaoya.setText(getString(R.string.noData));
        diya.setText(getString(R.string.noData));
        tiwen.setText(getString(R.string.noData));
        hideLoadingDialog();
    }

    private void setAdapter(final List<PatientDetailsBean> response) {
        if ("0".equals(response.get(0).getHigh_pressure())) {
            maibo.setText(getString(R.string.noData));
        } else {
            maibo.setText(response.get(0).getHigh_pressure() + "bpm");
        }
        if ("0".equals(response.get(0).getHeart_rate())) {
            xinlv.setText(getString(R.string.noData));
        } else {
            xinlv.setText(response.get(0).getHeart_rate() + "bpm");
        }
        if ("0".equals(response.get(0).getBlood_sugar())) {
            xuetang.setText(getString(R.string.noData));
        } else {
            xuetang.setText(response.get(0).getBlood_sugar() + "mmol/L");
        }
        if ("0".equals(response.get(0).getBlood_oxygen())) {
            xueyang.setText(getString(R.string.noData));
        } else {
            xueyang.setText(response.get(0).getBlood_oxygen() + "%");
        }
        if ("0".equals(response.get(0).getHigh_pressure())) {
            gaoya.setText(getString(R.string.noData));
        } else {
            gaoya.setText(response.get(0).getHigh_pressure() + "mmhg");
        }
        if ("0".equals(response.get(0).getLow_pressure())) {
            diya.setText(getString(R.string.noData));
        } else {
            diya.setText(response.get(0).getLow_pressure() + "mmhg");
        }
        if ("0".equals(response.get(0).getTemper_ature())) {
            tiwen.setText(getString(R.string.noData));
        } else {
            tiwen.setText(response.get(0).getTemper_ature() + "℃");
        }


        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PatientDetailsActivity.this, RecordListActivity.class)
                        .putParcelableArrayListExtra("data", (ArrayList<? extends Parcelable>) response));
            }
        });
    }

    private void setHat() {
        setTopTitle("患者使用详情");
        setRightView(R.drawable.ic_call);
    }

    @Override
    protected void onLeftViewClick() {
        finish();
    }

    @Override
    protected void onRightViewClick() {
        //呼叫
//        NimAccountHelper.getInstance().login("docter_" + CustomApplication.getInstance().userId, "123456", null);
//        NimCallActivity.launch(this, "user_" + patient.getBid());
        Intent intent = new Intent(this, MessageActivity.class);
        startActivity(intent);
    }
}
