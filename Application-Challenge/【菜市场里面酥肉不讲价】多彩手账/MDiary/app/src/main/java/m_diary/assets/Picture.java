package m_diary.assets;

public class Picture extends Items{
    public String type;
    public String path;
    public Picture(String path, float x, float y, String type){
        this.path = path;
        this.x = x;
        this.y = y;
        this.type = type;
    }
}
