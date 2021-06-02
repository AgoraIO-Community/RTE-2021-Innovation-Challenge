package com.example.jialichun.client.model;

/**
 * Created by Jay on 2015/9/25 0025.
 */
public class Item {

    private int iId;
    private String iName;

    public Item() {
    }

    public Item(int iId, String iName) {
        this.iId = iId;
        this.iName = iName;
    }

    public int getiId() {
        return iId;
    }

    public String getiName() {
        return iName;
    }

    public void setiId(int iId) {
        this.iId = iId;
    }

    public void setiName(String iName) {
        this.iName = iName;
    }
}
