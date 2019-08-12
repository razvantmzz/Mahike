package com.tmz.razvan.mountainapp.activities;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.tmz.razvan.mountainapp.Constants.NavigationContants;
import com.tmz.razvan.mountainapp.Constants.ValidationConstants;
import com.tmz.razvan.mountainapp.Core.Models.UserData;
import com.tmz.razvan.mountainapp.CustomUIViews.FormEditText;
import com.tmz.razvan.mountainapp.R;
import com.tmz.razvan.mountainapp.TranslationConstants;
import com.tmz.razvan.mountainapp.baseClasses.BaseAppCompat;
import com.tmz.razvan.mountainapp.validationCommands.RegexValidationCommand;

public class RegisterPasswordActivity extends BaseAppCompat {

    private TextView mHeaderTextView;
    private TextView mSubHeaderTextView;
    private TextView mPasswordTextView;
    private FormEditText mPasswordEditText;
    private FloatingActionButton mNextButton;
    private ToggleButton mPasswordControlButton;
    UserData newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_password);

        String serialisedUserData = getIntent().getStringExtra(NavigationContants.USER_DATA);
        newUser = new Gson().fromJson(serialisedUserData, UserData.class);

        setUpViews();
        setTranslationStrings();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setColors();
    }

    //listeners

    TextWatcher inputsTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(mPasswordEditText.isValid(false))
            {
                mNextButton.setEnabled(true);
                return;
            }
            mNextButton.setEnabled(false);
        }
    };

    View.OnClickListener passwordControllClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mPasswordControlButton.isChecked()) {
                mPasswordEditText.setTransformationMethod(null);
            } else {
                mPasswordEditText.setTransformationMethod(new PasswordTransformationMethod());
            }
        }
    };

    View.OnClickListener nextButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(RegisterPasswordActivity.this, RegisterBirthdayAndGenderActivity.class);
            String serialisedNewUser = new Gson().toJson(newUser);
            intent.putExtra(NavigationContants.USER_DATA, serialisedNewUser);
            intent.putExtra(NavigationContants.USER_PASSWORD, mPasswordEditText.getText().toString());
            RegisterPasswordActivity.this.startActivity(intent);
        }
    };

    //

    //region ui methods

    private void setUpViews()
    {
        mHeaderTextView = findViewById(R.id.tv_activity_register_password_header);
        mSubHeaderTextView = findViewById(R.id.tv_activity_register_password_sub_header);
        mPasswordTextView = findViewById(R.id.tv_activity_register_password);
        mPasswordEditText = findViewById(R.id.et_activity_register_password);
        mNextButton = findViewById(R.id.btn_activity_register_password_next);
        mPasswordControlButton = findViewById(R.id.btn_activity_register_password_control);

        mPasswordEditText.addTextChangedListener(inputsTextWatcher);
        mPasswordEditText.setValidationCommand(new RegexValidationCommand(mPasswordEditText, "Password is not valid", ValidationConstants.PASSWORD_REGEX));
        mPasswordControlButton.setOnClickListener(passwordControllClickListener);
        mNextButton.setOnClickListener(nextButtonClickListener);
        mNextButton.setEnabled(false);
    }

    private void setTranslationStrings()
    {
        mHeaderTextView.setText(TranslationConstants.REGISTER_PASSWORD_HEADER);
        mSubHeaderTextView.setText(TranslationConstants.REGISTER_PASSWORD_SUB_HEADER);
        mPasswordTextView.setText(TranslationConstants.PASSWORD);

        mPasswordControlButton.setText(TranslationConstants.PASSWORD_SHOW);
        mPasswordControlButton.setTextOff(TranslationConstants.PASSWORD_SHOW);
        mPasswordControlButton.setTextOn(TranslationConstants.PASSWORD_HIDE);
    }

    private void setColors()
    {
        Toolbar.setBackgroundColor(getResources().getColor(R.color.lightBlue));
        mNextButton.getBackgroundTintList();
    }
    //endregion

}
