package com.justice.justiceapp.authentication;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.justice.justiceapp.R;
import com.justice.justiceapp.adapters.AuthenticationViewPagerAdapter;
import com.justice.justiceapp.authentication.fragments.SignInFragment;
import com.justice.justiceapp.authentication.fragments.SignUpFragment;

public class AuthenticationActivity extends AppCompatActivity {

    private static final String TAG = "AuthenticationActivity";
    public static final int SIGN_IN_FRAGMENT = 0;
    public static final int SIGN_UP_FRAGMENT = 1;

    private SignInFragment mSignInFragment;
    private SignUpFragment mSignUpFragment;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private AuthenticationViewPagerAdapter mAuthenticationViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        setUpViewPager();
    }

    private void setUpViewPager() {
        Log.d(TAG, "setUpViewPager: initializing views and setting up view pager");
        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);

        mAuthenticationViewPagerAdapter =
                new AuthenticationViewPagerAdapter(getSupportFragmentManager());

        mSignInFragment = new SignInFragment();
        mSignUpFragment = new SignUpFragment();

        mAuthenticationViewPagerAdapter.addFragment(mSignInFragment);
        mAuthenticationViewPagerAdapter.addFragment(mSignUpFragment);

        mViewPager.setAdapter(mAuthenticationViewPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);

        mTabLayout.getTabAt(SIGN_IN_FRAGMENT);
        mTabLayout.getTabAt(SIGN_UP_FRAGMENT);


    }
}
