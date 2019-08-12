package com.tmz.razvan.mountainapp.activities;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tmz.razvan.mountainapp.CustomUIViews.FormEditText;
import com.tmz.razvan.mountainapp.R;
import com.tmz.razvan.mountainapp.TranslationConstants;
import com.tmz.razvan.mountainapp.baseClasses.BaseAppCompat;

public class RegisterActivity extends BaseAppCompat {

    private FormEditText mName;
    private FormEditText mEmail;
    private FormEditText mPassword;
    private FormEditText mConfirmPassword;
    private RadioGroup mGenderRadioGroup;
    private RadioButton mMaleRadioButton;
    private RadioButton mFemaleRadioButton;
    private EditText mBirthdayEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setUpViews();
        initialseViews();
        setTranslationStrings();
    }

    private void setUpViews()
    {
        mName = findViewById(R.id.et_activity_register_name);
        mEmail = findViewById(R.id.et_activity_register_email);
        mPassword = findViewById(R.id.et_activity_register_password);
        mConfirmPassword = findViewById(R.id.et_activity_register_confirm_password);
        mGenderRadioGroup = findViewById(R.id.rg_activity_register_gender);
        mMaleRadioButton = findViewById(R.id.rb_activity_register_male);
        mFemaleRadioButton = findViewById(R.id.rb_activity_register_female);
        mBirthdayEditText = findViewById(R.id.et_activity_register_birthday);
    }

    private void initialseViews()
    {
        mBirthdayEditText.setFocusable(false);
        mBirthdayEditText.setLongClickable(false);
    }

    private void setTranslationStrings()
    {
        TextInputLayout mNameInputLayout = findViewById(R.id.ti_activity_register_name);
        TextInputLayout mEmailInputLayout = findViewById(R.id.ti_activity_register_email);
        TextInputLayout mPasswordInputLayout = findViewById(R.id.ti_activity_register_password);
        TextInputLayout mConfirmPasswordInputLayout = findViewById(R.id.ti_activity_register_confirm_password);

        mNameInputLayout.setHint(TranslationConstants.NAME);
        mEmailInputLayout.setHint(TranslationConstants.EMAIL);
        mPasswordInputLayout.setHint(TranslationConstants.PASSWORD);
        mConfirmPasswordInputLayout.setHint(TranslationConstants.CONFIRM_PASSWORD);
    }
}
