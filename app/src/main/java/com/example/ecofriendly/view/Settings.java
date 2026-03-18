package com.example.ecofriendly.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecofriendly.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;

public class Settings extends AppCompatActivity {

    private ImageButton back;
    private Button signOut;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        back = findViewById(R.id.back);
        signOut = findViewById(R.id.signOut);

        back.setOnClickListener(v -> finish());
        signOut.setOnClickListener(v ->{

            //открытие дилогового окна для подтверждения выхода
            new MaterialAlertDialogBuilder(this, R.style.RoundedMaterialDialog)
                    .setMessage("Выйти из аккаунта?")
                            .setPositiveButton("Да",((dialog, which) -> {
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(Settings.this, MainActivity.class));
                            }))
                    .setNegativeButton("Нет", ((dialog, which) -> dialog.dismiss()))
                    .show();

        });

    }
}
