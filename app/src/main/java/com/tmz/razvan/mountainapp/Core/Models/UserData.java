package com.tmz.razvan.mountainapp.Core.Models;

import com.tmz.razvan.mountainapp.Core.UserCore;
import com.tmz.razvan.mountainapp.Helpers.FirebaseHelper;
import com.tmz.razvan.mountainapp.models.UserHikeData;
import com.tmz.razvan.mountainapp.models.UserNote;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserData {

    private String UserId;
    private String token;
    private List<UserHikeData> PrivateHikeData;
    private int Type;
    private String Phone;
    private String EmergencyContact;
    private Date DateOfBirth;
    private int Gender;
    private String FullName;
    private String ProfilePicture;
    private String Email;
    private int Experience;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<UserHikeData> getPrivateHikeData() {
        if(PrivateHikeData == null)
        {
            PrivateHikeData = new ArrayList<UserHikeData>();
        }
        return PrivateHikeData;

    }

    public UserHikeData getPrivateHikeDataById(String id) {
        if(PrivateHikeData == null)
        {
            PrivateHikeData = new ArrayList<UserHikeData>();
        }

        for (UserHikeData hike:PrivateHikeData) {
            if (hike.getId().contains(id))
            {
                return  hike;
            }
        }

        UserHikeData userHikeData = new UserHikeData();
        userHikeData.setId(id);
        userHikeData.setFavorite(false);
        userHikeData.setNoteList(new ArrayList<UserNote>());
        PrivateHikeData.add(userHikeData);

        return userHikeData;

    }

    public void setPrivateHikeData(List<UserHikeData> privateHikeData) {
        PrivateHikeData = privateHikeData;
        SyncData();
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
        SyncData();
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        this.Phone = phone;
    }

    public String getEmergencyContact() {
        return EmergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.EmergencyContact = emergencyContact;
    }

    public Date getDateOfBirth() {
        if(DateOfBirth == null)
        {
            DateOfBirth = new Date();
        }
        return DateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.DateOfBirth = dateOfBirth;
    }

    public int getGender() {
        return Gender;
    }

    public void setGender(int gender) {
        this.Gender = gender;
    }

    public void setFullName(String name)
    {
        this.FullName = name;
    }

    public String getFullName()
    {
        return FullName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getProfilePicture() {
        return ProfilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        ProfilePicture = profilePicture;
    }

    public int getExperience() {
        return Experience;
    }

    public void setExperience(int experience) {
        Experience = experience;
    }

    private void SyncData()
    {
        if(UserCore.Instance().getLoggedIn())
        {
            FirebaseHelper.Instance().SyncUserData(getUserId());
        }
    }
}