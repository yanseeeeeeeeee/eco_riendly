package com.example.ecofriendly.data.models;

public class Badge {

    private String badgeId;
    private String title;
    private String description;
    private String imageName;

    public Badge() {}

    public Badge(String uid, String title, String description, String imageName) {
        this.badgeId = uid;
        this.title = title;
        this.description = description;
        this.imageName = imageName;
    }

    public String getBadgeId() {
        return badgeId;
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

    public void setBadgeId(String badgeId) {
        this.badgeId = badgeId;
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
