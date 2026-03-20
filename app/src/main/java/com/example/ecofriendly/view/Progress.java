package com.example.ecofriendly.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ecofriendly.R;
import com.example.ecofriendly.data.GameRepository;
import com.example.ecofriendly.data.Repository;
import com.example.ecofriendly.data.models.Badge;
import com.example.ecofriendly.data.models.User;
import com.example.ecofriendly.data.models.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;


public class Progress extends Fragment {
    private TextView TVlevel, TVtotalPoints, TVearnedPoints, TVremainingPoints, TVstreak, descrStreak,
            cardStreak, cardCompletedTask, cardBages, all, callStreak, ouStreak, nullBadges, titleLast,
            titleSecondLast;
    private LinearLayout badgesContainer;
    private CardView lastCard, lastSecondCard;
    private ImageView[] days;

    private UserViewModel userViewModel;

    private ImageView imageLast, imageSecondLast;
   private FirebaseFirestore db;
   private Repository repository;
   private FirebaseAuth mAuth;
   private GameRepository gameRepository;
   private ProgressBar progressBar;
   String uid;


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
        all = view.findViewById(R.id.all);
        progressBar = view.findViewById(R.id.progress_bar);
        callStreak = view.findViewById(R.id.callStreak);
        ouStreak = view.findViewById(R.id.OuStreak);
        badgesContainer = view.findViewById(R.id.cardContainer);
        nullBadges = view.findViewById(R.id.nullBadges);
        lastSecondCard = view.findViewById(R.id.lastSecondCard);
        lastCard = view.findViewById(R.id.lastCard);
        imageLast = view.findViewById(R.id.imageLast);
        titleLast = view.findViewById(R.id.titleLast);
        imageSecondLast = view.findViewById(R.id.imageSecondLast);
        titleSecondLast = view.findViewById(R.id.titleSecondLast);
        repository = new Repository();

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        days = new ImageView[]{
                view.findViewById(R.id.day1),
                view.findViewById(R.id.day2),
                view.findViewById(R.id.day3),
                view.findViewById(R.id.day4),
                view.findViewById(R.id.day5),
                view.findViewById(R.id.day6),
                view.findViewById(R.id.day7)
        };

        userViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            if (user == null) return;

            TVlevel.setText(String.valueOf(user.getLevel()));
            TVtotalPoints.setText("100");

            int levelProgress = user.getPoints() % 100;
            if (user.getPoints() > 0 && levelProgress == 0) {
                levelProgress = 100;
            }

            progressBar.setMax(100);
            progressBar.setProgress(levelProgress);

            TVearnedPoints.setText(String.valueOf(levelProgress));
            TVremainingPoints.setText(String.valueOf(100 - levelProgress));

            if (user.getStreak() == 0) {
                callStreak.setText("Ваша серия ещё не началась :(");
                TVstreak.setText("0");
                ouStreak.setText("");
            } else {
                cardBages.setText("Ты выполняешь эко задания уже ");
                TVstreak.setText(String.valueOf(user.getStreak()));
                ouStreak.setText(" дней подряд!");
            }

            descrStreak.setText(String.valueOf(user.getStreak()));
            cardStreak.setText(String.valueOf(user.getStreak()));
            cardCompletedTask.setText(String.valueOf(user.getCompletedTasks()));
            updateStreakUI(user.getStreak());
        });

        gameRepository = new GameRepository();

        all.setOnClickListener(v -> startActivity(new Intent(requireContext(), CompletedBages.class)));

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        uid = "";
        if (firebaseUser != null) {
            uid = firebaseUser.getUid();
        }

        loadedLastBadges();
        loadBadgesCount();

        return view;
    }


    public void updateStreakUI(int streak) {
        for (int i = 0; i < days.length; i++) {
            if (i < streak && i < 7) {
                days[i].setImageResource(R.drawable.day_of_week_active);
            } else {
                days[i].setImageResource(R.drawable.day_of_week);
            }
        }
    }

    /**
     * Заполняем карточки со значками
     */
    public void loadedLastBadges() {
        repository.getTwoLastBages(uid, new Repository.bagesTwoLastListener() {
            @Override
            public void onLoaded(Badge lastBadge, Badge secondLastBadge) {

                if (lastBadge == null && secondLastBadge == null) {
                    badgesContainer.setVisibility(View.GONE);
                    nullBadges.setVisibility(View.VISIBLE);
                    return;
                }

                badgesContainer.setVisibility(View.VISIBLE);
                nullBadges.setVisibility(View.GONE);

                if (secondLastBadge == null) {
                    lastSecondCard.setVisibility(View.GONE);
                    lastCard.setVisibility(View.VISIBLE);

                    @SuppressLint("DiscouragedApi") int resId = getContext()
                            .getResources()
                            .getIdentifier(lastBadge.getImageName(), "drawable", getContext().getPackageName());
                    if (resId != 0) {
                        imageLast.setImageResource(resId);
                    }
                    titleLast.setText(lastBadge.getTitle());

                } else {
                    lastSecondCard.setVisibility(View.VISIBLE);
                    lastCard.setVisibility(View.VISIBLE);


                    if(lastBadge!=null) {
                        @SuppressLint("DiscouragedApi") int resId = getContext()
                                .getResources()
                                .getIdentifier(lastBadge.getImageName(), "drawable", getContext().getPackageName());
                        if (resId != 0) {
                            imageLast.setImageResource(resId);
                        }
                        titleLast.setText(lastBadge.getTitle());

                        @SuppressLint("DiscouragedApi") int resSecondId = getContext()
                                .getResources()
                                .getIdentifier(secondLastBadge.getImageName(), "drawable", getContext().getPackageName());
                        if (resSecondId != 0) {
                            imageSecondLast.setImageResource(resSecondId);
                        }
                        titleSecondLast.setText(secondLastBadge.getTitle());
                    }
                }

            }

            @Override
            public void onError(String error) {
                Log.e("Progress", "Error:"+error);

            }
        });

    }

    private void loadBadgesCount() {
        if (uid == null || uid.isEmpty()) return;

        repository.getListBagesUser(uid, new Repository.bagesUserListListener() {
            @Override
            public void onLoaded(List<Badge> badgeList) {
                cardBages.setText(String.valueOf(badgeList.size()));
            }

            @Override
            public void onError(String error) {
                Log.e("Progress", "Error loading badges count: " + error);
            }
        });
    }

}