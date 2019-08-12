package com.tmz.razvan.mountainapp.models;

import com.tmz.razvan.mountainapp.Interfaces.IHikeData;

import java.util.List;

public class AreaModel implements IHikeData {
    private String id;
    private String name;
    private String mountainName;
    private List<String> hikes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMountainName() {
        return mountainName;
    }

    public void setMountainName(String mountainName) {
        this.mountainName = mountainName;
    }

    public List<String> getHikes() {
        return hikes;
    }

    public void setHikes(List<String> hikes) {
        this.hikes = hikes;
    }
}
