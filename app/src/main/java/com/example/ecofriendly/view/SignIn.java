package com.example.ecofriendly.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.example.ecofriendly.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends BaseActivity {

    EditText EDemail, EDpassword;
    Button button;
    ImageButton back;
    TextView signOn;
    FirebaseAuth mAuth;

    @Override
    protected int getLayoutId() {
        return R.layout.sign_in;
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

        EDemail = findViewById(R.id.email);
        EDpassword = findViewById(R.id.password);
        button = findViewById(R.id.button);
        back = findViewById(R.id.back);
        signOn = findViewById(R.id.register_user);

        mAuth = FirebaseAuth.getInstance();

        button.setOnClickListener(v -> loginUser());
        back.setOnClickListener(v -> finish());

        signOn.setOnClickListener(v -> {
            startActivity(new Intent(this, SignOn.class));
        });

    }


    /**
     * Метод для входа пользователя в систему при помощи пароля и почты
     */
    private void loginUser() {

        String email = EDemail.getText().toString().trim();
        String password = EDpassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Заполните пустые поля", Toast.LENGTH_SHORT).show();
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            Log.d("SignIn", "Вход выполнен успешно");

                            startActivity(new Intent(SignIn.this, FragmentContainer.class));
                            finish();
                        } else {
                            Exception e = task.getException();
                            if (e!=null) {
                                Log.e("SignIn", "Ошибка входа" , e);
                            }
                            Toast.makeText(SignIn.this, "Не удалось войти",
                                            Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
