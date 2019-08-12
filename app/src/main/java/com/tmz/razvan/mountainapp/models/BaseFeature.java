package com.tmz.razvan.mountainapp.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BaseFeature {

    private String Id;
    private String AuthorId;
    private String HikeId;
    private Date CreateDate;
    private List<String> ImageUrlList;
    private String Content;
    private String Title;
    private Coordinates Coordinates;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getAuthorId() {
        return AuthorId;
    }

    public void setAuthorId(String authorId) {
        AuthorId = authorId;
    }

    public String getHikeId() {
        return HikeId;
    }

    public void setHikeId(String hikeId) {
        HikeId = hikeId;
    }

    public Date getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(Date createDate) {
        CreateDate = createDate;
    }

    public List<String> getImageUrlList() {
        if(ImageUrlList == null)
        {
            ImageUrlList = new ArrayList<String>();
        }
        return ImageUrlList;
    }

    public void setImageUrlList(List<String> imageUrlList) {
        ImageUrlList = imageUrlList;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public Coordinates getCoordinates() {
        return Coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        Coordinates = coordinates;
    }

    public void addPicture(String url)
    {
        if(ImageUrlList == null)
        {
            ImageUrlList = new ArrayList<String>();
        }

        if(!ImageUrlList.contains(url))
        {
            ImageUrlList.add(url);
        }
    }
}
