package com.tmz.razvan.mountainapp.models;

import com.tmz.razvan.mountainapp.enums.HikeFeatureType;

import java.util.UUID;

public class HikeFeature extends BaseFeature {

    private int Type;
    private Boolean WasAproved;

    public HikeFeature()
    {
        setId(UUID.randomUUID().toString());
        WasAproved = false;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public Boolean getWasAproved() {
        return WasAproved;
    }

    public void setWasAproved(Boolean wasAproved) {
        WasAproved = wasAproved;
    }
}
