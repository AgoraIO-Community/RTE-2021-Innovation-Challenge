package m_diary.assets;

import android.graphics.PointF;

public class Video extends Items {
    public String path;
    public Video(String path, float x, float y, int width, int height){
        this.path = path;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
