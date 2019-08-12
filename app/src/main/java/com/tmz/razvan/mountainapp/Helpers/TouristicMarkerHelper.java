package com.tmz.razvan.mountainapp.Helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.ImageView;

import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;
import com.tmz.razvan.mountainapp.R;

public class TouristicMarkerHelper {

    public static Bitmap getMarkerByType(Context context, int type)
    {
        ImageView imageView = new ImageView(context);

        int drawable = getDrawablebyType(type);

        VectorChildFinder vector = new VectorChildFinder(context, drawable, imageView);

        VectorDrawableCompat.VFullPath path1 = vector.findPathByName(context.getString(R.string.marker_background));
        path1.setFillColor(getColorByType(type));

//        VectorDrawableCompat.VGroup group1 = vector.findGroupByName("group1");
//        group1.setTranslateX(10);

        imageView.invalidate();
        Bitmap markerImage = BitmapHelper.getBitmapFromVectorDrawable(context, imageView.getDrawable());
        return markerImage;
    }

    public static int getDrawablebyType(int type)
    {
        type = type /10;
        switch (type)
        {
            case 0:
            case 1:
                return R.drawable.ic_marker_line;
            case 2:
                return R.drawable.ic_marker_triangle;
            case 3:
                return R.drawable.ic_marker_circle;
            case 4:
                return R.drawable.ic_marker_square;
            case 5:
                return R.drawable.ic_marker_cross;
        }
        return -1;
    }

    public static int getColorByType(int color)
    {
        color = color %10;
        switch (color)
        {
            case 1:
                return Color.RED;
            case 2:
                return Color.BLUE;
            case 3:
                return Color.YELLOW;

        }
        return Color.WHITE;
    }
}
