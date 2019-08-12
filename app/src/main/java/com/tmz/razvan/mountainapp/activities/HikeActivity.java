package com.tmz.razvan.mountainapp.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.widget.NestedScrollView;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tmz.razvan.mountainapp.Adapters.MainSliderAdapter;
import com.tmz.razvan.mountainapp.Constants.NavigationContants;
import com.tmz.razvan.mountainapp.Core.CoreData;
import com.tmz.razvan.mountainapp.Core.UserCore;
import com.tmz.razvan.mountainapp.Helpers.FetchURL;
import com.tmz.razvan.mountainapp.Helpers.FirebaseHelper;
import com.tmz.razvan.mountainapp.Helpers.GoogleMapsHelper;
import com.tmz.razvan.mountainapp.Helpers.TaskLoadedCallback;
import com.tmz.razvan.mountainapp.Helpers.TouristicMarkerHelper;
import com.tmz.razvan.mountainapp.R;
import com.tmz.razvan.mountainapp.baseClasses.BaseAppCompat;
import com.tmz.razvan.mountainapp.models.HikeModel;
import com.tmz.razvan.mountainapp.models.UserHikeData;
import com.tmz.razvan.mountainapp.services.PicassoImageLoadingService;

import java.lang.reflect.Type;
import java.text.Format;
import java.util.ArrayList;

import ss.com.bannerslider.Slider;

public class HikeActivity extends BaseAppCompat implements OnMapReadyCallback, TaskLoadedCallback {

    private Context mContext;
    private HikeModel mHikeModel;
    private UserHikeData mUserHikeData;

    private Slider mSliderView;
    private TextView mHeaderTextView;
    private TextView mDurationTextView;
    private TextView mAvailabilityTextView;
    private TextView mDificultyTextView;
    private TextView mDetailsHeaderTextView;
    private TextView mDetailsTextView;
    private ToggleButton mFavoritesButton;
    private GoogleMap mMap;
    private Polyline currentPolyline;

    private Boolean mIsUserFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike);
        String serialisedHikeModel = getIntent().getStringExtra(NavigationContants.HIKE_MODEL_KEY);
        mHikeModel = new Gson().fromJson(serialisedHikeModel, HikeModel.class);
        mUserHikeData = UserCore.Instance().User.getPrivateHikeDataById(mHikeModel.getId());
        mIsUserFavorite = mUserHikeData.getFavorite();
        CoreData.getInstance().addHikeToCache(mHikeModel);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        InitialiseViews();
        SetStrings();
        SetUpViews();
        SetUpImageSlider();
        InitToolbarAnimation();
    }


    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        LatLng startPoint = new LatLng(mHikeModel.getCoordinates().getStartPoint().getLatitude(), mHikeModel.getCoordinates().getStartPoint().getLongitude());
        LatLng endPoint = new LatLng(mHikeModel.getCoordinates().getEndPoint().getLatitude(), mHikeModel.getCoordinates().getEndPoint().getLongitude());
