package com.tmz.razvan.mountainapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.tmz.razvan.mountainapp.Constants.ValidationConstants;
import com.tmz.razvan.mountainapp.Core.Models.UserData;
import com.tmz.razvan.mountainapp.Core.UserCore;
import com.tmz.razvan.mountainapp.CustomUIViews.FormEditText;
import com.tmz.razvan.mountainapp.CustomUIViews.RoundImageView;
import com.tmz.razvan.mountainapp.Helpers.FirebaseHelper;
import com.tmz.razvan.mountainapp.Listeners.OnUploadImageListener;
import com.tmz.razvan.mountainapp.R;
import com.tmz.razvan.mountainapp.TranslationConstants;
import com.tmz.razvan.mountainapp.baseClasses.BaseAppCompat;
import com.tmz.razvan.mountainapp.enums.Gender;
import com.tmz.razvan.mountainapp.validationCommands.RegexValidationCommand;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class ProfileActivity extends BaseAppCompat {

    private final int PICK_IMAGE_REQUEST = 71;

    private TextView mFullName;
    private TextView mGenderAndBirth;
    private FormEditText mEmail;
    private FormEditText mPhone;
    private FormEditText mEmergencyContact;
    private Button mChangePhoto;
    private ToggleButton mEditButton;
    private RoundImageView mProfilePicture;
    private String mImagePath;

    private boolean isEditMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setUpViews();
        initialiseData();
        setValidationCommands();
        SetTranslationStrings();
        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEditMode(!isEditMode);
                if(!isEditMode)
                {
                    saveData();
                }
            }
        });

        mChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        isEditMode(isEditMode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            mImagePath = data.getData().toString();

            Uri imageUri = Uri.parse(mImagePath);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                mProfilePicture.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //region private methods

    private void setUpViews()
    {
        mFullName = findViewById(R.id.tv_activity_profile_user_name);
        mGenderAndBirth = findViewById(R.id.tv_activity_profile_gender_birth_date);
        mEmail = findViewById(R.id.et_activity_profile_email);
        mPhone = findViewById(R.id.et_activity_profile_phone);
        mEmergencyContact = findViewById(R.id.et_activity_profile_emergency_contact);
        mChangePhoto = findViewById(R.id.btn_toolbar_add_photo);
        mEditButton = findViewById(R.id.toolbar_edit_save_button);
        mProfilePicture = findViewById(R.id.iv_profile_activity_user_image);
    }

    private void initialiseData()
    {
        UserData user = UserCore.Instance().getUser();
        SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-mm-dd");
        Glide.with(this).load(user.getProfilePicture()).centerCrop().into(mProfilePicture);

        mFullName.setText(user.getFullName());
        mGenderAndBirth.setText(String.format("%s, %s", GetGenderText(Gender.values()[user.getGender()]), dt1.format(user.getDateOfBirth())));
        mEmail.setText(UserCore.Instance().getFUser().getEmail());
        mPhone.setText(user.getPhone());
        mEmergencyContact.setText(user.getEmergencyContact());
    }

    private String GetGenderText(Gender gender)
    {
        if(gender == Gender.MALE)
        {
            return TranslationConstants.MALE;
        }
        return  TranslationConstants.FEMALE;
    }

    private void setValidationCommands()
    {
        mEmail.setValidationCommand(new RegexValidationCommand(mEmail, "Email is not valid!", ValidationConstants.EMAIL_REGEX));
        mPhone.setValidationCommand(new RegexValidationCommand(mPhone, "Phone number is not valid!", "^\\w{1,10}$"));
        mEmergencyContact.setValidationCommand(new RegexValidationCommand(mEmergencyContact, "Emergency contact is not valid!", "^\\w{1,10}$"));
    }

    private void isEditMode(boolean edit)
    {
        isEditMode = edit;
        mEmail.setEnabled(edit);
        mEmail.clearFocus();
        mPhone.setEnabled(edit);
        mPhone.clearFocus();
        mEmergencyContact.setEnabled(edit);
        mEmergencyContact.clearFocus();

        if(edit)
        {
            mChangePhoto.setVisibility(View.VISIBLE);
            BackButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_close));
            BackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isEditMode(false);
                    mEditButton.setChecked(false);
                    Glide.with(ProfileActivity.this).load(UserCore.Instance().User.getProfilePicture()).centerCrop().into(mProfilePicture);
                    mEmail.cancelEdit();
                    mPhone.cancelEdit();
                    mEmergencyContact.cancelEdit();
                }
            });
        }
        else
        {
            BackButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
            mChangePhoto.setVisibility(View.INVISIBLE);
            BackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   onBackPressed();
                }
            });
        }
    }

    private void saveData()
    {
        if (!areInputsValid())
        {
            return;
        }

        final UserData user = UserCore.Instance().getUser();
        user.setPhone(mPhone.getText().toString());
        user.setEmergencyContact(mEmergencyContact.getText().toString());
        UserCore.Instance().getFUser().updateEmail(mEmail.getText().toString());

        if(mImagePath != null)
        {
            uploadImage(user);
            return;
        }

        FirebaseHelper.Instance().SyncUserData();
    }

    private void uploadImage(final UserData user) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog. setTitle(TranslationConstants.SAVING);
        progressDialog.show();

        StorageReference ref = FirebaseStorage.getInstance().getReference().child("userImages/").child(user.getUserId()).child("profile/");
        FirebaseHelper.Instance().uploadImage(mImagePath, "profileImage", ref).setOnUploadImageListener(new OnUploadImageListener() {
            @Override
            public void onUploadSucces(Uri uri) {
                user.setProfilePicture(uri.toString());
                FirebaseHelper.Instance().SyncUserData();
                progressDialog.dismiss();
            }
        });
    }

    private boolean areInputsValid()
    {
        return mEmail.isValid(true)&&
                mPhone.isValid(true) &&
                mEmergencyContact.isValid(true);
    }

    private void SetTranslationStrings()
    {
        TextInputLayout emailInputLayout = findViewById(R.id.ti_activity_profile_email);
        TextInputLayout phoneInputLayout = findViewById(R.id.ti_activity_profile_phone);
        TextInputLayout emergencyCOntactInputLayout = findViewById(R.id.ti_activity_profile_emergency_contact);

        emailInputLayout.setHint(TranslationConstants.EMAIL);
        phoneInputLayout.setHint(TranslationConstants.PHONE_NUMBER);
        emergencyCOntactInputLayout.setHint(TranslationConstants.EMERGENCY_CONTACT);

//        mEmail.setHint(TranslationConstants.EMAIL);
//        mPhone.setHint(TranslationConstants.PHONE_NUMBER);
//        mEmergencyContact.setHint(TranslationConstants.EMERGENCY_CONTACT);
    }

    //endregion

}
