package mobi.yiyin.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import mobi.yiyin.R;

/**
 * 定义我 页面的有点击箭头以及分割线的条目，并且可以隐藏分割线
 *
 * @author wlk
 * @date 2020/9/2
 */

public class LinerView extends FrameLayout {

    /**
     * 是否有线
     */
    private boolean haveLine;

    /**
     * 标题
     */
    private String title;

    /**
     * 图标地址
     */
    private int iconUrl;

    /**
     * 是否有返回按钮
     */
    private boolean haveBack;


    public LinerView(Context context) {
        this(context, null);
    }

    public LinerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (initType(context, attrs)) {
            return;
        }
        initView(context);
    }

    /**
     * 加载自定义的布局
     *
     * @param context context
     */
    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.liner_view, this, true);
        ImageView linerIcon = (ImageView) findViewById(R.id.liner_icon);
        TextView linerTitle = (TextView) findViewById(R.id.liner_title);
        ImageView linerBack = (ImageView) findViewById(R.id.liner_back);
        View linerLine = findViewById(R.id.liner_line);
        if (haveLine){
            linerLine.setVisibility(VISIBLE);
        }else {
            linerLine.setVisibility(INVISIBLE);
        }

        if (haveBack){
            linerBack.setVisibility(VISIBLE);
        }else {
            linerBack.setVisibility(GONE);
        }
        linerIcon.setImageResource(iconUrl);
        linerTitle.setText(title);
    }

    /**
     * 加载自定义的属性
     *
     * @param context context
     * @param attrs   attrs
     * @return
     */
    private boolean initType(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return true;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LinerView);
        haveLine = typedArray.getBoolean(R.styleable.LinerView_have_line, true);
        title = typedArray.getString(R.styleable.LinerView_liner_title);
        iconUrl = typedArray.getResourceId(R.styleable.LinerView_liner_icon_url, R.mipmap.imgs);
        haveBack = typedArray.getBoolean(R.styleable.LinerView_have_back, true);
        typedArray.recycle();
        return false;
    }
}
