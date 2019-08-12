package com.tmz.razvan.mountainapp.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.ColorUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tmz.razvan.mountainapp.Adapters.CustomInfoWindowGoogleMap;
import com.tmz.razvan.mountainapp.Constants.NavigationContants;
import com.tmz.razvan.mountainapp.Core.CoreData;
import com.tmz.razvan.mountainapp.Core.UserCore;
import com.tmz.razvan.mountainapp.Helpers.BitmapHelper;
import com.tmz.razvan.mountainapp.Helpers.FetchURL;
import com.tmz.razvan.mountainapp.Helpers.FirebaseHelper;
import com.tmz.razvan.mountainapp.Helpers.GoogleMapsHelper;
import com.tmz.razvan.mountainapp.Helpers.HikeFeaturesHelper;
import com.tmz.razvan.mountainapp.Helpers.TaskLoadedCallback;
import com.tmz.razvan.mountainapp.Helpers.TouristicMarkerHelper;
import com.tmz.razvan.mountainapp.R;
import com.tmz.razvan.mountainapp.TranslationConstants;
import com.tmz.razvan.mountainapp.baseClasses.BaseAppCompat;
import com.tmz.razvan.mountainapp.models.Coordinates;
import com.tmz.razvan.mountainapp.models.HikeFeature;
import com.tmz.razvan.mountainapp.models.HikeModel;
import com.tmz.razvan.mountainapp.models.UserHikeData;
import com.tmz.razvan.mountainapp.models.UserNote;

public class HikeMapActivity extends BaseAppCompat implements OnMapReadyCallback, TaskLoadedCallback {

    private HikeModel mHikeModel;
    private UserHikeData mUserHikeData;

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private Polyline currentPolyline;

    private FloatingActionButton mOpenMenuButon;
    private FloatingActionButton mAddNoteButton;
    private RelativeLayout mMenuContainer;
    private Marker mLastMarkerClicked;
    private RelativeLayout mMarkerMenu;

