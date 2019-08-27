package com.justice.justiceapp.authentication.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
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

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_sign_up,
                container, false);

        firstName = view.findViewById(R.id.signUpFirstName);
        lastName = view.findViewById(R.id.signUpLastName);
        phoneNumber = view.findViewById(R.id.signUpPhoneNumber);
        emailAddress = view.findViewById(R.id.signUpEmail);
        password = view.findViewById(R.id.signUpPassword);

        buttonSignUp = view.findViewById(R.id.buttonSignUp);
        buttonSignUp.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick: sign up option");

        int viewId = view.getId();
        switch (viewId){
            case R.id.buttonSignUp:
                //        Todo: Sign up logic will be here User should be taken to sign in fragment
                break;
            }
        }
    }

