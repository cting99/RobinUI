package com.cting.support.dialpad.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class DialpadPresenter implements DialpadContract.IPresenter {
    public static final String TAG = "cting/dialpad/presenter";
    DialpadContract.IUI mView;

    public DialpadPresenter(DialpadContract.IUI view) {
        this.mView = view;
    }

    @Override
    public void start() {

    }

    @Override
    public void dialNumber(Context context, String number) {
        if (TextUtils.isEmpty(number)) {
            Toast.makeText(context, "no number!", Toast.LENGTH_SHORT).show();
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "dialNumber: need CALL_PHONE permission");
            Toast.makeText(context, "need CALL_PHONE permission", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri uri = Uri.parse("tel:" + number);
        intent.setData(uri);
        context.startActivity(intent);
    }

    @Override
    public void sendMessage(Context context, String number) {
        if (TextUtils.isEmpty(number)) {
            Toast.makeText(context, "no number!", Toast.LENGTH_SHORT).show();
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "dialNumber: need SEND_SMS permission");
            Toast.makeText(context, "need SEND_SMS permission", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        Uri uri = Uri.parse("smsto:" + number);
        intent.setData(uri);
        context.startActivity(intent);
    }
}
