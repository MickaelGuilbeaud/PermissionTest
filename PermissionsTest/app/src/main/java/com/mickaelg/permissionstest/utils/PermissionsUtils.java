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
import java.util.ArrayList;
import java.util.List;

/**
 * Class gathering permissions related methods.
 * It helps simplify the permission request for Android M and later versions by sending the result to the
 * onRequestPermissionsResult method for both granted and denied. It is possible to show an AlertDialog explaining the
 * user why this method is needed, and always send the result to the onRequestPermissionResult method.
 * This class doesn't handle pre Android M versions, and multiple permissions.
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

    /**
     * Request the permission given in parameter. Granted or denied, the result will be send to the
     * Activity.onRequestPermissionsResult method.
     *
     * @param activity    Activity to send the result callback
     * @param permission  Permission to ask
     * @param requestCode Code of the request
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static void requestPermission(Activity activity, @PermissionString String permission, int requestCode) {
        requestPermissions(activity, new String[]{permission}, requestCode);
    }

    /**
     * Request the permission given in parameter. Show an AlertDialog explaining the permission if needed, and the
     * result will be send to the onRequestPermissionsResult method.
     *
     * @param activity     Activity to send the result callback
     * @param permission   Permission to ask
     * @param requestCode  Code of the request
     * @param titleResId   resId of the AlertDialog title
     * @param messageResId resId of the AlertDialog content
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static void explainAndRequestPermission(Activity activity,
                                                   @PermissionString String permission,
                                                   int requestCode,
                                                   @StringRes int titleResId,
                                                   @StringRes int messageResId) {
        explainAndRequestPermissions(activity, new String[]{permission}, requestCode, titleResId, messageResId);
    }

    /**
     * Request the permissions given in parameter. Denied permissions will be requested and the result will be send to
     * the Activity.onRequestPermissionsResult method. If all permissions are granted, send the result to the same
     * method.
     * Warning: it means that onRequestPermissionsResult will receive a callback with all permissions that was not
     * granted, OR all permissions if they are all already granted.
     *
     * @param activity    Activity to send the result callback
     * @param permissions Permissions to ask
     * @param requestCode Code of the request
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static void requestPermissions(Activity activity, @PermissionString String[] permissions, int requestCode) {
        explainAndRequestPermissions(activity, permissions, requestCode, -1, -1);
    }

    /**
     * Request the permissions given in parameter. Show an AlertDialog explaining the permissions if needed, and the
     * result will be send to the onRequestPermissionsResult method.
     * Warning: onRequestPermissionsResult will receive a callback with all permissions that was not granted, OR all
     * permissions if they are all already granted.
     *
     * @param activity     Activity to send the result callback
     * @param permissions  Permissions to ask
     * @param requestCode  Code of the request
     * @param titleResId   resId of the AlertDialog title
     * @param messageResId resId of the AlertDialog content
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static void explainAndRequestPermissions(Activity activity,
                                                    @PermissionString String[] permissions,
                                                    int requestCode,
                                                    @StringRes int titleResId,
                                                    @StringRes int messageResId) {
        List<String> deniedPermissions = new ArrayList<>();
        boolean shouldShowRequestPermissionRationale = false;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted, nothing to do with it
                continue;
            }

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
                    && titleResId != -1
                    && messageResId != -1) {
                shouldShowRequestPermissionRationale = true;
            }

            // Permission is not granted
            deniedPermissions.add(permission);
        }

        if (deniedPermissions.isEmpty()) {
            // No denied permissions, send all granted permissions
            grantPermissions(activity, permissions, requestCode);
        } else if (shouldShowRequestPermissionRationale) {
            // We need to show a permission explanation
            showPermissionsAlertDialog(activity,
                    deniedPermissions.toArray(new String[deniedPermissions.size()]),
                    requestCode,
                    titleResId,
                    messageResId);
        } else {
            // Some permissions are denied, we need to request them
            askPermissions(activity, permissions, requestCode);
        }
    }

    /**
     * Call the onRequestPermissionsResult method with a Granted result.
     *
     * @param activity    Activity to send the result callback
     * @param permissions Permissions to ask
     * @param requestCode Code of the request
     */
    @TargetApi(Build.VERSION_CODES.M)
    private static void grantPermissions(Activity activity, String[] permissions, int requestCode) {
        int[] results = new int[permissions.length];
        for (int i = 0; i < results.length; i++) {
            results[i] = PackageManager.PERMISSION_GRANTED;
        }
        activity.onRequestPermissionsResult(requestCode, permissions, results);
    }

    /**
     * Call the onRequestPermissionsResult method with a Denied result.
     *
     * @param activity    Activity to send the result callback
     * @param permissions Permissions to ask
     * @param requestCode Code of the request
     */
    @TargetApi(Build.VERSION_CODES.M)
    private static void denyPermissions(Activity activity, String[] permissions, int requestCode) {
        int[] results = new int[permissions.length];
        for (int i = 0; i < results.length; i++) {
            results[i] = PackageManager.PERMISSION_DENIED;
        }
        activity.onRequestPermissionsResult(requestCode, permissions, results);
    }

    /**
     * Show an AlertDialog explaining to the user why these permissions are needed. Then he can agree to be asked the
     * permissions, or refuse. In both case the result is send through the onRequestPermissionResult.
     *
     * @param activity     Activity to send the result callback
     * @param permissions  Permissions to ask
     * @param requestCode  Code of the request
     * @param titleResId   resId of the AlertDialog title
     * @param messageResId resId of the AlertDialog content
     */
    @TargetApi(Build.VERSION_CODES.M)
    private static void showPermissionsAlertDialog(final Activity activity,
                                                   @PermissionString final String[] permissions,
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
                        denyPermissions(activity, permissions, requestCode);
                    }
                })
                .setPositiveButton(R.string.camera_permission_explanation_ask, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        askPermissions(activity, permissions, requestCode);
                    }
                })
                .create()
                .show();
    }

    /**
     * Actually ask the requestPermission to the system.
     *
     * @param activity    Activity to send the result callback
     * @param permissions Permissions to ask
     * @param requestCode Code of the request
     */
    @TargetApi(Build.VERSION_CODES.M)
    private static void askPermissions(Activity activity, @PermissionString String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

}
