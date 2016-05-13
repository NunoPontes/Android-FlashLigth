package com.example.nunop.flashligth;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    //flag to detect flash is on or off
    private boolean isLighOn = false;

    private Camera camera;

    private Button button, buttonScreen;





    @Override
    protected void onStop() {
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
        try {
            camera = Camera.open();
        }
        catch(Exception e)
        {
            Log.e("err", "Erro ao abrir a camâra!");
            return;
        }
        final Camera.Parameters p = camera.getParameters();

        // if device support camera?
       // if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
         //   Log.e("err", "Device has no camera!");
         //   return;
        //}




        if(hasFlash(camera)==false) {
            Log.e("err", "Device has no flash!");
            //Starting a new Intent
            Intent nextScreen = new Intent(getApplicationContext(), Screen.class);
            startActivity(nextScreen);
        }
        else{
            Log.i("info", "Device has flash!");


            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    if (isLighOn) {

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

                @Override
                public void onClick(View arg0) {
                    //Starting a new Intent
                    Intent nextScreen = new Intent(getApplicationContext(), Screen.class);
                    startActivity(nextScreen);
                }
            });
        }
    }

    public boolean hasFlash(Camera camera) {
        if (camera == null) {
            return false;
        }

        Camera.Parameters parameters = camera.getParameters();

        if (parameters.getFlashMode() == null) {
            return false;
        }

        List<String> supportedFlashModes = parameters.getSupportedFlashModes();
        if (supportedFlashModes == null || supportedFlashModes.isEmpty() || supportedFlashModes.size() == 1 && supportedFlashModes.get(0).equals(Camera.Parameters.FLASH_MODE_OFF)) {
            return false;
        }

        return true;
    }
}
