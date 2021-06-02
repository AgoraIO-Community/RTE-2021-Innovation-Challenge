package com.ml.doctor.call2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;


import com.ml.doctor.R;
import com.ml.doctor.utils.T;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DoctorSignInActivity extends AppCompatActivity {

    @BindView(R.id.tv_id)
    TextView mTvId;
    @BindView(R.id.et_id)
    EditText mEtId;
    @BindView(R.id.tv_action)
    TextView mTvAction;
    public Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_sign_in);
        mUnbinder = ButterKnife.bind(this);
        mTvAction.setText("登录");
    }

    @OnClick(R.id.tv_action)
    public void onActionClicked() {
        String action = mTvAction.getText().toString();
        final String id = mEtId.getText().toString().trim();
        if ("登录".equals(action)) {
            mTvAction.setEnabled(false);
            mTvAction.setText("登录中...");
            return;
        }

        if ("呼叫".equals(action)
                && !TextUtils.isEmpty(id)) {
        }
    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }
}
