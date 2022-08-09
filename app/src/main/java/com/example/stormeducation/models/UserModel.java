package com.example.stormeducation.models;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class UserModel implements Serializable {
    public String uid, email,username,image;

    public UserModel(String uid, String email, String username,String image) {
        this.uid = uid;
        this.email = email;
        this.username = username;
        this.image= image;
    }

    public UserModel() {
    }
}
