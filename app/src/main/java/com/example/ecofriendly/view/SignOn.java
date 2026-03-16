package com.example.ecofriendly.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecofriendly.R;
import com.example.ecofriendly.data.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignOn extends AppCompatActivity {

    EditText email, name, password, forPassword;
    Button button;
    ImageButton back;
    TextView signIn;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_on);

        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        forPassword = findViewById(R.id.password_toggle);
        button = findViewById(R.id.button);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        back = findViewById(R.id.back);
        signIn = findViewById(R.id.signIn);

        button.setOnClickListener(v -> registerUser());
        back.setOnClickListener(v -> finish());

        signIn.setOnClickListener(v -> {
            startActivity(new Intent(this, SignIn.class));
        });

    }

    /**
     * Метод для регистрации пользователей в системе
     */
    private void registerUser() {

        String Semail = email.getText().toString().trim();
        String Sname = name.getText().toString().trim();
        String Spassword = password.getText().toString().trim();
        String Sforpassword = forPassword.getText().toString().trim();

        //пустые поля
        if (Semail.isEmpty() || Sname.isEmpty() || Spassword.isEmpty()||Sforpassword.isEmpty()) {
            Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_SHORT).show();
        }

        //проверка почты
        if (!Patterns.EMAIL_ADDRESS.matcher(Semail).matches()){
            Toast.makeText(this, "Неккоректный email", Toast.LENGTH_SHORT).show();
        }

        //проверка совпадения паролей

        //создание пользователя в системе firestore
        mAuth.createUserWithEmailAndPassword(Semail, Spassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String uid = firebaseUser.getUid();

                            User user = new User(uid, Semail, Sname);

                            if (firebaseUser != null) {
                                Log.d("SignOn", "Регистрация успешна"+firebaseUser.getUid());

                                db.collection("users")
                                        .document(uid)
                                        .set(user)
                                        .addOnSuccessListener(unused -> {
                                            Log.d("SignOn", "Регистрация прошла успешно");

                                            startActivity(new Intent(SignOn.this, FragmentContainer.class));
                                            finish();
                                        });

                            } else {
                                Log.e("SignOn", "FirebaseUser==null после успешной регистрации");

                                Toast.makeText(SignOn.this,
                                        "Не удалось зарегестрироваться.",
                                        Toast.LENGTH_SHORT).show();

                                }

                        } else {
                            Exception e = task.getException();
                                if (e !=null) {
                                    Log.e("SignOn", "Ошибка регистрации", e);
                                }

                                Toast.makeText(SignOn.this,
                                        "Не удалось зарегестрироваться.",
                                        Toast.LENGTH_SHORT).show();
                            }
                    }
                });
    }



}
