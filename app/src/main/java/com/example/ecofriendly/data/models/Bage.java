package com.example.ecofriendly.data.models;

public class Bage {

    private String uid;
    private String title;
    private String description;
    private String imageName;

    public Bage() {}

    public Bage(String uid, String title, String description, String imageName) {
        this.uid = uid;
        this.title = title;
        this.description = description;
        this.imageName = imageName;
    }

    public String getUid() {
        return uid;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageName() {
        return imageName;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
