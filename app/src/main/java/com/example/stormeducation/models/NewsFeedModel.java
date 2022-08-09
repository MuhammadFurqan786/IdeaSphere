package com.example.stormeducation.models;

public class NewsFeedModel {

    String news_id,image,name, question;

    public NewsFeedModel(String news_id, String image, String name, String question) {
        this.news_id = news_id;
        this.image = image;
        this.name = name;
        this.question = question;
    }

    public NewsFeedModel(String image, String name, String question) {
        this.image = image;
        this.name = name;
        this.question = question;
    }

    public NewsFeedModel() {
    }

    public String getNews_id() {
        return news_id;
    }

    public void setNews_id(String news_id) {
        this.news_id = news_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
