package m_diary.controls;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

//图片控件
@SuppressLint("AppCompatCustomView")
public class MyPictureControl extends ImageView {
    public MyPictureControl(Context context) {
        super(context);
    }

    public MyPictureControl(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyPictureControl(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyPictureControl(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
