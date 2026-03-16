package com.example.ecofriendly.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ecofriendly.R;
import com.example.ecofriendly.data.Repository;
import com.example.ecofriendly.data.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile extends Fragment {

    private TextView name, email, changeProfile, points, completedTask, streak, settings;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    Repository repository;
    String uid;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Profile() {}

    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container,false);

        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        changeProfile = view.findViewById(R.id.change_profile);
        points = view.findViewById(R.id.points);
        completedTask = view.findViewById(R.id.completed_task);
        streak = view.findViewById(R.id.streak);
        settings = view.findViewById(R.id.settings);
        mAuth = FirebaseAuth.getInstance();
        repository = new Repository();
        FirebaseUser user = mAuth.getCurrentUser();


        uid = "";
        if (user != null) {uid = user.getUid();}

        changeProfile.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), ChangeProfile.class));
        });

        settings.setOnClickListener(v-> {
            startActivity(new Intent(requireContext(), Settings.class));
        });

        repository.getUser(uid, new Repository.userGetInfoListenner() {
            @Override
            public void onLoaded(String userName, String userEmail, String userPoints, String completedTaskStr, String streakStr) {
                name.setText(userName);
                email.setText(userEmail);
                points.setText(userPoints);
                completedTask.setText(completedTaskStr);
                streak.setText(streakStr);
            }

            @Override
            public void onError(String error) {
                Log.e("Profile", "Ошибка:"+error);
            }
        });

        return view;
    }



}