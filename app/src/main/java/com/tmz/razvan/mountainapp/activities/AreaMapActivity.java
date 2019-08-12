package com.tmz.razvan.mountainapp.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.tmz.razvan.mountainapp.Adapters.SearchHikesListAdapter;
import com.tmz.razvan.mountainapp.Constants.NavigationContants;
import com.tmz.razvan.mountainapp.Core.CoreData;
import com.tmz.razvan.mountainapp.Helpers.GoogleMapsHelper;
import com.tmz.razvan.mountainapp.Helpers.InternetHelper;
import com.tmz.razvan.mountainapp.Helpers.OfflineHelper;
import com.tmz.razvan.mountainapp.Helpers.PixelHelper;
import com.tmz.razvan.mountainapp.Helpers.TaskLoadedCallback;
import com.tmz.razvan.mountainapp.Helpers.TouristicMarkerHelper;
import com.tmz.razvan.mountainapp.R;
import com.tmz.razvan.mountainapp.baseClasses.BaseAppCompat;
import com.tmz.razvan.mountainapp.models.AreaModel;
import com.tmz.razvan.mountainapp.models.HikeCoordinates;
import com.tmz.razvan.mountainapp.models.HikeModel;

import java.util.ArrayList;
import java.util.List;

public class AreaMapActivity extends BaseAppCompat implements OnMapReadyCallback, TaskLoadedCallback {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;

    private List<HikeModel> hikeModelList;
    private AreaModel area;

    private ToggleButton toggleButton;
    private RecyclerView hikesRecylerView;
    private SearchHikesListAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_map);

        area = CoreData.getInstance().getAreaById(getIntent().getStringExtra(NavigationContants.AREA_ID_KEY));

        setUpViews();

        mapFragment.getMapAsync(this);
        mapFragment.setRetainInstance(true);

        getAreaHikes();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SetToolbarTitle(area.getName());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mAdapter.mMap = mMap;
    }

    @Override
    public void onTaskDone(Object... values) {

    }

    private void getAreaHikes()
    {
        if(!InternetHelper.hasInternetAccess(this))
        {
            hikeModelList = new ArrayList<>();
            List<HikeModel> offlineHikeList = CoreData.getInstance().getHikesList();
            if(offlineHikeList == null)
            {
                return;
            }
            for (HikeModel hikeModel :  offlineHikeList) {

                for (String id : area.getHikes())
                {
                    if(id.equals(hikeModel.getId()))
                    {
                        hikeModelList.add((hikeModel));
                    }
                }
            }
            return;
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference tableViewHikesReference = database.getReference("tb_hikes");
        hikeModelList = new ArrayList<>();
        SetIsBusy(true);
        setUpHikesList();

        tableViewHikesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds :  dataSnapshot.getChildren()) {

                    HikeModel hikeModel = ds.getValue(HikeModel.class);
                    for (String id : area.getHikes())
                    {
                        if(id.equals(hikeModel.getId()))
                        {
                            hikeModelList.add((hikeModel));
                        }
                    }
                }

                SetIsBusy(false, false);
                if(hikeModelList == null || hikeModelList.size() == 0)
                {
                    return;
                }
                addHikesMapData();
                mAdapter.notifyDataSetChanged();
//                mAdapter.mDataset = hikeModelList;
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    private void addHikesMapData()
    {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (HikeModel hike :
                hikeModelList) {
            if(hike.getPolyline() == null)
            {
                continue;
            }
            HikeCoordinates hikeCoordinates = hike.getCoordinates();
            LatLng startPoint = new LatLng(hikeCoordinates.getStartPoint().getLatitude(), hikeCoordinates.getStartPoint().getLongitude());
            LatLng endPoint = new LatLng(hikeCoordinates.getEndPoint().getLatitude(), hikeCoordinates.getEndPoint().getLongitude());

            Bitmap bitmap = TouristicMarkerHelper.getMarkerByType(this, hike.getMarkType());
            mMap.addMarker(new MarkerOptions().position(startPoint).title(hikeCoordinates.getStartPoint().getName()).icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
            mMap.addMarker(new MarkerOptions().position(endPoint).title(hikeCoordinates.getEndPoint().getName()).icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
            PolylineOptions polyline = GoogleMapsHelper.getPolylineOptions(hike.getPolyline());
            PolylineOptions polylineWhite = GoogleMapsHelper.getPolylineOptions(hike.getPolyline());
            polylineWhite.width(5).color(Color.WHITE);

            polyline.width(2).color(TouristicMarkerHelper.getColorByType(hike.getMarkType()));
            mMap.addPolyline(polylineWhite);
            mMap.addPolyline(polyline);

            builder.include(startPoint);
            builder.include(endPoint);

        }
        final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(builder.build(), 100);
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                /**set animated zoom camera into map*/
                mMap.moveCamera(cu);// animateCamera(cu);
            }
        });
    }

    //region ui methods

    private void setUpViews()
    {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        hikesRecylerView = findViewById(R.id.rv_activity_area_map_hikes);
        toggleButton = new ToggleButton(this);

//        toggleButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_vector_menu_stripes, null));
//        toggleButton.setLayoutParams(new LinearLayout.LayoutParams(PixelHelper.dpToPixel(30), PixelHelper.dpToPixel(30)));
//        toggleButton.setTextOn("");
//        toggleButton.setTextOff("");
//        toggleButton.setChecked(true);
//
//        toggleButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(hikesRecylerView.getVisibility() == View.VISIBLE)
//                {
//                    hikesRecylerView.setVisibility(View.GONE);
//                }
//                else
//                {
//                    hikesRecylerView.setVisibility(View.VISIBLE);
//                }
//            }
//        });
//        addRightButton(toggleButton);

    }

    private void setUpHikesList()
    {
        hikesRecylerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        hikesRecylerView.setLayoutManager(layoutManager);
        mAdapter = new SearchHikesListAdapter(hikeModelList, this, mMap);
        hikesRecylerView.setAdapter(mAdapter);
        SnapHelper snaphelper = new LinearSnapHelper();
        snaphelper.attachToRecyclerView(hikesRecylerView);
    }

    //
}
