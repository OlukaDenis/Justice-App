package com.justice.justiceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignupActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private Button btnSignUp;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private TextView txt_Resend_Verification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        auth = FirebaseAuth.getInstance();

        btnSignUp = findViewById(R.id.sign_up_button);
        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        txt_Resend_Verification = findViewById(R.id.txt_resend);

        txt_Resend_Verification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(auth.getCurrentUser() != null){
                    auth.getCurrentUser().reload(); // reloads user fields, like emailVerified:
                    if (!auth.getCurrentUser().isEmailVerified()) {
                        auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(SignupActivity.this, "Verification Email Resent", Toast.LENGTH_LONG).show();
                                }
                                else
                                    Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                        Toast.makeText(SignupActivity.this, "Login to your account", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (task.isSuccessful()){
                                    auth.getCurrentUser().sendEmailVerification()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                                        Toast.makeText(SignupActivity.this,
                                                                "User with this email already exist.", Toast.LENGTH_SHORT).show();
                                                    }

                                                    else if (task.isSuccessful()){
                                                        Toast.makeText(SignupActivity.this, "Registered successfully. Please check your email for verification ", Toast.LENGTH_SHORT).show();
                                                        inputEmail.setText("");
                                                        inputPassword.setText("");
                                                        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                                    }
                                                    else{
                                                        Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                    }

                                                }
                                            });
                                }
//                                if (!task.isSuccessful()) {
//                                    Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
//                                            Toast.LENGTH_SHORT).show();
//                                } else {
//                                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
//                                    finish();
//                                }
                            }
                        });

            }
        });
    }
}
