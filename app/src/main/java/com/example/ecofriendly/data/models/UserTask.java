package com.example.ecofriendly.data.models;

public class UserTask {

    private String taskId;
    private String status;
    private boolean hasPhoto;
    private long completedAt;
    private int earnedPoints;

    public UserTask() {}

    public UserTask(String taskId, String status, boolean hasPhoto, long completedAt, int earnedPoints) {
        this.taskId = taskId;
        this.status = status;
        this.hasPhoto = hasPhoto;
        this.completedAt = completedAt;
        this.earnedPoints = earnedPoints;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getStatus() {
        return status;
    }

    public boolean isHasPhoto() {
        return hasPhoto;
    }

    public long getCompletedAt() {
        return completedAt;
    }

    public int getEarnedPoints() {
        return earnedPoints;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setHasPhoto(boolean hasPhoto) {
        this.hasPhoto = hasPhoto;
    }

    public void setCompletedAt(long completedAt) {
        this.completedAt = completedAt;
    }

    public void setEarnedPoints(int earnedPoints) {
        this.earnedPoints = earnedPoints;
    }
}
