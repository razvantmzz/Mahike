package com.tmz.razvan.mountainapp.models.internal;

public class FeatureRowItem {

    private int ImageId;
    private String Title;
    private int Type;

    public FeatureRowItem(String Title, int ImageId, int Type){

        this.Title = Title;
        this.ImageId = ImageId;
        this.Type = Type;
    }

    public String getTitle(){

        return Title;
    }

    public void setTitle(String Title){

        this.Title = Title;
    }

    public int getImageId(){

        return ImageId;
    }

    public void setImageId(int ImageId){

        this.ImageId = ImageId;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    @Override
    public String toString() {
        return Title ;
    }
}
