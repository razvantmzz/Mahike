package com.tmz.razvan.mountainapp.activities;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tmz.razvan.mountainapp.Core.CoreData;
import com.tmz.razvan.mountainapp.Helpers.InternetHelper;
import com.tmz.razvan.mountainapp.Helpers.OfflineHelper;
import com.tmz.razvan.mountainapp.Interfaces.OnBusyChangedListener;
import com.tmz.razvan.mountainapp.R;
import com.tmz.razvan.mountainapp.baseClasses.BaseAppCompat;
import com.tmz.razvan.mountainapp.models.AreaModel;
import com.tmz.razvan.mountainapp.models.HikeModel;
import com.tmz.razvan.mountainapp.models.OfflineHikeAndArea;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends BaseAppCompat {

    private FirebaseDatabase database;
    private DatabaseReference tableViewHikesReference;
    private List<HikeModel> mHikesList;
    private List<AreaModel> areasList;
    private SplashActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mActivity = this;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(!InternetHelper.hasInternetAccess(this))
        {
            OfflineHelper offlineHelper = new OfflineHelper();
            offlineHelper.InitialiseOfflineAreasHikes(this);

            Intent intent = new Intent(mActivity, HomeActivity.class);
            startActivity(intent);
            return;
        }

        database = FirebaseDatabase.getInstance();
        tableViewHikesReference = database.getReference("tb_hikes");

//        tableViewHikesReference = database.getReference("tb_areas");
//        for (int i = 0; i < 4; i++)
//        {
//            AreaModel hike = new AreaModel();
//            hike.setId(UUID.randomUUID().toString());
//            hike.setName("Area 5"+ i);
//            hike.setMountainName("Mountain " + i);
//            List<String> idList = new ArrayList<>();
//            idList.add(UUID.randomUUID().toString());
//            idList.add(UUID.randomUUID().toString());
//            hike.setHikes(idList);
//            tableViewHikesReference.child(hike.getId()).setValue(hike);
//        }
//        return;

        mOnBusyChangedListener = new OnBusyChangedListener() {
            @Override
            public void onChanged(boolean isBusy) {
                if(!isBusy)
                {
                    receive();

                }
            }
        };

        SetIsBusy(true, false);
//        database = FirebaseDatabase.getInstance();
//        tableViewHikesReference = database.getReference("tb_hikes");
//        mHikesList = new ArrayList<>();
//        tableViewHikesReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot ds :  dataSnapshot.getChildren()) {
//
//                    HikeModel hikeModel = ds.getValue(HikeModel.class);
//                    mHikesList.add((hikeModel));
//                }
//
//                CoreData.getInstance().setHikesList(mHikesList);
//                SetIsBusy(false, false);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//            }
//        });

        database = FirebaseDatabase.getInstance();
        tableViewHikesReference = database.getReference("tb_areas");
        areasList = new ArrayList<>();
        tableViewHikesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds :  dataSnapshot.getChildren()) {

                    AreaModel hikeModel = ds.getValue(AreaModel.class);
                    areasList.add((hikeModel));
                }

                CoreData.getInstance().setAreasList(areasList);
                SetIsBusy(false, false);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });


//        for (HikeModel hike : mHikesList)
//        {
////                    HikeModel hike = new HikeModel();
//            hike.setId(UUID.randomUUID().toString());
//            hike.setName("Hike ");
//            hike.setDuration("8 h");
//            hike.setDifficulty("Beginer");
//            hike.setImageUrl("https://firebasestorage.googleapis.com/v0/b/mountainapp-de974.appspot.com/o/mountain-peaks-unconquered.jpg?alt=media&token=0c5a8fbf-73f0-4bcf-a767-d84f53d2b217");
//            hike.setCoordinates(new HikeCoordinates(new Coordinates(23, 45), new Coordinates(23, 45)));
//            tableViewHikesReference.child(hike.getId()).setValue(hike);
//        }

    }

    public synchronized void receive() {
        if(CoreData.getInstance().getSyncAtStartup())
        {
            return;
        }
//                wait(2000);
        CoreData.getInstance().setSyncAtStartup(true);
        Intent intent = new Intent(mActivity, LoginActivity.class);
        startActivity(intent);

    }
}
