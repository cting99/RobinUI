package com.cting.support.dialpad.utils;

import android.view.KeyEvent;
import android.widget.TextView;

public class DialUtils {

    public static void pressKey(TextView textView, int keyCode) {
        KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
        textView.onKeyDown(keyCode, event);
    }
}
