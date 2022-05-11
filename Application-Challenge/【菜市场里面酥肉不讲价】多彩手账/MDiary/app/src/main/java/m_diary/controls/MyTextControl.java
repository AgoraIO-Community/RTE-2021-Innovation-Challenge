package m_diary.controls;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

//文本控件
@SuppressLint("AppCompatCustomView")
public class MyTextControl extends EditText {

    public MyTextControl(Context context) {
        super(context);
    }

    public MyTextControl(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextControl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyTextControl(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
