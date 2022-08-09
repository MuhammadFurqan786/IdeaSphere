package com.example.stormeducation.models;

public class ChatModel {

    String name,msg;

    public ChatModel(String name, String msg) {
        this.name = name;
        this.msg = msg;
    }

    public ChatModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
