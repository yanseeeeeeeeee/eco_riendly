package com.example.ecofriendly.data;

import com.example.ecofriendly.data.models.Badge;
import com.example.ecofriendly.data.models.Task;
import com.example.ecofriendly.data.models.User;
import com.example.ecofriendly.data.models.UserTask;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Repository {

    public Repository() {}

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    GameRepository gameRepository = new GameRepository();

    /**
     * Метод для получения полной модели пользователя
     * @param uid
     * @param listener
     */
    public void getUser(String uid, userGetInfoListenner listener) {
        db.collection("users")
                .document(uid)
                .addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        listener.onError("Error: " + e.getMessage());
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            listener.onLoaded(user);
                        } else {
                            listener.onError("Пользователь не найден");
                        }
                    } else {
                        listener.onError("Пользователь не найден");
                    }
                });
    }

    /**
     * получение листика с активными задачами
     * @param listener
     */
   public void getListTask( taskListGetInfoListener listener ) {
       db.collection("task")
               .whereEqualTo("active", true)
               .get()
               .addOnSuccessListener(queryDocumentSnapshot -> {

                       List<Task> newList = new ArrayList<>();

                       for (DocumentSnapshot document : queryDocumentSnapshot.getDocuments()) {
                           Task task = document.toObject(Task.class);
                           if (task != null) {
                               newList.add(task);
                           }
                       }

                       listener.onLoaded(newList);

               })
               .addOnFailureListener(e -> {
                   listener.onError("Error:" + e.getMessage());
               });
   }

    /**
     * Метод для получения задачи по id
     *
     * @param uid
     */
   public void getTaskWithUid(String uid, taskInfoListener listener){
       db.collection("task")
               .document(uid)
               .get()
               .addOnSuccessListener(documentSnapshot -> {

                   if (documentSnapshot.exists()) {
                       Task task = documentSnapshot.toObject(Task.class);

                       if (task != null) {
                           listener.onLoaded(task);
                       }

                   } else {
                       listener.onError("Задача не найдена");
                   }

               })
               .addOnFailureListener(e -> {
                   listener.onError("Error" + e.getMessage());
               });
   }

    /**
     * метод для сохранения выполненной задачи
     * в этом методе так же содержится создание подколлекции user_tasks
     * @param uid
     * @param task
     * @param listener
     */
   public void completeTask (String uid, Task task, completeTaskListener listener){

       DocumentReference userReference = db.collection("users").document(uid);
       DocumentReference taskUserReference = db.collection("users")
               .document(uid)
               .collection("user_tasks")
               .document(task.getTaskId());

       taskUserReference.get().addOnSuccessListener(documentSnapshot ->  {

           if (documentSnapshot.exists()) {
               listener.onError("Задание уже выполнено");
               return;
           }

           userReference.get().addOnSuccessListener(userSnapshot -> {

               User user = userSnapshot.toObject(User.class);
               if (user == null) {
                   listener.onError("Пользователь не найден");
                   return;
               }

               int newPoints = user.getPoints() + task.getPoints();
               int newCompletedTask = user.getCompletedTasks() + 1;
               int newLevel = gameRepository.calculateLevel(newPoints);
               String today = gameRepository.getTodayDate();
               int newStreak = gameRepository.calculateNewStreak(user.getLastActivityDay(), user.getStreak(), today);

               UserTask userTask = new UserTask(
                       task.getTaskId(),
                       "completed",
                       true,
                       System.currentTimeMillis(),
                       task.getPoints()

               );

               WriteBatch batch = db.batch();
               batch.set(taskUserReference, userTask);

               Map<String, Object> mapUser = new HashMap<>();
               mapUser.put("points", newPoints);
               mapUser.put("completedTasks", newCompletedTask);
               mapUser.put("level", newLevel);
               mapUser.put("streak", newStreak);
               mapUser.put("lastActivityDay", today);

               batch.update(userReference, mapUser);

                       //действие с бейджами
                       List<String> newBadges = gameRepository.getBadgesForTaskCompletion(
                               newCompletedTask,
                               newStreak
                       );

                       for (String badgeId : newBadges) {
                           DocumentReference badgeReference = db.collection("users")
                                   .document(uid)
                                   .collection("user_badges")
                                   .document(badgeId);

                           Map<String, Object> badgeMap = new HashMap<>();
                           badgeMap.put("badgeId", badgeId);
                           badgeMap.put("receivedAt", FieldValue.serverTimestamp());

                           batch.set(badgeReference, badgeMap);
                       }

               batch.commit().addOnSuccessListener(unused -> {

                   listener.onSuccess();
               })
                       .addOnFailureListener(e -> listener.onError(e.getMessage()));

           })
                   .addOnFailureListener(e -> listener.onError(e.getMessage()));

       })
               .addOnFailureListener(e -> listener.onError(e.getMessage()));

   }

    /**
     * метод для получения листа с задачами, доступными для пользователя к выполнению
     * @param uid
     * @param listener
     */
    public void getAvailableTasks(String uid, taskListGetInfoListener listener) {
        db.collection("users")
                .document(uid)
                .collection("user_tasks")
                .get()
                .addOnSuccessListener(userTasksSnapshot -> {

                    Set<String> completedTaskIds = new HashSet<>();

                    for (DocumentSnapshot document : userTasksSnapshot.getDocuments()) {
                        completedTaskIds.add(document.getId());
                    }

                    db.collection("task")
                            .whereEqualTo("active", true)
                            .get()
                            .addOnSuccessListener(taskSnapshot -> {

                                List<Task> newList = new ArrayList<>();

                                for (DocumentSnapshot document : taskSnapshot.getDocuments()) {
                                    Task task = document.toObject(Task.class);

                                    if (task != null) {
                                        String taskId = document.getId();


                                        if (!completedTaskIds.contains(taskId)) {
                                            task.setTaskId(taskId);
                                            newList.add(task);
                                        }
                                    }
                                }

                                listener.onLoaded(newList);
                            })
                            .addOnFailureListener(e -> {
                                listener.onError("Error: " + e.getMessage());
                            });

                })
                .addOnFailureListener(e -> {
                    listener.onError("Error: " + e.getMessage());
                });
    }


    /**
     * метод для сохранения значка для пользователя
     * @param uid
     * @param bageUid
     * @param listener
     */
    public void setBagesForUser(String uid, String bageUid, completeTaskListener listener ) {

        Map<String, Object> bageMap = new HashMap<>();
        bageMap.put("bageId", bageUid);
        bageMap.put("receivedAt", FieldValue.serverTimestamp());

        db.collection("users")
                .document("uid")
                .collection("user_badges")
                .document(bageUid)
                .set(bageMap)
                .addOnSuccessListener(unused -> {
                    listener.onSuccess();
                })
                .addOnFailureListener(e -> listener.onError(e.getMessage()));

    }

    /**
     * Получаем лист с полученными пользователем бейджами
     * @param uid
     * @param listener
     */
    public void getListBagesUser(String uid, bagesUserListListener listener) {
        db.collection("users")
                .document(uid)
                .collection("user_badges")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        listener.onError(e.getMessage());
                        return;
                    }

                    if (queryDocumentSnapshots == null) {
                        listener.onLoaded(new ArrayList<>());
                        return;
                    }

                    List<com.google.android.gms.tasks.Task<DocumentSnapshot>> badgeTasks = new ArrayList<>();

                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        String badgeId = documentSnapshot.getId();

                        com.google.android.gms.tasks.Task<DocumentSnapshot> badgeTask =
                                db.collection("badges")
                                        .document(badgeId)
                                        .get();

                        badgeTasks.add(badgeTask);
                    }

                    if (badgeTasks.isEmpty()) {
                        listener.onLoaded(new ArrayList<>());
                        return;
                    }

                    Tasks.whenAllSuccess(badgeTasks)
                            .addOnSuccessListener(res -> {
                                List<Badge> badgeList = new ArrayList<>();

                                for (Object o : res) {
                                    DocumentSnapshot badgeDoc = (DocumentSnapshot) o;
                                    Badge badge = badgeDoc.toObject(Badge.class);

                                    if (badge != null) {
                                        badge.setBadgeId(badgeDoc.getId());
                                        badgeList.add(badge);
                                    }
                                }

                                listener.onLoaded(badgeList);
                            })
                            .addOnFailureListener(error -> listener.onError(error.getMessage()));
                });
    }


    /**
     * Получаем значок по id
     * @param badgeUid
     * @param listener
     */
    public void getBadges(String badgeUid, badgesInfoListener listener ) {
        db.collection("badges")
                .document(badgeUid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    if (documentSnapshot.exists()) {
                        Badge badge = documentSnapshot.toObject(Badge.class);


                        if (badge != null) {
                            badge.setBadgeId(badgeUid);
                            listener.onLoaded(badge);
                        } else {
                            listener.onError("Ошибка чтения значка");
                        }
                    } else {
                        listener.onError("Значок не найден");
                    }

                })
                .addOnFailureListener(e -> listener.onError(e.getMessage()));
    }


    /**
     * получаем два последних значка для экрана прогресса
     * @param uid
     * @param listener
     */
    public void getTwoLastBages(String uid, bagesTwoLastListener listener) {
        db.collection("users")
                .document(uid)
                .collection("user_badges")
                .orderBy("receivedAt", Query.Direction.DESCENDING)
                .limit(2)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        listener.onError(e.getMessage());
                        return;
                    }

                    if (queryDocumentSnapshots == null || queryDocumentSnapshots.isEmpty()) {
                        listener.onLoaded(null, null);
                        return;
                    }

                    List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();

                    String lastBadgeId = documentSnapshots.get(0).getId();

                    getBadges(lastBadgeId, new badgesInfoListener() {
                        @Override
                        public void onLoaded(Badge lastBadge) {
                            if (documentSnapshots.size() == 1) {
                                listener.onLoaded(lastBadge, null);
                                return;
                            }

                            String secondLastBadgeId = documentSnapshots.get(1).getId();

                            getBadges(secondLastBadgeId, new badgesInfoListener() {
                                @Override
                                public void onLoaded(Badge secondLastBadge) {
                                    listener.onLoaded(lastBadge, secondLastBadge);
                                }

                                @Override
                                public void onError(String error) {
                                    listener.onError(error);
                                }
                            });
                        }

                        @Override
                        public void onError(String error) {
                            listener.onError(error);
                        }
                    });
                });
    }


    /**
     * метод поможет обновить имя пользователя
     * @param uid
     * @param newName
     * @param listener
     */
    public void updateUserName(String uid, String newName, updateUserListener listener) {
        if (newName == null || newName.trim().isEmpty()) {
            listener.onError("Имя не может быть пустым");
            return;
        }

        db.collection("users")
                .document(uid)
                .update("name", newName.trim())
                .addOnSuccessListener(unused -> {
                    listener.onSuccess();
                })
                .addOnFailureListener(e -> {
                    listener.onError("Error: " + e.getMessage());
                });
    }

    public interface updateUserListener {
        void onSuccess();
        void onError(String error);
    }
    public interface bagesTwoLastListener{
        void onLoaded(Badge lastBadge, Badge secondLastBadge);
        void onError(String error);
    }
    public interface badgesInfoListener{
        void onLoaded(Badge badge);
        void onError(String error);
    }
    public interface  bagesUserListListener{
        void onLoaded(List<Badge> badgeList);
        void onError(String error);
    }

   public interface userGetInfoListenner{
       void onLoaded(User user);
       void onError(String error);
   }

   public interface taskListGetInfoListener{
       void onLoaded(List<Task> task);
       void onError(String error);
   }

   public interface taskInfoListener{
       void onLoaded(Task task);
       void onError(String error);
   }

   public interface  completeTaskListener {
       void onSuccess();
       void onError(String error);
   }





}
