package com.tmz.razvan.mountainapp.Helpers;

import com.tmz.razvan.mountainapp.Core.Models.UserData;
import com.tmz.razvan.mountainapp.Core.UserCore;
import com.tmz.razvan.mountainapp.enums.UserType;

public class PermissionHelper {

    public static Boolean userCanEditFeature(String featureAuthorId)
    {
        UserData user = UserCore.Instance().User;
        if(user.getType() == UserType.Admin ||  user.getUserId().contains(featureAuthorId))
        {
            return true;
        }
        return false;
    }
}
