package com.example.ecofriendly.data;

import com.example.ecofriendly.data.models.Task;
import com.example.ecofriendly.data.models.User;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Repository {

    public Repository() {}

    FirebaseFirestore db = FirebaseFirestore.getInstance();

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
                           listenner.onLoaded(user.getName(),user.getEmail(),String.valueOf(user.getPoints()),
                                   String.valueOf(user.getCompletedTask()), String.valueOf(user.getStreak()));
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
     * получение листика с задачами
     * @param listener
     */
   public void getTask( taskGetInfoListener listener ) {
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


   public interface userNameLoadedListenner{
       void onLoaded(String userString);
       void onError(String error);
   }

   public interface userGetInfoListenner{
       void onLoaded(String userName, String userEmail, String userPoints, String completedTask, String streak );
       void onError(String error);
   }

   public interface taskGetInfoListener{
       void onLoaded(List<Task> task);
       void onError(String error);
   }

}
