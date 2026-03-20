package com.example.ecofriendly.view;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ecofriendly.R;
import com.example.ecofriendly.data.Repository;
import com.example.ecofriendly.data.models.User;
import com.example.ecofriendly.data.models.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FragmentContainer extends BaseActivity {

    private BottomNavigationView bottomMenu;

    private Fragment homeFragment;
    private Fragment progressFragment;
    private Fragment profileFragment;
    private Fragment activeFragment;
    private UserViewModel userViewModel;
    String uid;
    private Repository repository;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_container;
    }

    @Override
    protected int getRootViewId() {
        return R.id.root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        bottomMenu = findViewById(R.id.bottomNavigationView);

        repository = new Repository();

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        uid ="";

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();
            subscribeToUser();
        }

        if (savedInstanceState == null) {
            homeFragment = new Home();
            progressFragment = new Progress();
            profileFragment = new Profile();

            activeFragment = homeFragment;

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, profileFragment, "profile")
                    .hide(profileFragment)
                    .add(R.id.fragment_container, progressFragment, "progress")
                    .hide(progressFragment)
                    .add(R.id.fragment_container, homeFragment, "home")
                    .commit();
        } else {
            homeFragment = getSupportFragmentManager().findFragmentByTag("home");
            progressFragment = getSupportFragmentManager().findFragmentByTag("progress");
            profileFragment = getSupportFragmentManager().findFragmentByTag("profile");

            if (homeFragment != null && !homeFragment.isHidden()) {
                activeFragment = homeFragment;
            } else if (progressFragment != null && !progressFragment.isHidden()) {
                activeFragment = progressFragment;
            } else {
                activeFragment = profileFragment;
            }
        }

        bottomMenu.setOnItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();

            if (id == R.id.home) {
                showFragment(homeFragment);
                return true;
            } else if (id == R.id.notes) {
                showFragment(progressFragment);
                return true;
            } else if (id == R.id.profile) {
                showFragment(profileFragment);
                return true;
            }

            return false;
        });
    }

    private void showFragment(Fragment fragment) {
        if (fragment == null || fragment == activeFragment) return;

        getSupportFragmentManager()
                .beginTransaction()
                .hide(activeFragment)
                .show(fragment)
                .commit();

        activeFragment = fragment;
    }

    public void subscribeToUser() {
        repository.getUser(uid, new Repository.userGetInfoListenner() {
            @Override
            public void onLoaded(User user) {
                userViewModel.setUser(user);
            }

            @Override
            public void onError(String error) {
                Log.e("FragmentContainer", "Error:" +error);
            }
        });
    }
}