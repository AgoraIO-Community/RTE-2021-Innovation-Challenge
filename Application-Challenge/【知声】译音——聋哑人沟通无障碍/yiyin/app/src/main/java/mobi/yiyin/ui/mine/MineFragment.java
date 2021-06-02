package mobi.yiyin.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import mobi.yiyin.R;
import mobi.yiyin.utils.Utils;

/**
 * 我的页面
 *
 * @author wlk
 */
public class MineFragment extends Fragment implements View.OnClickListener {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mine, container, false);
        initView(root);
        return root;
    }

    public void initView(View root) {
        root.findViewById(R.id.lv_ksss).setOnClickListener(this);
        root.findViewById(R.id.lv_bzxx).setOnClickListener(this);
        root.findViewById(R.id.lv_ctwkfwxy).setOnClickListener(this);
        root.findViewById(R.id.lv_about_us).setOnClickListener(this);
        root.findViewById(R.id.lv_faceback).setOnClickListener(this);
        root.findViewById(R.id.logout).setOnClickListener(this);
        root.findViewById(R.id.lv_hard).setOnClickListener(this);
        root.findViewById(R.id.logout).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.lv_ksss: //畅听王卡快速上手
                Utils.yyOpenWXMin(getContext(),"gh_404ed47c22a4","pages/easystarts/easystarts");
                break;
            case R.id.lv_bzxx://畅听王卡帮助中心
                Utils.yyOpenWXMin(getContext(),"gh_404ed47c22a4","pages/fqalist/fqalist");
                break;
            case R.id.lv_ctwkfwxy: //畅听王卡服务协议
                Utils.yyOpenWXMin(getContext(),"gh_404ed47c22a4","pages/agreements/agreements");
                break;
            //助听设备
            case R.id.lv_hard:
                Toast.makeText(getContext(), "基于辅助功能助听设备需要购买相关配套硬件设备，后期会在译音小商店上线，想内测可以先联系客服QQ：1135309416", Toast.LENGTH_LONG).show();
                break;
            //反馈
            case R.id.lv_faceback:
                Toast.makeText(getContext(), "请联系客服，客服QQ：1135309416", Toast.LENGTH_LONG).show();
                break;
            //关于
            case R.id.lv_about_us:
                Toast.makeText(getContext(), "请联系客服，客服QQ：1135309416", Toast.LENGTH_LONG).show();
                break;
            //注销 登录
            case R.id.logout:
                Toast.makeText(getContext(), "已经退出", Toast.LENGTH_LONG).show();

                break;

            default:
                break;
        }
    }
}