    private boolean isInDropPinMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike_map);

        String serialisedHikeModel = getIntent().getStringExtra(NavigationContants.HIKE_MODEL_KEY);
        mHikeModel = new Gson().fromJson(serialisedHikeModel, HikeModel.class);
        mUserHikeData = UserCore.Instance().User.getPrivateHikeDataById(mHikeModel.getId());

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapFragment.setRetainInstance(true);

        SetToolbarTitle(mHikeModel.getName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUserHikeData = UserCore.Instance().User.getPrivateHikeDataById(mHikeModel.getId());
        mHikeModel = CoreData.getInstance().getHikeById(mHikeModel.getId());
        if(currentPolyline == null && mMap != null)
        {
            mapFragment.getMapAsync(this);
        }

        SetUpFabMenu();
        SetStrings();
        addUserNotesOnMap();
        SetDropPinMode(false, -1);
    }

    @Override
    public void onBackPressed() {
        if(isInDropPinMode)
        {
            SetDropPinMode(false, -1);
            return;
        }
        super.onBackPressed();
    }

    private void SetUpFabMenu() {
        mMarkerMenu = findViewById(R.id.rl_marker_menu_container);
        mOpenMenuButon = findViewById(R.id.fab_map_add_menu);
        mAddNoteButton = findViewById(R.id.fab_map_add_note);
        mMenuContainer = findViewById(R.id.rl_map_menu_container);

        mMenuContainer.setVisibility(View.GONE);
        mMenuContainer.setBackgroundColor(ColorUtils.setAlphaComponent(Color.WHITE, 230));
        mAddNoteButton.setRippleColor(getResources().getColor(R.color.primaryButtonColor));
        mOpenMenuButon.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));

        FloatingActionButton addFeatureButton = findViewById(R.id.fab_map_add_feature);
        addFeatureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMenuContainer.getVisibility() == View.VISIBLE)
                {
                    SetDropPinMode(true, 2);
                }
            }
        });

        mOpenMenuButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMenuContainer.setVisibility(View.VISIBLE);
                mOpenMenuButon.hide();
            }
        });

        mMenuContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMenuContainer.setVisibility(View.GONE);
                mOpenMenuButon.show();
            }
        });

        mAddNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMenuContainer.getVisibility() == View.VISIBLE)
                {
                    SetDropPinMode(true, 1);
                }
            }
        });
    }

    private void SetStrings()
    {
        TextView mAddNoteTitle = findViewById(R.id.tv_map_add_note);
        mAddNoteTitle.setText(TranslationConstants.ADD_BOOOKMARK);

        TextView mAddFeatureTitle = findViewById(R.id.tv_map_add_feature);
        mAddFeatureTitle.setText(TranslationConstants.ADD_FEATURE);
    }

    private void SetDropPinMode(boolean modeOn, final int type)
    {
        isInDropPinMode = modeOn;

        if(modeOn)
        {
            Snackbar.make(Toolbar, TranslationConstants.DROP_A_PIN, Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show();

            mMenuContainer.setVisibility(View.GONE);
            BackButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_close));
            mOpenMenuButon.setImageDrawable(getResources().getDrawable(R.drawable.ic_add));
            mOpenMenuButon.show();

            BackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SetDropPinMode(false, -1);
                }
            });

            mOpenMenuButon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mLastMarkerClicked == null)
                    {
                        Snackbar.make(Toolbar, TranslationConstants.DROP_A_PIN, Snackbar.LENGTH_LONG)
                                .setAction("Action", null)
                                .show();
                        return;
                    }
                    mMap.setOnMapClickListener(null);
                    final Coordinates markerCoordinates = Coordinates.fromLatlng(mLastMarkerClicked.getPosition());
                     if(mLastMarkerClicked != null)
                    {
                        mLastMarkerClicked.remove();
                        mLastMarkerClicked = null;
                    }
                    SetDropPinMode(false, -1);

                    if(type == 1)
                    {
                        UserNote selectedUserNote = mUserHikeData.getNoteByCoordinates(markerCoordinates);
                        selectedUserNote.setHikeId(mHikeModel.getId());

                        Intent intent = new Intent(HikeMapActivity.this, NoteActivity.class);
                        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                        } else {
                            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                        }
                        String serialisedUserNote = new Gson().toJson(selectedUserNote);
                        intent.putExtra(NavigationContants.USER_HIKE_DATA_KEY, serialisedUserNote);
                        intent.putExtra(NavigationContants.NOTE_SHOULD_EDIT, true);
                        HikeMapActivity.this.startActivity(intent);
                    }

                    if(type == 2)
                    {
                        Intent intent = new Intent(HikeMapActivity.this, HikeFeatureActivity.class);
                        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                        } else {
                            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                        }
                        HikeFeature hikeFeature = new HikeFeature();
                        hikeFeature.setHikeId(mHikeModel.getId());
                        hikeFeature.setAuthorId(UserCore.Instance().User.getUserId());
                        hikeFeature.setCoordinates(markerCoordinates);

                        String serialisedHikeFeature = new Gson().toJson(hikeFeature);
                        String serialisedHikeModel = new Gson().toJson(mHikeModel);

                        intent.putExtra(NavigationContants.HIKE_MODEL_KEY, serialisedHikeModel);
                        intent.putExtra(NavigationContants.BASE_FEATURE, serialisedHikeFeature);
                        intent.putExtra(NavigationContants.NOTE_SHOULD_EDIT, true);
                        HikeMapActivity.this.startActivity(intent);
                    }
                }
            });

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    if(mLastMarkerClicked != null)
                    {
                        mLastMarkerClicked.remove();
                        mLastMarkerClicked = null;
                        mMarkerMenu.setVisibility(View.GONE);
                    }
                    else
                    {
                        mLastMarkerClicked = mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
                    }
                }
            });
        }
        else
        {
            //mode off
            BackButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
            mOpenMenuButon.setImageDrawable(getResources().getDrawable(R.drawable.ic_vector_menu_stripes));
            mOpenMenuButon.hide();
            mOpenMenuButon.show();
            if(mLastMarkerClicked != null)
            {
                mLastMarkerClicked.remove();
                mLastMarkerClicked = null;
            }
            mOpenMenuButon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mMenuContainer.setVisibility(View.VISIBLE);
                    mOpenMenuButon.hide();
                }
            });
            if(mMap != null)
            {
                mMap.setOnMapClickListener(null);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        final LatLng startPoint = new LatLng(mHikeModel.getCoordinates().getStartPoint().getLatitude(), mHikeModel.getCoordinates().getStartPoint().getLongitude());
        final LatLng endPoint = new LatLng(mHikeModel.getCoordinates().getEndPoint().getLatitude(), mHikeModel.getCoordinates().getEndPoint().getLongitude());

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

        mMap.setMaxZoomPreference(14);
        addUserNotesOnMap();

        if(mHikeModel.getPolyline() == null || mHikeModel.getPolyline().isEmpty())
        {
            String url = GoogleMapsHelper.getUrl(this, startPoint, endPoint, "walking");
            new FetchURL(HikeMapActivity.this).execute(url, "walking");
        }
        else
        {
            mMap.addPolyline(GoogleMapsHelper.getPolylineOptions(mHikeModel.getPolyline()));
        }

        Bitmap bitmap = TouristicMarkerHelper.getMarkerByType(this, mHikeModel.getMarkType());
        mMap.addMarker(new MarkerOptions().position(startPoint).title(mHikeModel.getCoordinates().getStartPoint().getName()).icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
        mMap.addMarker(new MarkerOptions().position(endPoint).title(mHikeModel.getCoordinates().getEndPoint().getName()).icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if(marker.getTag() instanceof UserNote)
                {
                    mLastMarkerClicked = marker;
                    Intent intent = new Intent(HikeMapActivity.this, NoteActivity.class);
                    String serialisedUserNote = new Gson().toJson((UserNote)marker.getTag());
                    intent.putExtra(NavigationContants.USER_HIKE_DATA_KEY, serialisedUserNote);
                    intent.putExtra(NavigationContants.NOTE_SHOULD_EDIT, false);
                    HikeMapActivity.this.startActivity(intent);
                }
                if(marker.getTag() instanceof HikeFeature)
                {
                    mLastMarkerClicked = marker;
                    Intent intent = new Intent(HikeMapActivity.this, HikeFeatureActivity.class);
                    String serialisedUserNote = new Gson().toJson((HikeFeature)marker.getTag());
                    String serialisedHike = new Gson().toJson(mHikeModel);
                    intent.putExtra(NavigationContants.HIKE_MODEL_KEY, serialisedHike);
                    intent.putExtra(NavigationContants.BASE_FEATURE, serialisedUserNote);
                    intent.putExtra(NavigationContants.NOTE_SHOULD_EDIT, false);
                    HikeMapActivity.this.startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();

        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
        mHikeModel.setPolyline(new Gson().toJson(currentPolyline.getPoints()));
        FirebaseHelper.Instance().SyncHikesData(mHikeModel);
    }

    private void addUserNotesOnMap()
    {
        if(mMap != null)
        {
            mMap.clear();
            CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(HikeMapActivity.this);
            mMap.setInfoWindowAdapter(customInfoWindow);

            Bitmap bitmap = BitmapHelper.getBitmapFromVectorDrawableId(this, R.drawable.ic_bookmark_black_24dp);
            for (UserNote userNote : mUserHikeData.getNoteList())
            {
                if(userNote == null)
                {
                    continue;
                }
                mMap.addMarker(new MarkerOptions().position(Coordinates.toLatLng(userNote.getCoordinates())).title(userNote.getTitle()).icon(BitmapDescriptorFactory.fromBitmap(bitmap))).setTag(userNote);
            }

            for (HikeFeature feature : mHikeModel.getFeatures())
            {
                if(feature == null)
                {
                    continue;
                }
                mMap.addMarker(new MarkerOptions().position(Coordinates.toLatLng(feature.getCoordinates())).title(feature.getTitle()).
                        icon(BitmapDescriptorFactory.fromBitmap(HikeFeaturesHelper.GetImageBitmapByType(this, feature.getType())))).setTag(feature);
            }
        }
    }
}
