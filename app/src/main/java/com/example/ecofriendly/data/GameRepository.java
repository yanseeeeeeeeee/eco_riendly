package com.example.ecofriendly.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GameRepository {

    public GameRepository() {}

    public int calculateNewStreak(String lastCompletedDay, int currentStreak, String today) {
        if (lastCompletedDay == null || lastCompletedDay.isEmpty()) {
            return 1;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try {
            Date todayDate = simpleDateFormat.parse(today);
            Date lastDate = simpleDateFormat.parse(lastCompletedDay);

            if (todayDate == null || lastDate ==null) {
                return 1;
            }

            long difmils = todayDate.getTime() - lastDate.getTime();
            long diffDays = difmils / (1000 * 60 * 60 *24);

            if (diffDays == 0 ) {
                return currentStreak;
            } else if (diffDays == 1) {
                return currentStreak +1;
            } else  {
                return 1;
            }

        } catch (ParseException e){
            e.printStackTrace();
            return 1;
        }

    }

    public int calculateLevel(int points) {
        return (points/100) +1;
    }

    public int getPointsForNextLevel(int points) {
        int remainingPoints = points % 100;
        return remainingPoints == 0 ? 100 : 100 -remainingPoints;
    }

    public String getTodayDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return simpleDateFormat.format(new Date());
    }

    public List<String> getBadgesForTaskCompletion(int completedTasks, int streak) {
        List<String> badges = new ArrayList<>();

        if (completedTasks == 1) badges.add("first_step");
        if (completedTasks == 5) badges.add("tasks_5");
        if (completedTasks == 10) badges.add("tasks_10");

        if (streak == 3) badges.add("streak_3");
        if (streak == 7) badges.add("streak_7");

        return badges;
    }

}
