package com.example.ecofriendly.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.example.ecofriendly.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;

public class Settings extends BaseActivity {

    private ImageButton back;
    private Button signOut;

    @Override
    protected int getLayoutId() {
        return R.layout.settings;
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
