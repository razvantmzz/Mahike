package com.tmz.razvan.mountainapp.Helpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Camera;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tmz.razvan.mountainapp.Core.CoreData;
import com.tmz.razvan.mountainapp.models.AreaModel;
import com.tmz.razvan.mountainapp.models.HikeModel;
import com.tmz.razvan.mountainapp.models.OfflineHikeAndArea;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class OfflineHelper {

    private static final String  PREF_NAME = "mountainPrefs";
    private static final String  AREA_HIKES_LIST = "areaAndHikesList";

    private ProgressDialog progressDialog;
    private Context context;

    public void SaveAreaOnDevice(Context context, AreaModel area)
    {
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog. setTitle("Getting Hikes...");
        progressDialog.show();
        getOnlineAreaHikes(area);

    }

    public void InitialiseOfflineAreasHikes(Context context)
    {
        List<OfflineHikeAndArea> offlineList = new ArrayList<>();

        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        Gson gson = new Gson();
        String serialisedStoredData = settings.getString(AREA_HIKES_LIST, "");
        if(!serialisedStoredData.isEmpty())
        {
            Type listType = new TypeToken<List<OfflineHikeAndArea>>(){}.getType();
            offlineList =  gson.fromJson(serialisedStoredData, listType);

            List<AreaModel> areasList = new ArrayList<>();
            List<HikeModel> hikesList = new ArrayList<>();

            for (OfflineHikeAndArea offData :
                    offlineList) {
                   areasList.add(offData.getArea());
                   hikesList.addAll(offData.getHikeList());
            }

            CoreData.getInstance().setAreasList(areasList);
            CoreData.getInstance().setHikesList(hikesList);
        }
    }

    private void getOnlineAreaHikes(final AreaModel area)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference tableViewHikesReference = database.getReference("tb_hikes");
        final List<HikeModel> hikeModelList = new ArrayList<>();

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

                if(hikeModelList == null || hikeModelList.size() == 0)
                {
                    return;
                }

                //hikes downloaded

                OfflineHikeAndArea offlineData = new OfflineHikeAndArea()
                        .setArea(area).setHikeList(hikeModelList);

                StoreOfflineHikeAndArea(offlineData);

            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    private void StoreOfflineHikeAndArea(OfflineHikeAndArea data)
    {
        progressDialog.setTitle("Storing data...");

        List<OfflineHikeAndArea> offlineList = new ArrayList<>();

        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        Gson gson = new Gson();
        String serialisedStoredData = settings.getString(AREA_HIKES_LIST, "");
        if(!serialisedStoredData.isEmpty())
        {
            Type listType = new TypeToken<List<OfflineHikeAndArea>>(){}.getType();
            offlineList =  gson.fromJson(serialisedStoredData, listType);
        }

        if(!exist(offlineList, data))
        {
            offlineList.add(data);
        }


        String serialisedUpdatedData = gson.toJson(offlineList);
        editor.putString(AREA_HIKES_LIST, serialisedUpdatedData).apply();
        progressDialog.dismiss();
    }

    private boolean exist(List<OfflineHikeAndArea> list, OfflineHikeAndArea data)
    {
        for (OfflineHikeAndArea offData :
                list) {
            if (offData.getArea().getId().equals(data.getArea().getId())) {
                return true;
            }
        }
        return false;
    }
}
