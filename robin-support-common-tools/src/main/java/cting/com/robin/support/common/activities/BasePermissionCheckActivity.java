package cting.com.robin.support.common.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import cting.com.robin.support.common.utils.PermissionHelper;

public class BasePermissionCheckActivity extends AppCompatActivity {

    public String TAG = "cting/act/";
    protected boolean mPermissionReady = false;

    public BasePermissionCheckActivity() {
        TAG += getClass().getSimpleName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPermissionReady = PermissionHelper.checkPermission(this, getRequestPermission());
        Log.i(TAG, "onCreate: mPermissionReady=" + mPermissionReady);
        if (mPermissionReady) {
            onPermissionReady();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean permissionAllGranted = PermissionHelper.onPermissionResult(this, requestCode, permissions, grantResults);
        if (permissionAllGranted) {
            mPermissionReady = true;
            onPermissionReady();
        }
    }

    protected String[] getRequestPermission() {
        return PermissionHelper.REQUEST_PERMISSIONS;
    }

    protected void onPermissionReady() {
    }
}
