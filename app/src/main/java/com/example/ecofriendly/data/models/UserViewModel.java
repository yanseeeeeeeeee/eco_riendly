package com.example.ecofriendly.data.models;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class UserViewModel extends ViewModel {

    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Badge>> badgesLiveData = new MutableLiveData<>();

    public MutableLiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<List<Badge>> getBadgesLiveData() {
        return badgesLiveData;
    }

    public void setUser(User user) {
        userLiveData.setValue(user);
    }

    public void setBadges(List<Badge> badges) {
        badgesLiveData.setValue(badges);
    }
}
