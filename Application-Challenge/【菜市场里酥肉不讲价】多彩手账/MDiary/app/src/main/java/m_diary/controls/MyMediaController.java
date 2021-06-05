package m_diary.controls;

import android.content.Context;
import android.view.View;
import android.widget.MediaController;

public class MyMediaController extends MediaController{
    public MyMediaController(Context context, View anchor) {
        super(context);
        super.setAnchorView(anchor);//设置控制器的锚点视图
    }

    @Override
    public void setAnchorView(View anchor) {
        // Do nothing
    }
}
