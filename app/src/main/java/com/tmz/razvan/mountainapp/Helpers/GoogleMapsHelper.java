package com.tmz.razvan.mountainapp.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tmz.razvan.mountainapp.R;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class GoogleMapsHelper {

    public static String MODE_WALKING = "walking";

    public static String getUrl(Context context, LatLng origin, LatLng dest, String directionMode)
    {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String mode = "mode=" + directionMode;
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters +
                "&key=" +  context.getString(R.string.google_maps_api_key);
        return url;
    }

    public static PolylineOptions getPolylineOptions(String serialisedLatLngArray)
    {
        Type listType = new TypeToken<ArrayList<LatLng>>(){}.getType();
        ArrayList<LatLng> coordList = new Gson().fromJson(serialisedLatLngArray, listType);
        return  new PolylineOptions().addAll(coordList);
    }
}
