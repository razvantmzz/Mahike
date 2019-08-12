package com.tmz.razvan.mountainapp.Runnable;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;

public class SOSRunable implements Runnable {

    public boolean isRunning;
    boolean isFlashLightOn;
    private CameraManager camManager;
    private String cameraId;

    private static SOSRunable sosRunable;
    private static Thread sosThread;

    public SOSRunable(Context context)
    {
        camManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
    }

    public static SOSRunable getInstance(Context context)
    {
        if(sosRunable == null)
        {
            sosRunable = new SOSRunable(context);
        }
        return sosRunable;
    }

    @Override
    public void run() {
         cameraId = null; // Usually front camera is at 0 position.
        try {
            while (isRunning)
            {

                cameraId = camManager.getCameraIdList()[0];

                for (int j = 0; j < 6; j++)
                {
                    isFlashLightOn = !isFlashLightOn;
                    camManager.setTorchMode(cameraId, isFlashLightOn);
                    Thread.sleep(1000);
                }

//                isFlashLightOn = false;

                for (int j = 0; j < 3; j++)
                {
                    isFlashLightOn = !isFlashLightOn;
                    camManager.setTorchMode(cameraId, isFlashLightOn);
                    Thread.sleep(3000);
                    isFlashLightOn = !isFlashLightOn;
                    camManager.setTorchMode(cameraId, isFlashLightOn);
                    Thread.sleep(1000);
                }

                for (int j = 0; j < 6 ; j++)
                {
                    isFlashLightOn = !isFlashLightOn;
                    camManager.setTorchMode(cameraId, isFlashLightOn);
                    Thread.sleep(1000);
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public synchronized void stop()
    {
        isRunning = false;
        sosThread.interrupt();

        try {
            camManager.setTorchMode(cameraId, false);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public synchronized void start()
    {
        if(sosThread == null)
        {
            sosThread = new Thread(sosRunable);
        }
        if(sosThread.isInterrupted())
        {
            sosThread.run();
        }
        else
        {
            sosThread.start();
        }
        isRunning = true;
    }


}
