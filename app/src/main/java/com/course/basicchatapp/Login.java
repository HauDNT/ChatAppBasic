package com.course.basicchatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    Button btnLogin;
    EditText email, password;
    FirebaseAuth auth;
    String emailPattern = "/^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        btnLogin = findViewById(R.id.btnLogin);
        email = findViewById(R.id.edtEmail);
        password = findViewById(R.id.edtPassword);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();

                if ((TextUtils.isEmpty(userEmail))) {
                    Toast.makeText(Login.this, "Enter your email", Toast.LENGTH_SHORT).show();
                }
                else if ((TextUtils.isEmpty(userPassword))) {
                    Toast.makeText(Login.this, "Enter your password", Toast.LENGTH_SHORT).show();
                }
                else if (!userEmail.matches(emailPattern)){
                    email.setError("Give proper email address");
                } else if (password.length() < 6) {
                    password.setError("Mote than 6 characters with password");
                    Toast.makeText(Login.this, "Password needs to be longer than 6 characters", Toast.LENGTH_SHORT).show();
                } else {
                    auth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                try {
                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } catch (Exception e) {
                                    Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}