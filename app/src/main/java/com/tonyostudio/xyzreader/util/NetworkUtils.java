package com.tonyostudio.xyzreader.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;

/**
 * Created by tonyofrancis on 10/18/16.
 */

public final class NetworkUtils {

    private NetworkUtils() {}

    public static boolean isNetworkAvailable(@NonNull Context context) {
        final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
