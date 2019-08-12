package com.tmz.razvan.mountainapp.Core;

import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.auth.FirebaseUser;
import com.tmz.razvan.mountainapp.Core.Models.UserData;
import com.tmz.razvan.mountainapp.Helpers.FirebaseHelper;

public class UserCore {

    private static UserCore mUserCore;

    public UserCore()
    {
        User = new UserData();
    }

    public UserData User;

    private FirebaseUser FUser;

    public FirebaseUser getFUser() {
        return FUser;
    }

    public void setFUser(FirebaseUser user) {
        FUser = user;
    }

    private Boolean IsLoggedIn = false;

    public static UserCore Instance()
    {
        if(mUserCore == null)
        {
            mUserCore = new UserCore();
        }

        return mUserCore;
    }

    public UserData getUser() {
        return User;
    }

    public void setUser(UserData user) {
        User = user;
        FirebaseHelper.Instance().SyncUserData(User.getUserId());
    }

    public Boolean getLoggedIn() {
        return IsLoggedIn;
    }

    public void setLoggedIn(Boolean loggedIn) {
        IsLoggedIn = loggedIn;
    }
}
