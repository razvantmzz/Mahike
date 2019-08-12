package com.tmz.razvan.mountainapp.models;

import com.google.android.gms.maps.model.LatLng;

public class HikeCoordinates {

    private Coordinates StartPoint;

    private Coordinates EndPoint;

    public HikeCoordinates() {
    }

    public HikeCoordinates(Coordinates startPoint, Coordinates endPoint) {
        StartPoint = startPoint;
        EndPoint = endPoint;
    }

    public Coordinates getStartPoint() {
        return StartPoint;
    }

    public void setStartPoint(Coordinates startPoint) {
        StartPoint = startPoint;
    }

    public Coordinates getEndPoint() {
        return EndPoint;
    }

    public void setEndPoint(Coordinates endPoint) {
        EndPoint = endPoint;
    }


}