//
//        mMap.addMarker(new MarkerOptions().position(startPoint).title("Start"));
//        mMap.addMarker(new MarkerOptions().position(endPoint).title("End"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(startPoint));
        mMap.getUiSettings().setAllGesturesEnabled(false);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng arg0)
            {
                Intent intent = new Intent(mContext, HikeMapActivity.class);
                String serialisedHikeModel = new Gson().toJson(mHikeModel);
                intent.putExtra(NavigationContants.HIKE_MODEL_KEY, serialisedHikeModel);
                mContext.startActivity(intent);
            }
        });

        String urll = GoogleMapsHelper.getUrl(this, startPoint, endPoint, GoogleMapsHelper.MODE_WALKING);
        new FetchURL(HikeActivity.this).execute(urll, GoogleMapsHelper.MODE_WALKING);


        if(mHikeModel.getPolyline() == null || mHikeModel.getPolyline().isEmpty())
        {
            String url = GoogleMapsHelper.getUrl(this, startPoint, endPoint, GoogleMapsHelper.MODE_WALKING);
            new FetchURL(HikeActivity.this).execute(url, GoogleMapsHelper.MODE_WALKING);
            return;
        }

        Type listType = new TypeToken<ArrayList<LatLng>>(){}.getType();
        ArrayList<LatLng> coordList = new Gson().fromJson(mHikeModel.getPolyline(), listType);
        mMap.addPolyline(new PolylineOptions().addAll(coordList));
        addStartFInishMarkers();
    }

    //region private methods

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();

        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
        mHikeModel.setPolyline(new Gson().toJson(currentPolyline.getPoints()));
        FirebaseHelper.Instance().SyncHikesData(mHikeModel);

        addStartFInishMarkers();
        /**call the map call back to know map is loaded or not*/

    }

    private void addStartFInishMarkers() {

        final LatLng startPoint = new LatLng(mHikeModel.getCoordinates().getStartPoint().getLatitude(), mHikeModel.getCoordinates().getStartPoint().getLongitude());
        final LatLng endPoint = new LatLng(mHikeModel.getCoordinates().getEndPoint().getLatitude(), mHikeModel.getCoordinates().getEndPoint().getLongitude());
        Bitmap bitmap = TouristicMarkerHelper.getMarkerByType(this, mHikeModel.getMarkType());
        mMap.addMarker(new MarkerOptions().position(startPoint).title(mHikeModel.getCoordinates().getStartPoint().getName()).icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
        mMap.addMarker(new MarkerOptions().position(endPoint).title(mHikeModel.getCoordinates().getEndPoint().getName()).icon(BitmapDescriptorFactory.fromBitmap(bitmap)));

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(startPoint);
        builder.include(endPoint);
        final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(builder.build(), 100);
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                /**set animated zoom camera into map*/
                mMap.moveCamera(cu);// animateCamera(cu);
            }
        });
    }

    private void SetUpViews()
    {
        mFavoritesButton.setChecked(mIsUserFavorite);
        mFavoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(((ToggleButton)v).isChecked() && mUserHikeData != null)
                {
                    mUserHikeData.setFavorite(true);
                }
                else
                {
                    mUserHikeData.setFavorite(false);
                }
                FirebaseHelper.Instance().SyncUserData();
            }

        });
    }

    private void InitialiseViews()
    {
        mContext = this;
        mSliderView = findViewById(R.id.sld_activity_hike);
        mHeaderTextView = findViewById(R.id.tv_activity_hike_name_and_location);
        mDurationTextView = findViewById(R.id.tv_activity_hike_duration);
        mAvailabilityTextView = findViewById(R.id.tv_activity_hike_availability);
        mDificultyTextView = findViewById(R.id.tv_activity_hike_difficulty);
        mDetailsHeaderTextView = findViewById(R.id.tv_activity_hike_details_header);
        mDetailsTextView = findViewById(R.id.tv_activity_hike_details);
        mFavoritesButton = findViewById(R.id.toolbar_favorites_button);
    }

    private void SetStrings()
    {
        mHeaderTextView.setText(mHikeModel.getName());
        mDurationTextView.setText(mHikeModel.getDuration());
        mAvailabilityTextView.setText(mHikeModel.getAvailability());
        mDificultyTextView.setText(String.format("%s", mHikeModel.getDifficulty()));
        mDetailsHeaderTextView.setText("About");
        mDetailsTextView.setText(mHikeModel.getDetails());
    }

    private void SetUpImageSlider()
    {
        Slider.init(new PicassoImageLoadingService(this));
        mSliderView = findViewById(R.id.sld_activity_hike);
        mSliderView.setAdapter(new MainSliderAdapter(mHikeModel.getImageUrl()));
    }

    private void InitToolbarAnimation() {
        NestedScrollView scrollView = findViewById(R.id.sv_hike_activity_container);
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView nestedScrollView, int i, int i1, int i2, int i3) {
                if(i1 == 0.0f)
                {
                    Toolbar.setAlpha(1.0f);
                    Toolbar.setEnabled(true);
                    mFavoritesButton.setEnabled(true);
                }
                else
                {
                    float alpha = 1.0f - (i1 / 3)*0.01f;
                    if(alpha < 0.5f)
                    {
                        Toolbar.setEnabled(false);
                        mFavoritesButton.setEnabled(false);
                    }
                    Toolbar.setAlpha(alpha);
                }
            }
        });
    }

    //endregion
}
