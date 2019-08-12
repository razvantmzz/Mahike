package com.tmz.razvan.mountainapp.models;

import com.tmz.razvan.mountainapp.Interfaces.IHikeData;

import java.util.ArrayList;
import java.util.List;

public class HikeModel implements IHikeData{

    private String id;
    private String name;
    private String availability;
    private String details;
    private String duration;
    private int difficulty;
    private HikeCoordinates coordinates;
    private List<String> imageUrl;
    private int markType;
    private String polyline;

    private List<HikeFeature> features;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getImageUrl() {
        if(imageUrl == null)
        {
            imageUrl = new ArrayList<>();
            imageUrl.add("https://firebasestorage.googleapis.com/v0/b/mountainapp-de974.appspot.com/o/mountain-peaks-unconquered.jpg?alt=media&token=0c5a8fbf-73f0-4bcf-a767-d84f53d2b217");
        }
        return imageUrl;
    }

    public void setImageUrl(List<String> imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public HikeCoordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(HikeCoordinates coordinates) {
        this.coordinates = coordinates;
    }

    public int getMarkType() {
        return markType;
    }

    public void setMarkType(int markType) {
        this.markType = markType;
    }

    public String getPolyline() {
        return polyline;
    }

    public void setPolyline(String polyline) {
        this.polyline = polyline;
    }

    public List<HikeFeature> getFeatures() {
        if(features == null)
        {
            features = new ArrayList<>();
        }
        return features;
    }

    public void setFeatures(List<HikeFeature> features) {
        this.features = features;
    }

    public void addFeature(HikeFeature feature)
    {
        for (HikeFeature featureModel : getFeatures())
        {
            if(featureModel.getId().contains(feature.getId()))
            {
                features.set(features.indexOf(featureModel), feature);
                return;
            }
        }
        getFeatures().add(feature);
    }

}
