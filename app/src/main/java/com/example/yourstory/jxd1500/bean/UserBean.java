package com.example.yourstory.jxd1500.bean;

/**
 * Created by lenovo on 2019/4/2.
 */

public class UserBean {

    public String id;
    public String name;
    public String group;
    public String password;
    public String fingerCount;

    public UserBean() {
    }


    public UserBean(String id, String name, String group, String password, String fingerCount) {
        this.id = id;
        this.name = name;
        this.group = group;
        this.password = password;
        this.fingerCount = fingerCount;
    }
}
