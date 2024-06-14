package com.example.chat_r4;

public class Blog {
    String title;
    String description;
    int likes;
    String timestamp;

    public Blog() {
    }

    public Blog(String title, String description, int likes, String timestamp) {
        this.title = title;
        this.description = description;
        this.likes = likes;
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
