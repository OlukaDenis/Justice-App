package com.justice.justiceapp.authentication.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.justice.justiceapp.MainActivity;
import com.justice.justiceapp.R;
import com.justice.justiceapp.authentication.ForgotPasswordActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "SignInFragment";

    private TextInputEditText userNameOrEmail;
    private TextInputEditText userPassword;

    private Button buttonForgotPassword;
    private Button buttonLogin;


    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_sign_in,
                container, false);

        userNameOrEmail = view.findViewById(R.id.signInUsernameOrEmail);
        userPassword = view.findViewById(R.id.signInPassword);

        buttonForgotPassword = view.findViewById(R.id.buttonForgotPassword);
        buttonLogin = view.findViewById(R.id.buttonLogin);

        buttonForgotPassword.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.buttonForgotPassword: {
                Log.d(TAG, "onClick: forgot password option");
                startActivity(new Intent(getActivity(), ForgotPasswordActivity.class));
                break;
            }
            case R.id.buttonLogin: {
                Log.d(TAG, "onClick: login option");
//                Todo: Login logic will be here
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                getActivity().finish();

                break;
            }
        }

    }
}
