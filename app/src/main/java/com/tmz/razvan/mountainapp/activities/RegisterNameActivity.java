package com.tmz.razvan.mountainapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tmz.razvan.mountainapp.Constants.NavigationContants;
import com.tmz.razvan.mountainapp.Core.Models.UserData;
import com.tmz.razvan.mountainapp.R;
import com.tmz.razvan.mountainapp.TranslationConstants;
import com.tmz.razvan.mountainapp.baseClasses.BaseAppCompat;

public class RegisterNameActivity extends BaseAppCompat {

    private TextView mHeaderTextView;
    private TextView mFirstName;
    private TextView mLastName;
    private EditText mFirstNameEditText;
    private EditText mLastNameEditText;
    private FloatingActionButton mNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_name);

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
            if(!mFirstNameEditText.getText().toString().equals("") && !mLastNameEditText.getText().toString().equals(""))
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
            UserData newUser = new UserData();
            newUser.setFullName(String.format("%s %s" , mFirstNameEditText.getText(), mLastNameEditText.getText()));
            Intent intent = new Intent(RegisterNameActivity.this, RegisterEmailActivity.class);
            String serialisedNewUser = new Gson().toJson(newUser);
            intent.putExtra(NavigationContants.USER_DATA, serialisedNewUser);
            RegisterNameActivity.this.startActivity(intent);
        }
    };

    //

    //region ui methods

    private void setUpViews()
    {
        mHeaderTextView = findViewById(R.id.tv_activity_register_name_header);
        mFirstName = findViewById(R.id.tv_activity_register_first_name);
        mLastName = findViewById(R.id.tv_activity_register_last_name);
        mFirstNameEditText = findViewById(R.id.et_activity_register_first_name);
        mLastNameEditText = findViewById(R.id.et_activity_register_last_name);
        mNextButton = findViewById(R.id.btn_activity_register_name_next);

        mFirstNameEditText.addTextChangedListener(inputsTextWatcher);
        mLastNameEditText.addTextChangedListener(inputsTextWatcher);
        mNextButton.setOnClickListener(nextButtonClickListener);
        mNextButton.setEnabled(false);
    }

    private void setTranslationStrings()
    {
        mHeaderTextView.setText(TranslationConstants.REGISTER_NAME_HEADER);
        mFirstName.setText(TranslationConstants.FIRST_NAME);
        mLastName.setText(TranslationConstants.LAST_NAME);
    }

    private void setColors()
    {
        Toolbar.setBackgroundColor(getResources().getColor(R.color.lightBlue));
        mNextButton.getBackgroundTintList();
    }
    //endregion
}
