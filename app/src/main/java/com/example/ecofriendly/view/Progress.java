package com.example.ecofriendly.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class Progress extends Fragment {
    private TextView TVlevel, TVtotalPoints, TVearnedPoints, TVremainingPoints, TVstreak, descrStreak,
            cardStreak, cardCompletedTask, cardBages, all, callStreak, ouStreak, nullBadges, titleLast,
            titleSecondLast;
    private LinearLayout badgesContainer;
    private CardView lastCard, lastSecondCard;

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


        mAuth = FirebaseAuth.getInstance();
        repository = new Repository();
        gameRepository = new GameRepository();

        FirebaseUser user = mAuth.getCurrentUser();
        uid = "";

        if (user != null) {
            uid = user.getUid();
        }

        loadedInfoWithUser(uid);

        all.setOnClickListener(v -> startActivity(new Intent(requireContext(), CompletedBages.class)));

        progressBar.setMax(100);
        repository.getUser(uid, new Repository.userGetInfoListenner() {
           @Override
           public void onLoaded(User user) {
               progressBar.setProgress(user.getPoints());
           }

           @Override
           public void onError(String error) {
                Log.e("Progress", "Error:" + error);
           }
       });

        loadedLastBadges();

        return view;
    }

    /**
     * Просто вставляем всю инфу о юзере через сеты
     * @param uid
     */
    public void loadedInfoWithUser(String uid) {
        repository.getUser(uid, new Repository.userGetInfoListenner() {
            @Override
            public void onLoaded(User user) {
                TVlevel.setText(String.valueOf(user.getLevel()));
                TVtotalPoints.setText(String.valueOf(100));
                TVearnedPoints.setText(String.valueOf(user.getPoints()));
                TVremainingPoints.setText(String.valueOf(gameRepository
                        .getPointsForNextLevel(user.getPoints())));

                if (user.getStreak() == 0) {
                    callStreak.setText("Ваша серия ещё не началась :(");
                    TVstreak.setText("");
                    ouStreak.setText("");
                } else { TVstreak.setText(String.valueOf(user.getStreak()));}

                descrStreak.setText(String.valueOf(user.getStreak()));
                cardStreak.setText(String.valueOf(user.getStreak()));
                cardCompletedTask.setText(String.valueOf(user.getCompletedTasks()));
                cardBages.setText(String.valueOf(0));
            }

            @Override
            public void onError(String error) {
                Log.e("Progress", "Error:" + error);
            }
        });

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
                            imageSecondLast.setImageResource(resId);
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



}