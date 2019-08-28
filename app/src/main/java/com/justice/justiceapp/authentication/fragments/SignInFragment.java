package com.justice.justiceapp.authentication.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.justice.justiceapp.MainActivity;
import com.justice.justiceapp.R;
import com.justice.justiceapp.authentication.ForgotPasswordActivity;

import java.util.concurrent.Executor;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "SignInFragment";

    private TextInputEditText userNameOrEmail;
    private TextInputEditText userPassword;
    private static final int RC_SIGN_IN = 433;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private ProgressBar progressBar;
    private String emailReset;

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

        mAuth = FirebaseAuth.getInstance();

        userNameOrEmail = view.findViewById(R.id.signInUsernameOrEmail);
        userPassword = view.findViewById(R.id.signInPassword);

        buttonForgotPassword = view.findViewById(R.id.buttonForgotPassword);
        buttonLogin = view.findViewById(R.id.buttonLogin);

        buttonForgotPassword.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        if (mAuth.getCurrentUser() != null && mAuth.getCurrentUser().isEmailVerified()) {
            startActivity(new Intent(getContext(), MainActivity.class));
            getActivity().finish();
        }
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
                signIn();
                break;
            }
            case R.id.loginWithGoogleButton: {
                Log.d(TAG, "onClick: google login");
                googleLogin();
                break;
            }
        }

    }

    public void signIn(){
        String email = userNameOrEmail.getText().toString();
        final String password = userPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        //authenticate user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the mAuth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            // there was an error
                            if (password.length() < 6) {
                                userPassword.setError(getString(R.string.minimum_password));
                            } else {
                                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            if (mAuth.getCurrentUser().isEmailVerified()){
                                Intent intent = new Intent(getContext(), MainActivity.class);
                                startActivity(intent);
                                getActivity().overridePendingTransition(android.R.anim.fade_in,
                                        android.R.anim.fade_out);
                                getActivity().finish();

                            }
                            else {
                                Toast.makeText(getContext(), "Please verify your email address ", Toast.LENGTH_LONG).show();
                            }


                        }
                    }
                });
    }

    private void googleLogin() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication Failed", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });
    }

    @Override
    public void onStart() {
        // Check if user is signed in (non-null) and update UI accordingly.
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {

            if (user.isEmailVerified()){

                Intent intent = new Intent(getContext(),MainActivity.class);
                startActivity(intent);
                Toast.makeText(getContext(), "Logged in", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getContext(), "Check your Email for verification link", Toast.LENGTH_SHORT).show();

            }

        }
        else {
            //Intent intent = new Intent(getContext(),LoginActivity.class);
           // startActivity(intent);
        }
    }
}
