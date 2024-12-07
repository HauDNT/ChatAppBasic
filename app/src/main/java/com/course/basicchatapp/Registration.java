package com.course.basicchatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class Registration extends AppCompatActivity {
    Button btnRegister;
    EditText edtEmail, edtPassword, edtRePass;
    TextView labelLogin;
    CircleImageView profileImage;
    FirebaseAuth auth;
    Uri imageUri;
    String imageUriString;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseDatabase fbDatabase;
    FirebaseStorage fbStorage;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Establishing The Account");
        progressDialog.setCancelable(false);

        auth = FirebaseAuth.getInstance();
        fbDatabase = FirebaseDatabase.getInstance();
        fbStorage = FirebaseStorage.getInstance();
        btnRegister = findViewById(R.id.btnRegister);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtRePass = findViewById(R.id.edtRePassword);
        labelLogin = findViewById(R.id.labelSignupRedirect);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = edtEmail.getText().toString();
                String userPassword = edtPassword.getText().toString();
                String userRePass = edtRePass.getText().toString();
                String status = "Success";

                if (TextUtils.isEmpty(userEmail)) {
                    progressDialog.dismiss();
                    Toast.makeText(Registration.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                }
                else if (!userEmail.matches(emailPattern)) {
                    progressDialog.dismiss();
                    edtEmail.setError("Type a valid email here!");
                }
                else if (userPassword.length() < 6) {
                    progressDialog.dismiss();
                    edtPassword.setError("The password doesn't match");
                }
                else {
                    auth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String userId = task.getResult().getUser().getUid();
                                DatabaseReference dbReference = fbDatabase.getReference().child("user").child(userId);
                                StorageReference storageReference = fbStorage.getReference().child("Upload").child(userId);

                                if (imageUri != null) {
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            imageUriString = uri.toString();
                                            Users users = new Users(userId, userEmail, userPassword, imageUriString,  status);
                                            dbReference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        progressDialog.show();
                                                        Intent intent = new Intent(Registration.this, Login.class);
                                                        startActivity(intent);
                                                        finish();
                                                    } else {
                                                        Toast.makeText(Registration.this, "Error when creating user", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
            }
        });
    }
}