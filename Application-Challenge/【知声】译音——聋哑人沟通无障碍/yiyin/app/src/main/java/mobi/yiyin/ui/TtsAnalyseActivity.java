package mobi.yiyin.ui;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.IdRes;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import mobi.yiyin.R;
import mobi.yiyin.base.BaseActivity;
import mobi.yiyin.ui.mode.OfflineModeFragment;
import mobi.yiyin.ui.mode.OnlineModeFragment;


public class TtsAnalyseActivity extends BaseActivity {
    private RadioGroup mRadioGroup;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tts_analyse);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("语音合成");
        mRadioGroup = findViewById(R.id.radioGroup);
        mViewPager = findViewById(R.id.viewPager);

        initView();
    }

    private void initView() {
        List<Fragment> fragments = new ArrayList<>();
        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1;
        fragments.add(OnlineModeFragment.newInstance());
        mRadioGroup.addView(createItem(0, getString(R.string.online_tts)), layoutParams);
        fragments.add(OfflineModeFragment.newInstance());
        mRadioGroup.addView(createItem(1, getString(R.string.offline_tts)), layoutParams);

        Adapter adapter = new Adapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(adapter.getCount());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mRadioGroup.check(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                mViewPager.setCurrentItem(checkedId, true);
            }
        });
    }

    private static class Adapter extends FragmentStatePagerAdapter {

        private final List<Fragment> itemlist;

        public Adapter(FragmentManager fm, List<Fragment> itemlist) {
            super(fm);
            this.itemlist = itemlist;
        }

        @Override
        public Fragment getItem(int position) {

            return itemlist.get(position);

        }

        @Override
        public int getCount() {
            return itemlist.size();
        }
    }

    private RadioButton createItem(int id, String title) {
        RadioButton rb = new RadioButton(this);
        rb.setId(id);
        if (id == 0) {
            rb.setChecked(true);
        }
        rb.setBackground(new ColorDrawable(ContextCompat.getColor(this, R.color.transparent)));
        rb.setButtonDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.transparent)));
        rb.setGravity(Gravity.CENTER);
        rb.setText(title);
        rb.setTextSize(18);
        rb.setTextColor(ContextCompat.getColorStateList(this, R.color.rb_bottom_text));
        rb.setGravity(Gravity.CENTER);
        return rb;
    }
}
