package com.tmz.razvan.mountainapp.Helpers;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class InternetHelper {

    public static boolean hasInternetAccess(Context context) {
//        if (isNetworkAvailable(context)) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected())
        {
            return false;
        }
        return true;

    }
}
