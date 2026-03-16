package com.example.ecofriendly.data.models;

public class User {

    private String uid;
    private String email;
    private String name;
    private String avatarName;
    private int streak;
    private int points;
    private int level;
    private int completedTask;
    private boolean isActive;
    private Object createdAd;
    private String lastActivityDay;

    public User() {}

    public User (String uid, String email, String name){
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.avatarName= "";
        this.streak = 0;
        this.points = 0;
        this.level = 0;
        this.completedTask = 0;
        this.isActive = true;
        this.createdAd = com.google.firebase.firestore.FieldValue.serverTimestamp();
        this.lastActivityDay=null;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getAvatarName() {
        return avatarName;
    }

    public int getStreak() {
        return streak;
    }

    public int getPoints() {
        return points;
    }

    public int getLevel() {
        return level;
    }

    public int getCompletedTask() {
        return completedTask;
    }

    public boolean isActive() {
        return isActive;
    }

    public Object getCreatedAd() {
        return createdAd;
    }

    public String getLastActivityDay() {
        return lastActivityDay;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatarName(String avatarName) {
        this.avatarName = avatarName;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setCompletedTask(int completedTask) {
        this.completedTask = completedTask;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setCreatedAd(Object createdAd) {
        this.createdAd = createdAd;
    }

    public void setLastActivityDay(String lastActivityDay) {
        this.lastActivityDay = lastActivityDay;
    }
}
