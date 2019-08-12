package com.tmz.razvan.mountainapp.models;

import com.google.android.gms.maps.model.LatLng;

public class Coordinates {

    private double Latitude;
    private double Longitude;
    private String name;

    public Coordinates() {
    }

    public Coordinates(double lat, double longitude)
    {
        this.setLatitude(lat);
        this.setLongitude(longitude);
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public static Coordinates fromLatlng(LatLng cord)
    {
        return new Coordinates(cord.latitude, cord.longitude);
    }

    public  static LatLng toLatLng(Coordinates coord)
    {
        return  new LatLng(coord.getLatitude(), coord.getLongitude());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
