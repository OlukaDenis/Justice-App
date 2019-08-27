package com.justice.justiceapp.adapters;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sirikye Brian on 8/20/2019.
 * bryanmuloni@gmail.com
 */
public class AuthenticationViewPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = AuthenticationViewPagerAdapter.class.getSimpleName();
    private final List<Fragment> mFragments = new ArrayList<>();
    private final String[] titles = new String[]{"Sign In", "Sign Up"};

    public AuthenticationViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, "getItem: finding the position of the fragment");
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        Log.d(TAG, "getCount: getting the size of the fragments");
        return mFragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    public void addFragment(Fragment fragment) {
        Log.d(TAG, "addFragment: adding fragments");
        mFragments.add(fragment);
    }
}
