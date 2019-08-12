package com.tmz.razvan.mountainapp.activities;

import android.content.Intent;
import android.support.annotation.NonNull;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.tmz.razvan.mountainapp.Core.Models.UserData;
import com.tmz.razvan.mountainapp.Core.UserCore;
import com.tmz.razvan.mountainapp.Helpers.DataValidationHelper;
import com.tmz.razvan.mountainapp.Helpers.FirebaseHelper;
import com.tmz.razvan.mountainapp.R;
import com.tmz.razvan.mountainapp.baseClasses.BaseAppCompat;

public class LoginActivity extends BaseAppCompat {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private TextView mForgotPasswordTextView;
    private Button mEmailSignInButton;
    private Button mRegisterButton;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference userDataReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        setUpViews();
        setStrings();

        mFirebaseAuth = FirebaseAuth.getInstance();

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    return true;
                }
                return false;
            }
        });

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SignIn(mEmailView.getText().toString(), mPasswordView.getText().toString());
            }
        });

        mRegisterButton.setOnClickListener(new OnClickListener() {
           @Override
           public void onClick(View v) {
                onRegister();
           }
       });

        mForgotPasswordTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onForgotPassword();
            }
        });

    }

    @Override
    public void onBackPressed() {
        return;
    }

    private void SignIn(String email, String password) {
        if (!DataValidationHelper.isEmailValid(email) && !DataValidationHelper.isPasswordValid(password)) {
            Toast.makeText(LoginActivity.this, "User or Password incorect", Toast.LENGTH_LONG).show();
            return;
        }
        SetIsBusy(true);
        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            onSignInSuccessfull();
                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onSignInSuccessfull() {
        Toast.makeText(LoginActivity.this, "Authentication succesfull.",
                Toast.LENGTH_SHORT).show();
        final FirebaseUser user = mFirebaseAuth.getCurrentUser();
        UserCore.Instance().setFUser(user);
        UserCore.Instance().User.setUserId(user.getUid());

        database = FirebaseDatabase.getInstance();
        userDataReference = database.getReference("tb_userData");
        DatabaseReference userData = userDataReference.child(user.getUid());
        userData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!UserCore.Instance().getLoggedIn())
                {
                    UserData userData = dataSnapshot.getValue(UserData.class);
                    if(userData == null)
                    {
                        userData = new UserData();
                        userData.setUserId(user.getUid());
                        userData.setEmail(user.getEmail());
                    }
                    if(userData.getUserId().contains(user.getUid()))
                    {
                        UserCore.Instance().setLoggedIn(true);
                        UserCore.Instance().setUser(userData);

                        SetIsBusy(false);

                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);

                        return;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });


        FirebaseMessaging.getInstance().subscribeToTopic("featureUpdate")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
//                        String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
//                            msg = getString(R.string.msg_subscribe_failed);
                            Log.d("subscribe to topics:", "false");
                            return;
                        }
                            Log.d("subscribe to topics:", "true");
//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                        if (!task.isSuccessful()) {
////To do//
//                            return;
//                        }
//
//// Get the Instance ID token//
//                        String token = task.getResult().getToken();
//                        UserCore.Instance().getUser().setToken(token);
//                        FirebaseHelper.Instance().SyncUserData(UserCore.Instance().getUser().getUserId());
//                    }
//                });

    }

    private void onRegister()
    {
        Intent intent = new Intent(LoginActivity.this, RegisterNameActivity.class);
        startActivity(intent);
    }

    private void onForgotPassword()
    {
        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    private void setUpViews()
    {
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mRegisterButton = (Button) findViewById(R.id.btn_activity_sign_in_register);
        mForgotPasswordTextView = findViewById(R.id.tv_activity_login_forgot_password);
    }

    private  void setStrings()
    {
//        mEmailView.setText("thelastgrootstanding@gmail.com");
//        mPasswordView.setText("1pppppppp");
        mEmailView.setText("razvantmz@gmail.com");
        mPasswordView.setText("razvantmz");

    }
}

