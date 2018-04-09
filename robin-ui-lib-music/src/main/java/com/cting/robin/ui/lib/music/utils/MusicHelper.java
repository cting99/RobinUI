package com.cting.robin.ui.lib.music.utils;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

public class MusicHelper {

    public static final String TAG = "cting/music/helper";

    public static void clickMediaButton(int keyCode, Context context) {
        Log.i(TAG, "click button " + getKeyName(keyCode));
        reportMediaKey(context, KeyEvent.ACTION_DOWN, keyCode);
        reportMediaKey(context, KeyEvent.ACTION_UP, keyCode);
    }

    public static void reportMediaKey(Context context, int action, int keyCode) {
        KeyEvent keyEvent = new KeyEvent(action, keyCode);
        Intent intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        intent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
        Log.d(TAG, "reportMediaKey " + keyEvent.toString());
        context.sendBroadcast(intent);
    }


    public static final String getKeyName(int keyCode) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                return "KEYCODE_MEDIA_PLAY_PAUSE";
            case KeyEvent.KEYCODE_MEDIA_STOP:
                return "KEYCODE_MEDIA_STOP";
            case KeyEvent.KEYCODE_MEDIA_NEXT:
                return "KEYCODE_MEDIA_NEXT";
            case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                return "KEYCODE_MEDIA_PREVIOUS";
            case KeyEvent.KEYCODE_MEDIA_REWIND:
                return "KEYCODE_MEDIA_REWIND";
            case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                return "KEYCODE_MEDIA_FAST_FORWARD";
            default:
                return String.valueOf(keyCode);

        }
    }
}
