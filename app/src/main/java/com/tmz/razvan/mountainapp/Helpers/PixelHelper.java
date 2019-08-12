package com.tmz.razvan.mountainapp.Helpers;

import android.content.res.Resources;
import android.util.DisplayMetrics;

public class PixelHelper {

    public static int dpToPixel(float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }
}
