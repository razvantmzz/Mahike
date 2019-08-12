package com.tmz.razvan.mountainapp.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.tmz.razvan.mountainapp.Constants.ValidationConstants;
import com.tmz.razvan.mountainapp.CustomUIViews.FormEditText;
import com.tmz.razvan.mountainapp.R;
import com.tmz.razvan.mountainapp.TranslationConstants;
import com.tmz.razvan.mountainapp.baseClasses.BaseAppCompat;
import com.tmz.razvan.mountainapp.validationCommands.RegexValidationCommand;

import java.text.Normalizer;

public class ForgotPasswordActivity extends BaseAppCompat {

    private TextView headerTextView;
    private FormEditText emailEditText;
    private Button recoverPasswordButton;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        SetUpViews();
        SetStrings();
        mAuth = FirebaseAuth.getInstance();

        recoverPasswordButton.setOnClickListener(forgotPasswordClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SetToolbarTitle(TranslationConstants.RECOVER_PASSWORD);
    }

    //region listeners

    View.OnClickListener forgotPasswordClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!emailEditText.isValid(true))
            {
                return;
            }

            mAuth.sendPasswordResetEmail(emailEditText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(ForgotPasswordActivity.this, TranslationConstants.EMAIL_SEND_SUCCESFULLY, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(ForgotPasswordActivity.this, TranslationConstants.EMAIL_SEND_FAIL, Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    //endregion

    //region private methods

    private void SetUpViews()
    {
        headerTextView = findViewById(R.id.tv_activity_forgot_password_header);
        emailEditText = findViewById(R.id.et_activity_forgot_password_email);
        recoverPasswordButton = findViewById(R.id.btn_forgot_password_recover_password);

        emailEditText.setValidationCommand(new RegexValidationCommand(emailEditText, "Email is not valid!", ValidationConstants.EMAIL_REGEX));
    }

    private void SetStrings()
    {
        headerTextView.setText(TranslationConstants.FORGOT_PASSWORD_HEADER);
        emailEditText.setHint(TranslationConstants.EMAIL);
        recoverPasswordButton.setText(TranslationConstants.RECOVER_PASSWORD);
    }

    //endregion
}
