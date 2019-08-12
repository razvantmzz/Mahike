package com.tmz.razvan.mountainapp.Core;

import android.hardware.Camera;

import com.tmz.razvan.mountainapp.Helpers.PixelHelper;
import com.tmz.razvan.mountainapp.models.AreaModel;
import com.tmz.razvan.mountainapp.models.HikeModel;

import java.util.ArrayList;
import java.util.List;

public class CoreData {

    private static CoreData mCoreData;

    private List<HikeModel> mHikesList;
    private List<AreaModel> areasList;
    private boolean mSyncAtStartup;

    public static CoreData getInstance()
       {
           if(mCoreData == null)
           {
               mCoreData = new CoreData();
           }
           return mCoreData;
       }

    public List<HikeModel> getHikesList() {
        return mHikesList;
    }

    public void setHikesList(List<HikeModel> mhikeModel) {
        this.mHikesList = mhikeModel;
    }

    public void addHikeToCache(HikeModel hike)
    {
        if(mHikesList == null)
        {
            mHikesList = new ArrayList<>();
        }
        mHikesList.add(hike);
    }

    public List<AreaModel> getAreasList() {
        return areasList;
    }

    public AreaModel getAreaById(String id)
    {
        if(areasList == null)
        {
            return null;
        }

        for (AreaModel area : areasList)
        {
            if(area.getId().equals(id))
            {
                return area;
            }
        }

        return null;
    }

    public void setAreasList(List<AreaModel> areasList) {
        this.areasList = areasList;
    }

    public HikeModel getHikeById(String id)
    {
        for (HikeModel hike : mHikesList) {
            if(hike.getId().contains(id))
            {
                return  hike;
            }
        }
        return null;
    }

    public void updateHike(HikeModel hikeModel)
    {
        for (HikeModel hike : mHikesList) {
            if(hike.getId().contains(hikeModel.getId()))
            {
                mHikesList.set(mHikesList.indexOf(hike), hikeModel);
            }
        }
    }

    public boolean ismSyncAtStartup() {
        return mSyncAtStartup;
    }

    public void setSyncAtStartup(boolean SyncAtStartup) {
        this.mSyncAtStartup = SyncAtStartup;
    }

    public boolean getSyncAtStartup()
    {
        return mSyncAtStartup;
    }
}
