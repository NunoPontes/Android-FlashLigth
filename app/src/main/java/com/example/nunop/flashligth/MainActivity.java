package com.example.nunop.flashligth;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.util.List;

public class MainActivity extends Activity {

    //flag to detect flash is on or off
    private boolean isLighOn = false;

    private Camera camera;

    private Button button, buttonScreen;


    public void putScreen(){

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();

        if(params.screenBrightness != 1) {
            Log.i("info", "Screen is turn on!");
            params.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL;
            window.setAttributes(params);
            window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        }
        else{
            Log.i("info", "Screen is turn off!");
            params.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
            window.setAttributes(params);
        }
    }

    @Override
    protected void onStop() {
        //When the activity stops the LED is turned off
        super.onStop();

        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera=null;
            isLighOn = false;
        }
    }

    @Override
    protected void onPause(){
        //When the activity pauses the LED is turned off
        super.onPause();
        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera=null;
            isLighOn = false;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.btnFlashlight);
        buttonScreen = (Button) findViewById(R.id.btnScreen);
        Context context = this;
        PackageManager pm = context.getPackageManager();



        // if device support camera?
       // if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
         //   Log.e("err", "Device has no camera!");
         //   return;
        //}

        //Tries to see if there's a camera, if there is not, the screen will turn to it's maximum brightness
        try {
            camera = Camera.open();
        } catch (Exception e) {
            Log.e("err", "Error opening the camera!");
            putScreen();
            return;
        }


        //Tries to see if the camera has flash, if it doesn't, the screen will turn to it's maximum brightness
        if(hasFlash(camera)==false) {
            Log.e("err", "Device has no flash!");
            putScreen();
        }
        else{
            //The device has flash
            final Camera.Parameters p = camera.getParameters();


            Log.i("info", "Device has flash!");


            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    if (isLighOn) {
                        //Turn off LED
                        if(camera==null)
                        {
                            camera = Camera.open();
                            final Camera.Parameters p = camera.getParameters();
                        }
                        Log.i("info", "torch is turn off!");

                        p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        camera.setParameters(p);
                        camera.stopPreview();
                        isLighOn = false;

                    } else {
                        //Turn on LED
                        if(camera==null)
                        {
                            camera = Camera.open();
                            final Camera.Parameters p = camera.getParameters();
                        }

                        Log.i("info", "torch is turn on!");

                        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);

                        camera.setParameters(p);
                        camera.startPreview();
                        isLighOn = true;

                    }
                }
            });

            buttonScreen.setOnClickListener(new View.OnClickListener() {
                //When you have LED but you want to put light on the screen
                @Override
                public void onClick(View arg0) {
                    putScreen();
                }
            });
        }
    }

    public boolean hasFlash(Camera camera) {
        //boolean Method to determine if the device has a flash LED. Does=true;Doesn't=false

        if (camera == null) {
            camera.stopPreview();
            return false;
        }

        Camera.Parameters parameters = camera.getParameters();

        if (parameters.getFlashMode() == null) {
            camera.stopPreview();
            return false;
        }

        List<String> supportedFlashModes = parameters.getSupportedFlashModes();
        if (supportedFlashModes == null || supportedFlashModes.isEmpty() || supportedFlashModes.size() == 1 && supportedFlashModes.get(0).equals(Camera.Parameters.FLASH_MODE_OFF)) {
            camera.stopPreview();
            return false;
        }

        return true;
    }
}
