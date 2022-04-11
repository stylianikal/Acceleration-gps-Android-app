package com.example.gpsapp.User;

import com.google.firebase.database.Exclude;

public class Authentication {
    private String username;
    private String password;

    public Authentication(){

    }

    public Authentication(String username, String password) {
        this.username = username;
        this.password = password;
    }
    @Exclude
    public String getusername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
}
