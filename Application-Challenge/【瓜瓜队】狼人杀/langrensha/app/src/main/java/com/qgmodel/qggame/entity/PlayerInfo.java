package com.qgmodel.qggame.entity;

import java.util.Objects;
import cn.bmob.v3.BmobObject;

public class PlayerInfo extends BmobObject {

    private String name;//玩家名称
    private String uid;//玩家uid
    private String url;//玩家头像

    public PlayerInfo(String name, String uid, String avatar) {
        this.name = name;
        this.uid = uid;
        this.url = avatar;
    }

    public PlayerInfo(String name, String uid) {
        this.name = name;
        this.uid = uid;

        double random = Math.random()*3;
        if(random<1){
           //this.url = "https://s1.ax1x.com/2020/08/31/dOj6Qe.png";
            this.url = "https://i.loli.net/2020/09/01/EpboQq27NHeWg3X.png";
        }else if(random>=2){
            //this.url = "https://s1.ax1x.com/2020/08/31/dOxEuQ.png";
            this.url = "https://i.loli.net/2020/09/01/uFiYn7pZqchJvjO.png";
        }else {
            //this.url = "https://s1.ax1x.com/2020/08/31/dOxBvD.png";
            this.url = "https://i.loli.net/2020/09/01/Kw6oufGjgMtbJ5m.png";
        }

    }

    public PlayerInfo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerInfo that = (PlayerInfo) o;
        return name.equals(that.name) &&
                uid.equals(that.uid) &&
                url.equals(that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, uid, url);
    }

}
