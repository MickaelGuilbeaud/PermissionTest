package com.mickaelg.permissionstest.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.StringDef;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.mickaelg.permissionstest.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Class gathering permissions related methods.
 * Created by mickaelg on 14/10/2015.
 */
public class PermissionsUtils {

    /**
     * Possible states of a permission.
     */
    @IntDef({PERMISSIONS_UTILS_GRANTED, PERMISSIONS_UTILS_DENIED,
            PERMISSIONS_UTILS_SHOULD_SHOW_RATIONALE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PermissionState {
    }

    public static final int PERMISSIONS_UTILS_GRANTED = 0;
    public static final int PERMISSIONS_UTILS_DENIED = 1;
    public static final int PERMISSIONS_UTILS_SHOULD_SHOW_RATIONALE = 2;

    /**
     * Permissions as String.
     */
    @StringDef({PERMISSION_READ_CALENDAR, PERMISSION_WRITE_CALENDAR,
            PERMISSION_CAMERA, PERMISSION_READ_CONTACTS,
            PERMISSION_WRITE_CONTACTS, PERMISSION_READ_PROFILE,
            PERMISSION_WRITE_PROFILE, PERMISSION_ACCESS_FINE_LOCATION,
            PERMISSION_ACCESS_COARSE_LOCATION, PERMISSION_RECORD_AUDIO,
            PERMISSION_READ_PHONE_STATE, PERMISSION_CALL_PHONE,
            PERMISSION_READ_CALL_LOG, PERMISSION_WRITE_CALL_LOG,
            PERMISSION_ADD_VOICEMAIL, PERMISSION_USE_SIP,
            PERMISSION_PROCESS_OUTGOING_CALLS, PERMISSION_BODY_SENSORS,
            PERMISSION_USE_FINGERPRINT, PERMISSION_SEND_SMS,
            PERMISSION_RECEIVE_SMS, PERMISSION_READ_SMS,
            PERMISSION_RECEIVE_WAP_PUSH, PERMISSION_RECEIVE_MMS,
            PERMISSION_READ_CELL_BROADCASTS, PERMISSION_READ_EXTERNAL_STORAGE,
            PERMISSION_WRITE_EXTERNAL_STORAGE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PermissionString {
    }

    // Calendar
    public static final String PERMISSION_READ_CALENDAR = "android.permission.READ_CALENDAR";
    public static final String PERMISSION_WRITE_CALENDAR = "android.permission.WRITE_CALENDAR";
    // Camera
    public static final String PERMISSION_CAMERA = "android.permission.CAMERA";
    // Contacts
    public static final String PERMISSION_READ_CONTACTS = "android.permission.READ_CONTACTS";
    public static final String PERMISSION_WRITE_CONTACTS = "android.permission.WRITE_CONTACTS";
    public static final String PERMISSION_READ_PROFILE = "android.permission.READ_PROFILE";
    public static final String PERMISSION_WRITE_PROFILE = "android.permission.WRITE_PROFILE";
    // Location
    public static final String PERMISSION_ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";
    public static final String PERMISSION_ACCESS_COARSE_LOCATION = "android.permission.ACCESS_COARSE_LOCATION";
    // Microphone
    public static final String PERMISSION_RECORD_AUDIO = "android.permission.RECORD_AUDIO";
    public static final String PERMISSION_READ_PHONE_STATE = "android.permission.READ_PHONE_STATE";
    public static final String PERMISSION_CALL_PHONE = "android.permission.CALL_PHONE";
    public static final String PERMISSION_READ_CALL_LOG = "android.permission.READ_CALL_LOG";
    public static final String PERMISSION_WRITE_CALL_LOG = "android.permission.WRITE_CALL_LOG";
    public static final String PERMISSION_ADD_VOICEMAIL = "com.android.voicemail.permission.ADD_VOICEMAIL";
    public static final String PERMISSION_USE_SIP = "android.permission.USE_SIP";
    public static final String PERMISSION_PROCESS_OUTGOING_CALLS = "android.permission.PROCESS_OUTGOING_CALLS";
    // Sensors
    public static final String PERMISSION_BODY_SENSORS = "android.permission.BODY_SENSORS";
    public static final String PERMISSION_USE_FINGERPRINT = "android.permission.USE_FINGERPRINT";
    // SMS
    public static final String PERMISSION_SEND_SMS = "android.permission.SEND_SMS";
    public static final String PERMISSION_RECEIVE_SMS = "android.permission.RECEIVE_SMS";
    public static final String PERMISSION_READ_SMS = "android.permission.READ_SMS";
    public static final String PERMISSION_RECEIVE_WAP_PUSH = "android.permission.RECEIVE_WAP_PUSH";
    public static final String PERMISSION_RECEIVE_MMS = "android.permission.RECEIVE_MMS";
    public static final String PERMISSION_READ_CELL_BROADCASTS = "android.permission.READ_CELL_BROADCASTS";
    // Storage
    public static final String PERMISSION_READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    public static final String PERMISSION_WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";

    @TargetApi(Build.VERSION_CODES.M)
    public static void requestPermission(Activity activity, @PermissionString String permission, int requestCode) {
        explainAndRequestPermission(activity, permission, requestCode, -1, -1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void explainAndRequestPermission(Activity activity,
                                                   @PermissionString String permission,
                                                   int requestCode,
                                                   @StringRes int titleResId,
                                                   @StringRes int messageResId) {
        if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted
            grantPermission(activity, permission, requestCode);
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
                && titleResId != -1
                && messageResId != -1) {
            // Permission is not granted, we should show an explanation and we have the resources to do so
            showPermissionAlertDialog(activity, permission, requestCode, titleResId, messageResId);
        } else {
            // Permission is not granted, need to ask for it
            askPermission(activity, permission, requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static void grantPermission(Activity activity,
                                        String permission,
                                        int requestCode) {
        activity.onRequestPermissionsResult(requestCode,
                new String[]{permission},
                new int[]{PackageManager.PERMISSION_GRANTED});
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static void showPermissionAlertDialog(final Activity activity,
                                                  @PermissionString final String permission,
                                                  final int requestCode,
                                                  @StringRes int titleResId,
                                                  @StringRes int messageResId) {
        new AlertDialog.Builder(activity)
                .setTitle(titleResId)
                .setMessage(messageResId)
                .setNegativeButton(R.string.camera_permission_explanation_back, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.camera_permission_explanation_ask, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        askPermission(activity, permission, requestCode);
                    }
                })
                .create()
                .show();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static void askPermission(Activity activity, @PermissionString String permission, int requestCode) {
        ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
    }

}
