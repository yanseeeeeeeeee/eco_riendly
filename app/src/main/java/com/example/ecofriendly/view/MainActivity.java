package com.example.ecofriendly.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecofriendly.R;
import com.example.ecofriendly.data.models.Badge;
import com.google.common.reflect.TypeToken;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.gson.Gson;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button signIn, signOn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        signIn = findViewById(R.id.signIn);
        signOn = findViewById(R.id.signOn);

        signIn.setOnClickListener(v -> {
//            uploadAllData();
            startActivity(new Intent(MainActivity.this,SignIn.class));
        });

        signOn.setOnClickListener(v -> {
            //uploadAllData();
            startActivity(new Intent(MainActivity.this,SignOn.class));
        });

    }


//    private String loadJsonFromAssets(String fileName) {
//        try {
//            InputStream inputStream = getAssets().open(fileName);
//            int size = inputStream.available();
//            byte[] buffer = new byte[size];
//            inputStream.read(buffer);
//            inputStream.close();
//            return new String(buffer, StandardCharsets.UTF_8);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
////    private List<Category> parseCategories() {
////        String json = loadJsonFromAssets("categories.json");
////
////        if (json == null || json.isEmpty()) {
////            return new ArrayList<>();
////        }
////
////        Type type = new TypeToken<List<Category>>() {}.getType();
////        return new Gson().fromJson(json, type);
////    }
////
////    private List<Task> parseTasks() {
////        String json = loadJsonFromAssets("tasks.json");
////
////        if (json == null || json.isEmpty()) {
////            return new ArrayList<>();
////        }
////
////        Type type = new TypeToken<List<Task>>() {}.getType();
////        return new Gson().fromJson(json, type);
////    }
//
//    private List<Badge> parseBages() {
//        String json = loadJsonFromAssets("bages.json");
//
//        if (json == null || json.isEmpty()) {
//            return new ArrayList<>();
//        }
//
//        Type type = new TypeToken<List<Badge>>() {}.getType();
//        return new Gson().fromJson(json,type);
//    }
//
//    private void uploadAllData() {
////        List<Category> categoryList = parseCategories();
////        List<Task> taskList = parseTasks();
//        List<Badge> bagesList = parseBages();
//
////        if (categoryList.isEmpty()) {
////            Log.e("MainActivity", "List with categories is empty");
////            return;
////        }
////
////        if (taskList.isEmpty()) {
////            Log.e("MainActivity", "List with tasks is empty");
////            return;
////        }
////
//        if (bagesList.isEmpty()) {
//            Log.e("MainActivity", "List with bages is empty");
//            return;
//        }
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        WriteBatch batch = db.batch();
//
////        CollectionReference categoriesRef = db.collection("categories");
////        CollectionReference tasksRef = db.collection("task");
//        CollectionReference bagesRef = db.collection("badges");
//
////        for (Category category : categoryList) {
////            if (category.getCategoryId() != null && !category.getCategoryId().isEmpty()) {
////                batch.set(categoriesRef.document(category.getCategoryId()), category);
////            }
////        }
////
////        for (Task task : taskList) {
////            if (task.getTaskId() != null && !task.getTaskId().isEmpty()) {
////                batch.set(tasksRef.document(task.getTaskId()), task);
////            }
////        }
//
//        for (Badge bages : bagesList) {
//            if (bages.getBadgeId() != null && !bages.getBadgeId().isEmpty()) {
//                batch.set(bagesRef.document(bages.getBadgeId()), bages);
//            }
//        }
//
//        batch.commit()
//                .addOnSuccessListener(unused -> {
//                    Toast.makeText(this, "Данные успешно загружены", Toast.LENGTH_LONG).show();
//                })
//                .addOnFailureListener(e -> {
//                    Toast.makeText(this, "Ошибка: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                });
//    }
}
