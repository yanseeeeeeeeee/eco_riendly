package com.example.ecofriendly.data;

import com.example.ecofriendly.data.models.Bage;
import com.example.ecofriendly.data.models.Task;
import com.example.ecofriendly.data.models.User;
import com.example.ecofriendly.data.models.UserBage;
import com.example.ecofriendly.data.models.UserTask;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Repository {

    public Repository() {}

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    GameRepository gameRepository = new GameRepository();

    /**
     * Метод для получения имени пользователя
     * @param uid
     * @param listenner
     */
   public void getUserName(String uid, userNameLoadedListenner listenner) {
       db.collection("users")
               .document(uid)
               .get()
               .addOnSuccessListener(documentSnapshot -> {

                   if (documentSnapshot.exists()) {
                       String userName = documentSnapshot.getString("name");
                       listenner.onLoaded(userName);
                   } else {
                       listenner.onError("Пользователь не найден");
                   }

               })
               .addOnFailureListener( e -> {
                   listenner.onError(e.getMessage());
               });

   }

    /**
     * Метод для получения email пользователя
     * @param uid
     * @param listenner
     */
   public void getUserEmail(String uid, userNameLoadedListenner listenner) {
       db.collection("users")
               .document(uid)
               .get()
               .addOnSuccessListener(documentSnapshot -> {

                   if (documentSnapshot.exists()) {
                       String userEmail = documentSnapshot.getString("email");
                       listenner.onLoaded(userEmail);
                   } else {
                       listenner.onError("Пользователь не найден");
                   }
               })
               .addOnFailureListener(e -> {
                   listenner.onError(e.getMessage());
               });
   }

    /**
     * Метод для получения полной модели пользователя
     * @param uid
     * @param listenner
     */
   public void getUser(String uid, userGetInfoListenner listenner) {
       db.collection("users")
               .document(uid)
               .get()
               .addOnSuccessListener(documentSnapshot -> {

                   if (documentSnapshot.exists()) {
                       User user = documentSnapshot.toObject(User.class);
                       if (user != null) {
                           listenner.onLoaded(user);
                       } else {
                           listenner.onError("Пользователь не найден");
                       }
                   }
               })
               .addOnFailureListener(e -> {
                   listenner.onError("Error:" + e.getMessage());
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

               batch.commit().addOnSuccessListener(unused -> {
                   //действие с бейджами
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
    public void setBagesForUser(String uid, Bage bage, completeTaskListener listener ) {

        Map<String, Object> bageMap = new HashMap<>();
        bageMap.put("bageId", bage.getUid());
        bageMap.put("receivedAt", FieldValue.serverTimestamp());

        db.collection("users")
                .document("uid")
                .collection("user_bages")
                .document(bage.getUid())
                .set(bageMap)
                .addOnSuccessListener(unused -> {
                    listener.onSuccess();
                })
                .addOnFailureListener(e -> listener.onError(e.getMessage()));

    }

//    public void getListBagesUser(String uid, bagesUserListListener listener) {
//        db.collection("users")
//                .document(uid)
//
//    }




    public interface  bagesUserListListener{
        void onLoaded(List<UserBage> userBageList);
        void onError(String error);
    }
   public interface userNameLoadedListenner{
       void onLoaded(String userString);
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
