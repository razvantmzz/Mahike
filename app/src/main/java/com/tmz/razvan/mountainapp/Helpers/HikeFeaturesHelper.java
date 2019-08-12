package com.tmz.razvan.mountainapp.Helpers;

import android.content.Context;
import android.graphics.Bitmap;

import com.tmz.razvan.mountainapp.R;
import com.tmz.razvan.mountainapp.enums.HikeFeatureType;

public class HikeFeaturesHelper {

    public static Bitmap GetImageBitmapByType(Context context, int type)
    {
        switch (type)
        {
            case HikeFeatureType.WATER_SOURCE:
                return BitmapHelper.getBitmapFromVectorDrawableId(context, R.drawable.ic_water_cource);
            case HikeFeatureType.SHELTER:
                return BitmapHelper.getBitmapFromVectorDrawableId(context, R.drawable.ic_shelter);
            case HikeFeatureType.CABIN:
                return BitmapHelper.getBitmapFromVectorDrawableId(context, R.drawable.ic_cabin);
            case HikeFeatureType.RESCUE_POINT:
                return BitmapHelper.getBitmapFromVectorDrawableId(context, R.drawable.ic_rescue_point);

        }
        return  BitmapHelper.getBitmapFromVectorDrawableId(context, R.drawable.ic_bookmark_black_24dp);
    }
}
