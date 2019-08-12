package com.tmz.razvan.mountainapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tmz.razvan.mountainapp.R;
import com.tmz.razvan.mountainapp.TranslationConstants;
import com.tmz.razvan.mountainapp.baseClasses.BaseAppCompat;

public class RegisterSuccesfullyActivity extends BaseAppCompat {

    private TextView headerTextView;
    private Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_succesfully);

        SetUpViews();
        SetStrings();

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterSuccesfullyActivity.this, LoginActivity.class);
                RegisterSuccesfullyActivity.this.startActivity(intent);
            }
        });
    }

    //region private methods

    private void SetUpViews()
    {
        headerTextView = findViewById(R.id.tv_register_succesfully_header);
        continueButton = findViewById(R.id.btn_register_succesfully_continue);
    }

    private void SetStrings()
    {
        headerTextView.setText(TranslationConstants.REGISTER_SUCCESFULLY_HEADER);
        continueButton.setText(TranslationConstants.SIGN_IN);
    }

    //endregion

}
