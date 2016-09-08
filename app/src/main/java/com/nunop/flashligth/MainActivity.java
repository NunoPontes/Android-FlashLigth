package com.nunop.flashligth;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.nunop.flashligth.R;

import java.util.List;

//TODO: implementar o codigo dos motorolas
//TODO: mudar as strings para os icones

public class MainActivity extends Activity {

    //flag to detect flash is on or off
    private boolean isLightOn = false;

    private Camera camera;

    private static final int MY_PERMISSIONS = 1;
    private Camera.Parameters p;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button button = (Button) findViewById(R.id.btnFlashlight);
        Button buttonScreen = (Button) findViewById(R.id.btnScreen);


        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(!isLightOn) {
                    //Turn on LED
                    turnOn(p);
                }
                else {
                    turnOff(p);
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

        //Tries to see if there's a camera, if there is not, the screen will turn to it's maximum brightness
        try {
            releaseCameraAndPreview();
            checkPermissions(Manifest.permission.CAMERA);
            checkPermissions(Manifest.permission.FLASHLIGHT);
            camera = Camera.open();
        } catch (Exception e) {
            button.setVisibility(View.INVISIBLE);
            buttonScreen.setVisibility(View.INVISIBLE);
            putScreen();
            return;
        }

        //Tries to see if the camera has flash, if it doesn't, the screen will turn to it's maximum brightness
        if(!hasFlash(camera)) {
            button.setVisibility(View.INVISIBLE);
            buttonScreen.setVisibility(View.INVISIBLE);
            putScreen();
        }
        else{
            //The device has flash
            p = camera.getParameters();

        }
    }


    @Override
    protected void onResume() {
        //Check permissions, just to see if there is any change
        super.onResume();

        checkPermissions(Manifest.permission.CAMERA);
        checkPermissions(Manifest.permission.FLASHLIGHT);

    }


    @Override
    protected void onStop() {
        //When the activity stops the LED is turned off
        super.onStop();
        turnOffCamera();
    }


    private boolean hasFlash(Camera camera) {
        //boolean Method to determine if the device has a flash LED. Does=true;Doesn't=false

        Context context = this;

        if(context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
            return true;

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

    private void putScreen(){
        //Function to turn the screen white and shiny

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();

        if(params.screenBrightness != 1) {
            params.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL;
            window.setAttributes(params);
            window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        }
        else{
            params.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
            window.setAttributes(params);
        }
    }

    private void releaseCameraAndPreview() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    private void checkPermissions(String permission)
    {
        //TODO: Fazer verificação da string recebida
        //Function that receives a string and it verifies if we have that permission
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), permission);

        switch (permissionCheck)
        {
            case PackageManager.PERMISSION_GRANTED:
                return;
            case PackageManager.PERMISSION_DENIED:
                getPermissions(permission);
                break;
            default:
                break;
        }
    }

    private void getPermissions(String permission)
    {
        //TODO: Fazer verificação da string recebida
        //Function that receives a permission and asks for it
        ActivityCompat.requestPermissions(this, new String[]{permission}, MY_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        //Function called when getPermissions() is called more specifically requestPermissions() and it sends, on this case the int MY_PERMISSIONS
        switch (requestCode) {
            case MY_PERMISSIONS:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    //This restarts the activity
                    Intent i = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    startActivity(i);
                    return;

                } else {

                    //alertDialog("Warning", "Some functions may not work. Are you sure?");

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void turnOn(Camera.Parameters p)
    {
        //Function that receives camera parameters
        //Turn on LED
        if(camera==null)
        {
            camera = Camera.open();
        }

        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);

        camera.setParameters(p);
        camera.startPreview();
        isLightOn = true;
    }

    private void turnOff(Camera.Parameters p)
    {
        //Function that receives camera parameters
        //Turn off LED
        if(camera==null)
        {
            camera = Camera.open();
            p = camera.getParameters();
        }

        p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(p);
        camera.stopPreview();
        isLightOn = false;
    }

    private void turnOffCamera()
    {
        //Function to turn the camera and the flash off

        if (camera != null) {
            if(!hasFlash(camera))
            {
                //if it doesnt have flash you cant setFlashMode
                camera.setPreviewCallback(null);
                camera.stopPreview();
                camera.release();
                camera = null;
            }
            else {
                p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(p);
                camera.setPreviewCallback(null);
                camera.stopPreview();
                camera.release();
                camera = null;
                isLightOn = false;
            }
        }
    }
}
