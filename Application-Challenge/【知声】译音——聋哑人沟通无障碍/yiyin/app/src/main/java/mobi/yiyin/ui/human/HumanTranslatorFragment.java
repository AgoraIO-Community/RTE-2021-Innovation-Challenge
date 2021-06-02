package mobi.yiyin.ui.human;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import mobi.yiyin.R;
import mobi.yiyin.utils.Utils;
import mobi.yiyin.view.LinerView;

/**
 * 我的页面
 *
 * @author wlk
 */
public class HumanTranslatorFragment extends Fragment implements View.OnClickListener {

    private final static int EDIT_REQEARCODE = 1009;
    private static final int MODIFYPASSWORD = 1007;
    private TextView tvKf1;
    private TextView tvKf1Status;
    private LinearLayout llContentKf1;
    private TextView tvKf2;
    private TextView tvKf2Status;
    private LinearLayout llContentKf2;
    private TextView tvKf3;
    private TextView tvKf3Status;
    private LinearLayout llContentKf3;
    private TextView tvBdQy1;
    private TextView tvBdQy1Status;
    private LinearLayout llContentBdQy1;
    private TextView tvBdQy2;
    private TextView tvBdQy2Status;
    private LinearLayout llContentBdQy2;

    private LinerView lvCall, lvCallHistory;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_human_translator, container, false);
        initViews(root);
        initListener();
        return root;
    }

    public void initViews(View root) {
        llContentKf1 = root.findViewById(R.id.ll_content_kf_1);
        tvKf1 = root.findViewById(R.id.tv_kf_1);
        tvKf1Status = root.findViewById(R.id.tv_kf_1_status);
        llContentKf2 = root.findViewById(R.id.ll_content_kf_2);
        tvKf2 = root.findViewById(R.id.tv_kf_2);
        tvKf2Status = root.findViewById(R.id.tv_kf_2_status);
        llContentKf3 = root.findViewById(R.id.ll_content_kf_3);
        tvKf3 = root.findViewById(R.id.tv_kf_3);
        tvKf3Status = root.findViewById(R.id.tv_kf_3_status);
        llContentBdQy1 = root.findViewById(R.id.ll_content_bd_qy_1);
        tvBdQy1 = root.findViewById(R.id.tv_bd_qy_1);
        tvBdQy1Status = root.findViewById(R.id.tv_bd_qy_1_status);
        llContentBdQy2 = root.findViewById(R.id.ll_content_bd_qy_2);
        tvBdQy2 = root.findViewById(R.id.tv_bd_qy_2);
        tvBdQy2Status = root.findViewById(R.id.tv_bd_qy_2_status);

        lvCall = root.findViewById(R.id.lv_call);
        lvCallHistory = root.findViewById(R.id.lv_call_history);

        tvBdQy1.setText(String.format(getResources().getString(R.string.text_bd_qy_1), "妈妈"));
        tvBdQy2.setText(String.format(getResources().getString(R.string.text_bd_qy_2), "姐姐"));
        root.findViewById(R.id.lv_people).setOnClickListener(this);

    }


    public void initListener() {
        llContentKf1.setOnClickListener(this);
        llContentKf2.setOnClickListener(this);
        llContentKf3.setOnClickListener(this);
        llContentBdQy1.setOnClickListener(this);
        llContentBdQy2.setOnClickListener(this);
        lvCall.setOnClickListener(this);
        lvCallHistory.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        //TODO 在线状态固定，客服，亲友等人员为固定状态
        switch (view.getId()) {
            //手语客服
            case R.id.ll_content_kf_1:
                startActivity(new Intent(getContext(), VideoChatActivity.class).putExtra("title", "周老师"));
                break;
            //手语客服
            case R.id.ll_content_kf_2:
                break;
            //手语客服
            case R.id.ll_content_kf_3:
                break;
            //绑定亲友
            case R.id.ll_content_bd_qy_1:
                break;
            //绑定亲友
            case R.id.ll_content_bd_qy_2:
                startActivity(new Intent(getContext(), VideoChatActivity.class).putExtra("title", "姐姐"));
                break;
            case R.id.lv_call:
                Utils.yyOpenWXMin(getContext(),"gh_404ed47c22a4","pages/tabdial/tabdial");
                break;
            case R.id.lv_call_history:
                Utils.yyOpenWXMin(getContext(),"gh_404ed47c22a4","pages/tabchats/tabchats");
                break;

            case R.id.lv_people:
                Utils.yyOpenWXMin(getContext(),"gh_8c85706e745f","pages/index/index");//腾讯教育小程序课文片段
                break;
            default:
                break;
        }
    }
}
