package com.mickaelg.permissionstest.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;

import com.mickaelg.permissionstest.R;
import com.mickaelg.permissionstest.utils.PermissionsUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mickaelg on 14/10/2015.
 */
public class MainActivity extends Activity implements View.OnClickListener {

    // region Properties

    @SuppressWarnings("unused")
    private static final String TAG = MainActivity.class.getSimpleName();
    // Request code to use when asking for a camera permission
    private static final int REQUEST_CODE_CAMERA = 0;
    // Code used to take a camera pick
    private static final int CAMERA_PIC_REQUEST = 1;

    @Bind(R.id.main_btn_ask_permission)
    Button mBtnAskPermission;

    // endregion


    // region Lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this, this);

        mBtnAskPermission.setOnClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_CAMERA:
                boolean cameraPermissionGranted = grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (cameraPermissionGranted) {
                    launchCamera();
                } else {
                    showPermissionDenied();
                }
                break;
        }
    }
    // endregion


    // region View.OnClickListener implementation

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_btn_ask_permission:
                handleCameraPermission();
                break;
        }
    }

    // endregion


    // region Permissions methods

    /**
     * Handle the camera permission, if it is granted, if we should show an explanation or if it is denied.
     */
    private void handleCameraPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // Permission is granted at install time
            launchCamera();
        } else {
            // We need to check the permission
            PermissionsUtils.explainAndRequestPermission(this, PermissionsUtils.PERMISSION_CAMERA, REQUEST_CODE_CAMERA,
                    R.string.camera_permission_explanation_title, R.string.camera_permission_explanation);
        }
    }

    /**
     * Launch the camera to take pictures.
     */
    private void launchCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
    }

    /**
     * Show an error message explaining that the permission is denied.
     */
    private void showPermissionDenied() {
        Snackbar.make(getWindow().getDecorView(), R.string.error_camera_permission_denied, Snackbar.LENGTH_SHORT)
                .show();
    }

    // endregion

}
