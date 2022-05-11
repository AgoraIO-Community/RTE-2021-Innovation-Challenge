package m_diary.assets;

public class User {
    public String requestCode;
    public String username;
    public String password;
    public User(String requestCode, String username, String password){
        this.requestCode = requestCode;
        this.username = username;
        this.password = password;
    }
}
