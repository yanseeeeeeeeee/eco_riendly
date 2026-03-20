package com.example.ecofriendly.data.models;

import java.sql.Timestamp;

public class UserBadge {

    private String uidBadge;
    private Timestamp receivedAt;

    public UserBadge() {}

    public UserBadge(String uidBadge, Timestamp receivedAt) {
        this.uidBadge = uidBadge;
        this.receivedAt = receivedAt;
    }

    public String getUidBadge() {
        return uidBadge;
    }

    public Timestamp getReceivedAt() {
        return receivedAt;
    }

    public void setUidBadge(String uidBadge) {
        this.uidBadge = uidBadge;
    }

    public void setReceivedAt(Timestamp receivedAt) {
        this.receivedAt = receivedAt;
    }
}
