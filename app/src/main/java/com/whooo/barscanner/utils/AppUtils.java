package com.whooo.barscanner.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.whooo.barscanner.R;

public class AppUtils {

    //no need to create instance for this class
    private AppUtils() {
    }

    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }

    public static boolean isInternetOn(Context ctx) {
        try {
            if (isNetworkAvailable(ctx)) {
                return true;

            } else {
                Log.d("[NetworkUtils]:isInternetOn No network available!");
                return false;
            }
        } catch (Exception ex) {
            Log.e(ex.getMessage());
            return false;
        }
    }

    public static boolean isCameraAvailable(Context context) {
        PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    private static boolean isNetworkAvailable(Context ctx) {
        try {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null;
        } catch (Exception ex) {
            Log.e(ex.getMessage());
            return false;
        }
    }

}
