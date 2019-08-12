package com.tmz.razvan.mountainapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tmz.razvan.mountainapp.Core.Models.UserData;
import com.tmz.razvan.mountainapp.Core.UserCore;
import com.tmz.razvan.mountainapp.Helpers.InternetHelper;
import com.tmz.razvan.mountainapp.R;
import com.tmz.razvan.mountainapp.TranslationConstants;
import com.tmz.razvan.mountainapp.baseClasses.BaseAppCompat;
import com.tmz.razvan.mountainapp.fragments.FragmentHome;
import com.tmz.razvan.mountainapp.fragments.FragmentMenu;
import com.tmz.razvan.mountainapp.fragments.FragmentSearch;
import com.tmz.razvan.mountainapp.models.Coordinates;
import com.tmz.razvan.mountainapp.models.HikeCoordinates;
import com.tmz.razvan.mountainapp.models.HikeModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HomeActivity extends BaseAppCompat {

    private BottomNavigationView mNavigation;
    private FragmentHome mFragmentHome;
    private FragmentSearch mFragmentSearch;
    private FragmentMenu mFragmentMenu;
    private DatabaseReference myRef;
    private List<HikeModel> mHikeModelList;
    private boolean isOffline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if(!InternetHelper.hasInternetAccess(this))
        {
            isOffline = true;
            showNoInternerText(true);
            InstantiateFragments();
            mNavigation = (BottomNavigationView) findViewById(R.id.navigation);
            mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

            SetStrings();
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Toast.makeText(this, user.getEmail(), Toast.LENGTH_SHORT).show();

        InstantiateFragments();

        mNavigation = (BottomNavigationView) findViewById(R.id.navigation);
        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        SetStrings();

    }

    @Override
    public void onBackPressed() {
        //Include the code here
        return;
    }
    //region private methods

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    showNoInternerText(true);
                    fragmentTransaction.replace(R.id.fragment_container, mFragmentHome);
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_search:
                    showNoInternerText(false);
                    fragmentTransaction.replace(R.id.fragment_container, mFragmentSearch);
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_menu:
                    showNoInternerText(false);
                    fragmentTransaction.replace(R.id.fragment_container, mFragmentMenu);
                    fragmentTransaction.commit();
                    return true;
            }
            return false;
        }
    };

    private void showNoInternerText(boolean show)
    {
        if(isOffline)
        {
            TextView offText = findViewById(R.id.offlineText);
            offText.setText(TranslationConstants.OFFLINE_TEXT);
            offText.setVisibility(show ? View.VISIBLE : View.GONE);
        }

    }

    private void InstantiateFragments()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        mFragmentHome = new FragmentHome();
        mFragmentSearch = new FragmentSearch();
        mFragmentMenu = new FragmentMenu();

        fragmentTransaction.add(R.id.fragment_container, mFragmentHome);
        fragmentTransaction.commit();

    }

    private void SetStrings()
    {
        mNavigation.getMenu().getItem(0).setTitle(TranslationConstants.HOME);
        mNavigation.getMenu().getItem(1).setTitle(TranslationConstants.SEARCH);
        mNavigation.getMenu().getItem(2).setTitle(TranslationConstants.MENU);

    }

    //endregion
}
