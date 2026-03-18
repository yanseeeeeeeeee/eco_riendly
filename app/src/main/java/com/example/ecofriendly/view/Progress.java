package com.example.ecofriendly.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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


public class Progress extends Fragment {

    private int earnedPoints, totalPoints, remainingPoints, level, streak, bages, completedTask;

    private TextView TVlevel, TVtotalPoints, TVearnedPoints, TVremainingPoints, TVstreak, descrStreak,
            cardStreak, cardCompletedTask, cardBages;

   private FirebaseFirestore db;
   private Repository repository;
   private FirebaseAuth mAuth;




    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Progress() {}

    public static Progress newInstance(String param1, String param2) {
        Progress fragment = new Progress();
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
        View view = inflater.inflate(R.layout.fragment_progress, container,false);
        TVlevel = view.findViewById(R.id.level);
        TVtotalPoints = view.findViewById(R.id.totalPoints);
        TVearnedPoints = view.findViewById(R.id.earnedPoints);
        TVremainingPoints = view.findViewById(R.id.remainingPoints);
        TVstreak = view.findViewById(R.id.streak);
        descrStreak = view.findViewById(R.id.descrStreak);
        cardStreak = view.findViewById(R.id.streakCard);
        cardCompletedTask = view.findViewById(R.id.completed_task);
        cardBages = view.findViewById(R.id.bages);
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = "";

        if (user != null) {
            uid = user.getUid();
        }

        loadedInfoWithUser(uid);

        return view;
    }

    public void loadedInfoWithUser(String uid) {
        repository.getUser(uid, new Repository.userGetInfoListenner() {
            @Override
            public void onLoaded(User user) {
                TVlevel.setText(String.valueOf(user.getLevel()));
                TVtotalPoints.setText(String.valueOf(totalPoints));
                TVearnedPoints.setText(String.valueOf(user.getPoints()));
                TVremainingPoints.setText(String.valueOf(remainingPoints));
                TVstreak.setText(String.valueOf(user.getStreak()));
                descrStreak.setText(String.valueOf(user.getStreak()));
                cardStreak.setText(String.valueOf(user.getStreak()));
                cardCompletedTask.setText(String.valueOf(user.getCompletedTask()));
                cardBages.setText(String.valueOf(bages));
            }

            @Override
            public void onError(String error) {

            }
        });

    }
}