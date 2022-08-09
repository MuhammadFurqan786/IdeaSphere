package com.example.stormeducation.models;

public class CommentsModel {

    String postId,comment,username,date,time;

    public CommentsModel(String postId, String comment, String username, String date) {
        this.postId = postId;
        this.comment = comment;
        this.username = username;
        this.date = date;
    }

    public CommentsModel() {
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
