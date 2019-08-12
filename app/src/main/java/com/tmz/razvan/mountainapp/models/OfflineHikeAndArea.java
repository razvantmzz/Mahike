package com.tmz.razvan.mountainapp.models;

import java.util.List;

public class OfflineHikeAndArea {
    AreaModel area;
    List<HikeModel> hikeList;

    public AreaModel getArea() {
        return area;
    }

    public OfflineHikeAndArea setArea(AreaModel area) {
        this.area = area;
        return this;
    }

    public List<HikeModel> getHikeList() {
        return hikeList;
    }

    public OfflineHikeAndArea setHikeList(List<HikeModel> hikeList) {
        this.hikeList = hikeList;
        return this;
    }
}
