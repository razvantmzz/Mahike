package com.tmz.razvan.mountainapp.Helpers;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tmz.razvan.mountainapp.Core.CoreData;
import com.tmz.razvan.mountainapp.Core.Models.UserData;
import com.tmz.razvan.mountainapp.Core.UserCore;
import com.tmz.razvan.mountainapp.Listeners.OnMultipleFilesUploadListener;
import com.tmz.razvan.mountainapp.Listeners.OnUploadImageListener;
import com.tmz.razvan.mountainapp.models.HikeModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FirebaseHelper {

    private OnUploadImageListener onUploadImageListener;
    private OnMultipleFilesUploadListener onMultipleFilesUploadListener;

    private static FirebaseHelper mFirebaseHelper;

    private FirebaseDatabase database;
    private DatabaseReference tableViewHikesReference;

    public static FirebaseHelper Instance()
    {
        if(mFirebaseHelper == null)
        {
            mFirebaseHelper = new FirebaseHelper();
        }
        return mFirebaseHelper;
    }


    public void SetRegistrationToken(String token)
    {
    }


    public void SyncUserData(String userId)
    {
        database = FirebaseDatabase.getInstance();
        tableViewHikesReference = database.getReference("tb_userData");
        tableViewHikesReference.child(userId).setValue(UserCore.Instance().User);
    }

    public void SyncUserData()
    {
        if(UserCore.Instance().getLoggedIn())
        {
            database = FirebaseDatabase.getInstance();
            tableViewHikesReference = database.getReference("tb_userData");
            tableViewHikesReference.child(UserCore.Instance().User.getUserId()).setValue(UserCore.Instance().User);
        }
    }

    public void SyncUserData(UserData user)
    {
        database = FirebaseDatabase.getInstance();
        tableViewHikesReference = database.getReference("tb_userData");
        tableViewHikesReference.child(user.getUserId()).setValue(user);
    }

    public void SyncHikesData(HikeModel hikeModel)
    {
        if(UserCore.Instance().getLoggedIn())
        {
            CoreData.getInstance().updateHike(hikeModel);
            database = FirebaseDatabase.getInstance();
            tableViewHikesReference = database.getReference("tb_hikes");
            tableViewHikesReference.child(hikeModel.getId()).setValue(hikeModel);
        }
    }

    public FirebaseHelper uploadImage(String imagePath, String imageName, StorageReference storageReference)
    {
        Uri originalImage = Uri.parse(imagePath);
        final StorageReference ImageName = storageReference.child(imageName);

        //add original image
        ImageName.putFile(originalImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        onUploadImageListener.onUploadSucces(uri);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                });
            }
        });

        return this;
    }

    int upload_succes_count = -1;
    List<String> itemUrls = new ArrayList<String>();

    public FirebaseHelper uploadMultipleImages(final List<String> itemList, StorageReference ref) {
        int upload_count = 0;
        upload_succes_count = 0;
        for (upload_count = 0; upload_count < itemList.size(); upload_count++) {

            FirebaseHelper firebaseHelper = new FirebaseHelper();
            firebaseHelper.uploadImage(itemList.get(upload_count), UUID.randomUUID().toString(), ref).setOnUploadImageListener(
                    new OnUploadImageListener() {
                        @Override
                        public void onUploadSucces(Uri uri) {
                            itemUrls.add(String.valueOf(uri));
                            upload_succes_count += 1;
                            if(upload_succes_count == itemList.size())
                            {
                                //call on success
                                onMultipleFilesUploadListener.onUploadSucces(itemUrls);
                            }
                            }
                        }
                    );
        }

        return this;
    }

    public void uploadImageWithThumnails(String imagePath, String imageName, StorageReference storageReference) throws IOException {
        uploadImage(imagePath, imageName, storageReference);

        Bitmap smallThumbnail = BitmapHelper.getThumbnail(imagePath, 32, 32);
    }


    public FirebaseHelper setOnUploadImageListener(OnUploadImageListener onUploadImageListener)
    {
        this.onUploadImageListener = onUploadImageListener;
        return this;
    }

    public FirebaseHelper setOnMultipleFilesUploadListener(OnMultipleFilesUploadListener onMultipleFilesUploadListener)
    {
        this.onMultipleFilesUploadListener = onMultipleFilesUploadListener;
        return this;
    }
//    public void GetUserData(String id)
//    {
//        final String userId = id;
//        database = FirebaseDatabase.getInstance();
//        tableViewHikesReference = database.getReference("tb_userData");
//        tableViewHikesReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    UserData userData = ds.getValue(UserData.class);
//                    if(userData.getUserId() == userId)
//                    {
//                        return; userData;
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//            }
//        });
//    }
}
