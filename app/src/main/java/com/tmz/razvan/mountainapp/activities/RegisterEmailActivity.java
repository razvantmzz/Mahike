package com.tmz.razvan.mountainapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tmz.razvan.mountainapp.Constants.NavigationContants;
import com.tmz.razvan.mountainapp.Constants.ValidationConstants;
import com.tmz.razvan.mountainapp.Core.Models.UserData;
import com.tmz.razvan.mountainapp.CustomUIViews.FormEditText;
import com.tmz.razvan.mountainapp.R;
import com.tmz.razvan.mountainapp.TranslationConstants;
import com.tmz.razvan.mountainapp.baseClasses.BaseAppCompat;
import com.tmz.razvan.mountainapp.models.HikeModel;
import com.tmz.razvan.mountainapp.validationCommands.RegexValidationCommand;

public class RegisterEmailActivity extends BaseAppCompat {

    private TextView mHeaderTextView;
    private TextView mEmailTextView;
    private FormEditText mEmailEditText;
    private FloatingActionButton mNextButton;
    UserData newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_email);

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
            if(mEmailEditText.isValid(false))
            {
                mNextButton.setEnabled(true);
                return;
            }
            mNextButton.setEnabled(false);
        }
    };

    View.OnClickListener nextButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            newUser.setEmail(mEmailEditText.getText().toString());
            Intent intent = new Intent(RegisterEmailActivity.this, RegisterPasswordActivity.class);
            String serialisedNewUser = new Gson().toJson(newUser);
            intent.putExtra(NavigationContants.USER_DATA, serialisedNewUser);
            RegisterEmailActivity.this.startActivity(intent);
        }
    };

    //

    //region ui methods

    private void setUpViews()
    {
        mHeaderTextView = findViewById(R.id.tv_activity_register_email_header);
        mEmailTextView = findViewById(R.id.tv_activity_register_email);
        mEmailEditText = findViewById(R.id.et_activity_register_email);
        mNextButton = findViewById(R.id.btn_activity_register_email_next);

        mEmailEditText.addTextChangedListener(inputsTextWatcher);
        mEmailEditText.setValidationCommand(new RegexValidationCommand(mEmailEditText, "Email is not valid", ValidationConstants.EMAIL_REGEX));
        mNextButton.setOnClickListener(nextButtonClickListener);
        mNextButton.setEnabled(false);
    }

    private void setTranslationStrings()
    {
        mHeaderTextView.setText(TranslationConstants.REGISTER_EMAIL_HEADER);
        mEmailTextView.setText(TranslationConstants.EMAIL);
    }

    private void setColors()
    {
        Toolbar.setBackgroundColor(getResources().getColor(R.color.lightBlue));
        mNextButton.getBackgroundTintList();
    }
    //endregion
}
