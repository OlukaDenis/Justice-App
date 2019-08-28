package com.justice.justiceapp.authentication.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.justice.justiceapp.R;
import com.justice.justiceapp.authentication.AuthenticationActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "SignUpFragment";

    private TextInputEditText firstName;
    private TextInputEditText lastName;
    private TextInputEditText phoneNumber;
    private TextInputEditText emailAddress;
    private TextInputEditText password;


    private Button buttonSignUp;
    private FirebaseAuth auth;
    private TextView txt_Resend_Verification;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_sign_up,
                container, false);

        auth = FirebaseAuth.getInstance();

        firstName = view.findViewById(R.id.signUpFirstName);
        lastName = view.findViewById(R.id.signUpLastName);
        phoneNumber = view.findViewById(R.id.signUpPhoneNumber);
        emailAddress = view.findViewById(R.id.signUpEmail);
        password = view.findViewById(R.id.signUpPassword);

        buttonSignUp = view.findViewById(R.id.buttonSignUp);
        buttonSignUp.setOnClickListener(this);

        txt_Resend_Verification = view.findViewById(R.id.txt_resend);
        txt_Resend_Verification.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick: sign up option");

        int viewId = view.getId();
        switch (viewId){
            case R.id.buttonSignUp:
                signUp();
                break;

            case R.id.txt_resend:
                //resendVerification();
                break;
        }

    }

    private void resendVerification() {
        if(auth.getCurrentUser() != null){
            auth.getCurrentUser().reload(); // reloads user fields, like emailVerified:
            if (!auth.getCurrentUser().isEmailVerified()) {
                auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getContext(), "Verification Email Resent", Toast.LENGTH_LONG).show();
                        }
                        else
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                //startActivity(new Intent(getContext(), LoginActivity.class));
                Toast.makeText(getContext(), "Login to your account", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void signUp() {
        final String email = emailAddress.getText().toString().trim();
        final String mpassword = password.getText().toString().trim();
        final String number = phoneNumber.getText().toString().trim();
        final String fname = firstName.getText().toString().trim();
        final String lname = lastName.getText().toString().trim();

        if (TextUtils.isEmpty(fname)) {
            Toast.makeText(getContext(), "Enter your first name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(lname)) {
            Toast.makeText(getContext(), "Enter your last name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(number)) {
            Toast.makeText(getContext(), "Enter your phone number!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(mpassword)) {
            Toast.makeText(getContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(getContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }


        //create user
        auth.createUserWithEmailAndPassword(email, mpassword)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (task.isSuccessful()){
                            auth.getCurrentUser().sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                                Toast.makeText(getContext(),
                                                        "User with this email already exist.", Toast.LENGTH_SHORT).show();
                                            }

                                            else if (task.isSuccessful()){
                                                Toast.makeText(getContext(), "Registered successfully. Please check your email for verification ", Toast.LENGTH_SHORT).show();
                                                emailAddress.setText("");
                                                password.setText("");
                                                firstName.setText("");
                                                lastName.setText("");
                                                phoneNumber.setText("");
                                                //startActivity(new Intent(getContext(), LoginActivity.class));
                                            }
                                            else{
                                                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            }

                                        }
                                    });
                        }

                    }
                });
    }
}



