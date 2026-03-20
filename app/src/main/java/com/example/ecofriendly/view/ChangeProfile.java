package com.example.ecofriendly.view;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.example.ecofriendly.R;
import com.example.ecofriendly.data.Repository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangeProfile extends BaseActivity {

    private ImageButton back;
    private EditText inputName;
    private Button save;
    FirebaseAuth mAuth;
    String uid;
    Repository repository;

    @Override
    protected int getLayoutId() {
        return R.layout.change_profile;
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

        inputName = findViewById(R.id.inputName);
        save = findViewById(R.id.save);
        back = findViewById(R.id.back);
        repository = new Repository();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        uid = "";

        if (firebaseUser != null) {
            uid = firebaseUser.getUid();
        }


        back.setOnClickListener(v -> finish());
        save.setOnClickListener(v -> updateName() );
    }

    private void updateName() {

        String newName = inputName.getText().toString().trim();

        if (newName.isEmpty()) {
            Toast.makeText(this, "Нет изменений", Toast.LENGTH_SHORT).show();
        } else {

            repository.updateUserName(uid, newName, new Repository.updateUserListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(ChangeProfile.this, "Данные успешно обновлены!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(String error) {
                    Log.e("ChangeProfile", "Error: " +error);
                }
            });

        }

    }


}
