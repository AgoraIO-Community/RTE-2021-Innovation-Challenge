package m_diary.assets;

//音频
public class Audio extends Items {
    public String path;
    public String uri;
    public String name;
    public int length;
    public Audio(String name, String path, String uri){
        this.name = name;
        this.path = path;
        this.uri = uri;
    }
}
