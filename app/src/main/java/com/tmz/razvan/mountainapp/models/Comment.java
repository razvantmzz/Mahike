package com.tmz.razvan.mountainapp.models;

public class Comment {
    private String id;
    private String userId;
    private String userName;
    private String comment;

    public Comment(String id, String userId, String userName, String comment) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
