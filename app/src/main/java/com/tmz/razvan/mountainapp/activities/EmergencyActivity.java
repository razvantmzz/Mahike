package com.tmz.razvan.mountainapp.activities;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.tmz.razvan.mountainapp.R;
import com.tmz.razvan.mountainapp.Runnable.SOSRunable;
import com.tmz.razvan.mountainapp.TranslationConstants;
import com.tmz.razvan.mountainapp.baseClasses.BaseAppCompat;

public class EmergencyActivity extends BaseAppCompat {

    private Button sosButton;

//    private CameraManager camManager;
//    private boolean sosThreadRunning;
//    private Thread sosThread;
    private SOSRunable sosSignal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        sosSignal = SOSRunable.getInstance(this);

        setUpViews();
        setStrings();

        sosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sosSignal != null && sosSignal.isRunning)
                {
                    sosSignal.stop();
//                    sosSignal.stop();
//                    sosThread.interrupt();
                    sosButton.setText(TranslationConstants.SOS_SIGNAL);
//                    sosThreadRunning = false;
                    return;
                }
                sosSignal.start();
//                camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
//                String cameraId = null; // Usually front camera is at 0 position.

//                sosThread = new Thread(sosSignal);
//                sosThread.start();
//                sosThreadRunning = true;
                sosButton.setText("Stop");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    //region private methods

    private void setUpViews()
    {
        sosButton = findViewById(R.id.btn_activity_emergency_sos);
    }

    private void setStrings()
    {
        if(sosSignal.isRunning)
        {
            sosButton.setText("Stop");
        }
        else
        {
            sosButton.setText(TranslationConstants.SOS_SIGNAL);
        }
    }

    //endregion

}
