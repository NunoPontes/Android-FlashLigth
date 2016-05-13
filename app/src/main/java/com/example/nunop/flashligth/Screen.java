package com.example.nunop.flashligth;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class Screen extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        // Flashlights should be bright.
        WindowManager.LayoutParams params = window.getAttributes();
        params.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL;
        // params.buttonBrightness was added in API level 8. The additional brightness is not worth the added code size.
        // The screen will go to max brightness even without the following line, but the API doesn't guarantee it.
        window.setAttributes(params);
        window.addFlags(
                // Use the power button to turn flashlight off and on, even if you have a lock screen.
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        // We won't do anything with touch events. Don't bother sending them to us.
                        // Unfortunately, there is a platform bug that still exists in 4.2 (!) that causes ANRs if this
                        // flag is set.
                        // | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        // A flashlight that turns itself off isn't a good flashlight.
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        // Turn the screen on if it isn't already on when launching (e.g., from ADB).
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }


/*
    public void changeScreen(){
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.screenBrightness = 1;
        getWindow().setAttributes(layoutParams);

        float curBrightnessValue = 255;
        try {
            curBrightnessValue = android.provider.Settings.System.getInt(getContentResolver(),android.provider.Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.screenBrightness = 1.0f;
        getWindow().setAttributes(params);
        //changeScreen();
    }
*/
    /*
   private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);
        seekBar = (SeekBar) findViewById(R.id.seekBar1);
        seekBar.setMax(255);

        float curBrightnessValue = 0;
        try {
            curBrightnessValue = android.provider.Settings.System.getInt(getContentResolver(),android.provider.Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        int screen_brightness = (int) curBrightnessValue;
        seekBar.setProgress(screen_brightness);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue,
                                          boolean fromUser) {
                progress = progresValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do something here,
                // if you want to do anything at the start of
                // touching the seekbar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                android.provider.Settings.System.putInt(getContentResolver(),
                        android.provider.Settings.System.SCREEN_BRIGHTNESS,
                        progress);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
*/
}
