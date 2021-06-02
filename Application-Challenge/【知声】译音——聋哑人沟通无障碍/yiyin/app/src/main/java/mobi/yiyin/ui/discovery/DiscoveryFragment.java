package mobi.yiyin.ui.discovery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import mobi.yiyin.R;
import mobi.yiyin.utils.Utils;


public class DiscoveryFragment extends Fragment  implements View.OnClickListener {



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View discovery = inflater.inflate(R.layout.fragment_discovery, container, false);
        initView(discovery);

        return discovery;
    }

    private void initView(View discovery) {
        discovery.findViewById(R.id.lv_community).setOnClickListener(this);
        discovery.findViewById(R.id.lv_oral_testing).setOnClickListener(this);
        discovery.findViewById(R.id.lv_learn_chinese).setOnClickListener(this);
        discovery.findViewById(R.id.lv_learn_lesson).setOnClickListener(this);
        discovery.findViewById(R.id.lv_learn_video).setOnClickListener(this);
        discovery.findViewById(R.id.lv_learn_english).setOnClickListener(this);
        discovery.findViewById(R.id.lv_shop).setOnClickListener(this);
        discovery.findViewById(R.id.lv_translate).setOnClickListener(this);

        discovery.findViewById(R.id.lv_news).setOnClickListener(this);
        discovery.findViewById(R.id.lv_gmae).setOnClickListener(this);
        discovery.findViewById(R.id.lv_sign_learn).setOnClickListener(this);
        discovery.findViewById(R.id.lv_sign_word).setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.lv_community:
                Utils.yyOpenWXMin(getContext(),"gh_4815110163ee","pages/home/index");//启明圈小程序
                break;
            case R.id.lv_news:
                Utils.yyOpenWXMin(getContext(),"gh_88de1dd6a39a","pages/index/index");
                break;
            case R.id.lv_sign_learn: //柚得手语词典小程序
                Utils.yyOpenWXMin(getContext(),"gh_3ffd30d03bb1","sign_language/pages/index/index");
                break;
            case R.id.lv_sign_word: //手语词库小程序
                Utils.yyOpenWXMin(getContext(),"gh_1ed772e5f70e","pages/index/index");
                break;
            case R.id.lv_oral_testing:
                Utils.yyOpenWXMin(getContext(),"gh_5497ef21c1f1","pages/list/index");//中文派智聆口语评测
                break;
            case R.id.lv_learn_chinese:
                Utils.yyOpenWXMin(getContext(),"gh_2b11e3f2676e","pages/chinese-package/shizi/main");//腾讯教育小程序文字识别功能
                break;
            case R.id.lv_learn_lesson:
                Utils.yyOpenWXMin(getContext(),"gh_2b11e3f2676e","pages/chinese-package/lesson/main");//腾讯教育小程序课文片段
                break;
            case R.id.lv_learn_english:
                Utils.yyOpenWXMin(getContext(),"gh_2b11e3f2676e","pages/english-package/words/main");//腾讯教育小程序课文片段
                break;
            case R.id.lv_learn_video:
                Utils.yyOpenWXMin(getContext(),"gh_2b11e3f2676e","pages/english-package/videos/main");//腾讯教育小程序视频片段
                break;
            case R.id.lv_shop:
                Utils.yyOpenWXMin(getContext(),"gh_9fd8141f411f","pages/index/index");//腾讯教育小程序课文片段
                break;
            case R.id.lv_gmae:
                Utils.yyOpenWXMin(getContext(),"gh_eacd07af7420","pages/index/index");//腾讯教育小程序课文片段
                break;
            case R.id.lv_translate:
                Utils.yyOpenWXMin(getContext(),"gh_0e4ed2286545","pages/index/index"); //腾讯翻译君
                break;
            default:
                break;

        }


    }
}