package com.example.ecofriendly.data.models;

public class Task {

    private String taskId;
    private String title;
    private String shortDescription;
    private String description;
    private String categoryId;
    private int points;
    private boolean isActive;

    public Task(){}

    public Task(String taskId, String title, String shortDescription, String description, String categoryId, int points, boolean isActive) {
        this.taskId = taskId;
        this.title = title;
        this.shortDescription = shortDescription;
        this.description = description;
        this.categoryId = categoryId;
        this.points = points;
        this.isActive = isActive;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getTitle() {
        return title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public int getPoints() {
        return points;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
