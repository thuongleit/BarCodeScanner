package com.whooo.babr.view.session.base;

public class User {
    public String email;
    public String fullname;
    public String password;
    public String id;

    public User(String fullname,String id) {
        this.fullname = fullname;
        this.id=id;
    }
}
