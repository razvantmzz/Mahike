package com.tmz.razvan.mountainapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tmz.razvan.mountainapp.R;
import com.tmz.razvan.mountainapp.models.BaseFeature;
import com.tmz.razvan.mountainapp.models.UserNote;

import java.util.List;

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

    private Context context;
    private Marker mMarker;
    private Marker mLastMarker;
    public CustomInfoWindowGoogleMap(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        mMarker = marker;
        BaseFeature userNote = (BaseFeature) marker.getTag();
        if(userNote == null)
        {
           return null;// getStandardView(marker);
        }

        return getNoteMarkerView(userNote);
    }

    private View getNoteMarkerView(final BaseFeature userNote)
    {
        View noteMarkerView = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.menu_map_marker, null);
        List<String> imagesList = userNote.getImageUrlList();

        TextView title = noteMarkerView.findViewById(R.id.tv_menu_marker_title);
        TextView description = noteMarkerView.findViewById(R.id.tv_menu_marker_description);

        title.setText(userNote.getTitle());
        description.setText(userNote.getContent());

        noteMarkerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        if(imagesList == null || imagesList.size() == 0)
        {
            RelativeLayout imagesContainer = noteMarkerView.findViewById(R.id.rl_menu_mark_images_container);
            imagesContainer.setVisibility(View.GONE);
            return noteMarkerView;
        }

        ImageView topLeft = noteMarkerView.findViewById(R.id.iv_top_left);
        ImageView topRight = noteMarkerView.findViewById(R.id.iv_top_right);
        ImageView bottomLeft = noteMarkerView.findViewById(R.id.iv_bottom_left);
        ImageView bottomRight = noteMarkerView.findViewById(R.id.iv_bottom_right);

        Callback imageWasLoadedCallback = new com.squareup.picasso.Callback() {
        @Override
        public void onSuccess() {
            //do smth when picture is loaded successfully
            if(mLastMarker != null && mLastMarker.getId().contains(mMarker.getId()))
            {
                return;
            }
            mLastMarker = mMarker;
            mMarker.showInfoWindow();
        }

        @Override
        public void onError(Exception e) {

        }
    };

        int i = 0;
        for (i = 0; i < imagesList.size(); i++)
        {
            switch (i)
            {
                case 0:
                    Picasso.get().load(imagesList.get(i)).resize(30, 30).placeholder(R.drawable.ic_load_image).into(topLeft, imageWasLoadedCallback);
                    break;
                case  1:
                    Picasso.get().load(imagesList.get(i)).resize(30, 30).placeholder(R.drawable.ic_load_image).into(topRight, imageWasLoadedCallback);
                    break;
                case 2:
                    Picasso.get().load(imagesList.get(i)).resize(30, 30).placeholder(R.drawable.ic_load_image).into(bottomLeft, imageWasLoadedCallback);
                    break;
                case 3:
                    Picasso.get().load(imagesList.get(i)).resize(30, 30).placeholder(R.drawable.ic_load_image).into(bottomRight, imageWasLoadedCallback);
                    break;
                default:
                    return noteMarkerView;
            }
        }

        return noteMarkerView;
    }

    private View getStandardView(Marker marker)
    {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.menu_standard_marker, null);


        TextView title = view.findViewById(R.id.tv_standard_marker_title);
        TextView description = view.findViewById(R.id.tv_standard_marker_details);

        title.setText(marker.getTitle());
        description.setText(marker.getSnippet());

        return view;
    }

}