package com.example.ecofriendly.view;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecofriendly.R;
import com.example.ecofriendly.adapters.BadgesAdapter;
import com.example.ecofriendly.data.Repository;
import com.example.ecofriendly.data.models.Badge;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CompletedBages extends BaseActivity{

    private ImageButton back;

    private RecyclerView recyclerView;
    private BadgesAdapter adapter;

    private List<Badge> list;
    private  FirebaseAuth mAuth;
    String uid;
    private Repository repository;


    @Override
    protected int getLayoutId() {
        return R.layout.completed_bages;
    }

    @Override
    protected int getRootViewId() {
        return R.id.root;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        back = findViewById(R.id.back);
        recyclerView = findViewById(R.id.bages);

        repository = new Repository();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user =  mAuth.getCurrentUser();

        uid = "";
        if (user != null) {
            uid = user.getUid();
        }

        list = new ArrayList<>();
        adapter = new BadgesAdapter(list, this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
        loadedList();

        back.setOnClickListener(v -> finish());

    }


    /**
     * Метод для подгрузки листа
     */
    public void loadedList() {
        repository.getListBagesUser(uid, new Repository.bagesUserListListener() {
            @Override
            public void onLoaded(List<Badge> badgeList) {
                adapter.updateList(badgeList);
            }

            @Override
            public void onError(String error) {
                Log.e("CompletedBages", "Error:"+error);
            }
        });
    }


}
