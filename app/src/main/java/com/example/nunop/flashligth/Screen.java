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
}
