package com.justice.justiceapp.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.justice.justiceapp.R;

public class ForgotPasswordActivity extends AppCompatActivity {
    private static final String TAG = "ForgotPasswordActivity";

    private TextInputEditText newPassword;
    private TextInputEditText confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        newPassword = findViewById(R.id.recoveryNewPassword);
        confirmPassword = findViewById(R.id.recoveryConfirmPassword);
    }

    public void initiatePasswordRecovery(View view) {
        Log.d(TAG, "initiatePasswordRecovery: password recovery");

//        Todo: Password recovery logic will be here and user should be taken to sign in fragment
        startActivity(new Intent(this, AuthenticationActivity.class));
        finish();
    }
}
