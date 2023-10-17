package com.termux.revancify;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public final class MainActivity extends Activity {
    /**
     * Permission request code
     */
    public static final int REQUEST_PERMISSION = 11;

    /**
     * Revancify github
     */
    private static final String REVANCIFY_GITHUB = "https://github.com/decipher3114/Revancify#installation";

    /**
     * Revancify path
     */
    @SuppressLint("SdCardPath")
    private static final String REVANCIFY_PATH = "/data/data/com.termux/files/home/Revancify/revancify";

    /**
     * Target permission name
     */
    private static final String TARGET_PERMISSION = "com.termux.permission.RUN_COMMAND";

    /**
     * Termux package name
     */
    private static final String TERMUX_PACKAGE_NAME = "com.termux";

    /**
     * Termux work dir path
     */
    @SuppressLint("SdCardPath")
    private static final String TERMUX_WORKDIR = "/data/data/com.termux/files/home";

    /**
     * Termux monet releases
     */
    private static final String TERMUX_MONET_RELEASES = "https://github.com/HardcodedCat/termux-monet/releases/latest";


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        Helper.activity = this;
        checkTermuxEnabled();
        checkPermissionAndRunTermux();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                runTermux();
            } else {
                Helper.showToastShort(getString(R.string.toast_require_permission));
                finish();
            }
        }
    }

    private void checkTermuxEnabled() {
        if (!Helper.isPackageEnabled(TERMUX_PACKAGE_NAME)) {
            Helper.showToastShort(getString(R.string.toast_termux_not_found));
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(TERMUX_MONET_RELEASES));
            startActivity(intent);

            finish();
        }
    }

    private void checkPermissionAndRunTermux() {
        final int checkSelfPermission = ContextCompat.checkSelfPermission(this, TARGET_PERMISSION);
        final boolean shouldShowRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(this, TARGET_PERMISSION);

        if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermission) {
                Helper.showToastShort(getString(R.string.toast_require_permission));
            }

            ActivityCompat.requestPermissions(this, new String[]{
                    TARGET_PERMISSION}, REQUEST_PERMISSION);
            finish();
        } else {
            runTermux();
        }
    }

    /**
     * Open termux
     */
    private void runTermux() {
        try {
            Intent intent = new Intent();
            intent.setClassName("com.termux", "com.termux.app.RunCommandService");
            intent.setAction("com.termux.RUN_COMMAND");
            intent.putExtra("com.termux.RUN_COMMAND_PATH", REVANCIFY_PATH);
            intent.putExtra("com.termux.RUN_COMMAND_WORKDIR", TERMUX_WORKDIR);
            intent.putExtra("com.termux.RUN_COMMAND_BACKGROUND", false);
            intent.putExtra("com.termux.RUN_COMMAND_SESSION_ACTION", "0");
            startForegroundService(intent);

            Helper.showToastShort(getString(R.string.toast_run_revancify));
        } catch (Exception ex) {
            Helper.showToastShort(getString(R.string.toast_revancify_not_found));
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(REVANCIFY_GITHUB));
            startActivity(intent);
        }
        finish();
    }
}
