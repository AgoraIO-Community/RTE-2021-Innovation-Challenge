package com.example.jialichun.client.model;

/**
 * Created by jialichun on 2018/2/27.
 */

public class Door {
    String name;
    Boolean state;
    public Door(String name,Boolean state){
        this.name=name;
        this.state=state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }
}
