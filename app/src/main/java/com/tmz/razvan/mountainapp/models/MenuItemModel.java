package com.tmz.razvan.mountainapp.models;

public class MenuItemModel {

    public MenuItemModel(String text, int resourceId) {
        ResourceId = resourceId;
        Text = text;
    }

    private int ResourceId;

    private String Text;

    public void setResourceId(int resourceId) {
        ResourceId = resourceId;
    }

    public void setText(String text) {
        Text = text;
    }

    public int getResourceId() {

        return ResourceId;
    }

    public String getText() {
        return Text;
    }


}
