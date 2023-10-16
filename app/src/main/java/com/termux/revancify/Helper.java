package com.termux.revancify;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class Helper {
    @SuppressLint("StaticFieldLeak")
    public static Activity activity;

    private Helper() {
    } // helper class

    public static void showToastShort(@NonNull String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    public static boolean isPackageEnabled(@NonNull String packageName) {
        try {
            return activity.getPackageManager().getApplicationInfo(packageName, 0).enabled;
        } catch (PackageManager.NameNotFoundException ignored) {
        }

        return false;
    }
}