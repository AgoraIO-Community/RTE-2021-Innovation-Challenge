package Test;

public class TestUser {
    private String username;
    private String password;

    public String getUser()
    {
        return username;
    }

    public String getPwd()
    {
        return password;
    }

    public void setUser(String user)
    {
       this.username = user;
    }

    public void setPwd(String pwd)
    {
        this.password = pwd;
    }
}
