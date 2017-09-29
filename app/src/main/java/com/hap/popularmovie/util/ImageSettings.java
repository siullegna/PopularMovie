package com.hap.popularmovie.util;

import android.app.Activity;

/**
 * Created by luis on 9/15/17.
 */

public class ImageSettings {
    private static final String BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String SIZE_W92 = "w92";
    private static final String SIZE_W154 = "w154";
    public static final String SIZE_W185 = "w185"; // recommended for most of the phones.
    private static final String SIZE_W342 = "w342";
    private static final String SIZE_W500 = "w500";
    public static final String SIZE_W780 = "w780";
    private static final String SIZE_ORIGINAL = "original";

    public static String getBasePhotoUrl(final String size) {
        return ImageSettings.BASE_URL + size;
    }

    public static int getItemSize(final Activity activity, final int cols) {
        final int width = DeviceSettings.getDeviceWidth(activity);
        return width / cols;
    }
}
