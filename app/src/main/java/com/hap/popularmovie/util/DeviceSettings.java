package com.hap.popularmovie.util;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by luis on 9/28/17.
 */

public class DeviceSettings {
    public static int getDeviceWidth(final Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int getDeviceHeight(final Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }
}
