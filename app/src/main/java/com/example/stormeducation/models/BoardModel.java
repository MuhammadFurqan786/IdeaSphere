package com.example.stormeducation.models;

public class BoardModel {

    String postid,text,userId,username,date,type;


    public BoardModel(String postid, String text, String userId, String username, String date,String type) {
        this.postid = postid;
        this.text = text;
        this.userId = userId;
        this.username = username;
        this.date = date;
        this.type=type;
    }

    public BoardModel() {
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }
}