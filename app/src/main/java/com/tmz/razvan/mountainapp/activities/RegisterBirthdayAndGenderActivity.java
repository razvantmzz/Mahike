package com.tmz.razvan.mountainapp.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.tmz.razvan.mountainapp.Constants.AppConstants;
import com.tmz.razvan.mountainapp.Constants.NavigationContants;
import com.tmz.razvan.mountainapp.Core.Models.UserData;
import com.tmz.razvan.mountainapp.CustomUIViews.FormEditText;
import com.tmz.razvan.mountainapp.CustomUIViews.ProgressDialog;
import com.tmz.razvan.mountainapp.Helpers.FirebaseHelper;
import com.tmz.razvan.mountainapp.R;
import com.tmz.razvan.mountainapp.TranslationConstants;
import com.tmz.razvan.mountainapp.baseClasses.BaseAppCompat;
import com.tmz.razvan.mountainapp.enums.Gender;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegisterBirthdayAndGenderActivity extends BaseAppCompat {

    private TextView mHeaderTextView;
    private TextView mBirthdayTextView;
    private TextView mGenderTextView;
    private EditText mBirthdayEditText;
    private RadioGroup mGenderRadioGroup;
    private RadioButton mMaleRadioButton;
    private RadioButton mFemaleRadioButton;
    Calendar myCalendar = Calendar.getInstance();
    private FloatingActionButton mNextButton;
    UserData newUser;

    boolean dateChoosen;
    Gender choosenGender;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_birthday_and_gender);

        String serialisedUserData = getIntent().getStringExtra(NavigationContants.USER_DATA);
        newUser = new Gson().fromJson(serialisedUserData, UserData.class);

        mAuth = FirebaseAuth.getInstance();

        setUpViews();
        setTranslationStrings();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setColors();
    }

    //listeners

    View.OnClickListener nextButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            newUser.setGender(choosenGender.ordinal());
            newUser.setDateOfBirth(myCalendar.getTime());
           createUser();
        }
    };

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    RadioGroup.OnCheckedChangeListener genderRadioGroupListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            if(i == R.id.rb_activity_birthday_and_gender_gender_male)
            {
                choosenGender = Gender.MALE;
            }
            else if (i == R.id.rb_activity_birthday_and_gender_gender_female)
            {
                choosenGender = Gender.FEMALE;
            }
            validateInputs();
        }
    };

    //region register

    private void createUser()
    {
        final android.app.ProgressDialog progressDialog = new android.app.ProgressDialog(this);
//        progressDialog. setTitle("Uploading...");
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(newUser.getEmail(), getIntent().getStringExtra(NavigationContants.USER_PASSWORD))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        task.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            newUser.setUserId(mAuth.getCurrentUser().getUid());
                                            newUser.setProfilePicture(AppConstants.DEFAULT_PROFILE_PICTURE);
                                            FirebaseHelper.Instance().SyncUserData(newUser);
                                            progressDialog.dismiss();
                                            Intent intent = new Intent(RegisterBirthdayAndGenderActivity.this, RegisterSuccesfullyActivity.class);
                                            RegisterBirthdayAndGenderActivity.this.startActivity(intent);
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
    }

    //

    //region ui methods

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateChoosen = true;
        mBirthdayEditText.setText(sdf.format(myCalendar.getTime()));
    }

    private void validateInputs()
    {
        if(dateChoosen && choosenGender != null)
        {
            mNextButton.setEnabled(true);
        }
    }

    private void setUpViews()
    {
        mHeaderTextView = findViewById(R.id.tv_activity_register_birthday_and_gender_header);
        mBirthdayTextView = findViewById(R.id.tv_activity_birthday_and_gender_birthday);
        mGenderTextView = findViewById(R.id.tv_activity_birthday_and_gender_gender);
        mBirthdayEditText = findViewById(R.id.et_activity_register_birthday_and_gender_birthday);
        mGenderRadioGroup = findViewById(R.id.rg_activity_birthday_and_gender);
        mMaleRadioButton = findViewById(R.id.rb_activity_birthday_and_gender_gender_male);
        mFemaleRadioButton = findViewById(R.id.rb_activity_birthday_and_gender_gender_female);
        mNextButton = findViewById(R.id.btn_activity_register_birthday_and_gender_next);

        mNextButton.setOnClickListener(nextButtonClickListener);
        mNextButton.setEnabled(false);
        mGenderRadioGroup.setOnCheckedChangeListener(genderRadioGroupListener);
        mBirthdayEditText.setFocusable(false);
        mBirthdayEditText.setLongClickable(false);
        mBirthdayEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(RegisterBirthdayAndGenderActivity.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void setTranslationStrings()
    {
        mHeaderTextView.setText(TranslationConstants.REGISTER_BIRTHDAY_HEADER);
        mBirthdayTextView.setText(TranslationConstants.BIRTHDAY);
        mGenderTextView.setText(TranslationConstants.GENDER);
        mFemaleRadioButton.setText(TranslationConstants.FEMALE);
        mMaleRadioButton.setText(TranslationConstants.MALE);
        mBirthdayEditText.setText(TranslationConstants.DATE_PLACEHOLDER);
    }

    private void setColors()
    {
        Toolbar.setBackgroundColor(getResources().getColor(R.color.lightBlue));
        mNextButton.getBackgroundTintList();
    }

    //endregion
}
