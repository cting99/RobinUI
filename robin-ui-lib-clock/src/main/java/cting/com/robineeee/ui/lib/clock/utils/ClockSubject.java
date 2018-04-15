package cting.com.robineeee.ui.lib.clock.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;

import java.util.Calendar;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ClockSubject {

    public static final String TAG = "cting/clock/subject";
    public static final boolean DEBUG = false;

    private static class Holder {
        private static final ClockSubject INSTANCE = new ClockSubject();
    }

    public static ClockSubject getInstance() {
        return Holder.INSTANCE;
    }

    private ClockSubject() {
    }

    private final Set<Listener> mMinuteListeners = Collections.newSetFromMap(
            new ConcurrentHashMap<Listener, Boolean>(3));
    private ContentObserver mObserver;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(DEBUG)   Log.i(TAG, "onReceive: " + action + ",notify---");
            notifyListeners();
        }
    };

    private void register(Context context) {
        Context applicationContext = context.getApplicationContext();
        registerObserver(applicationContext);
        registerReceiver(applicationContext);
    }

    private void registerObserver(Context applicationContext) {
        mObserver = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                if (DEBUG) Log.i(TAG, "observer: onChange,notify---");
                notifyListeners();
            }
        };
        applicationContext.getContentResolver().registerContentObserver(
                Settings.System.getUriFor(Settings.System.TIME_12_24),
                false,
                mObserver);
    }

    private void registerReceiver(Context applicationContext) {
        Log.i(TAG, "{ register: observer and receiver");
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        applicationContext.registerReceiver(mReceiver, filter);
    }

    private void unregister(Context context) {
        Log.i(TAG, "} unregister: observer and receiver");
        Context applicationContext = context.getApplicationContext();
        unregisterObserver(applicationContext);
        unregisterReceiver(applicationContext);
    }

    private void unregisterObserver(Context applicationContext) {
        applicationContext.getContentResolver().unregisterContentObserver(mObserver);
    }

    private void unregisterReceiver(Context applicationContext) {
        applicationContext.unregisterReceiver(mReceiver);
    }

    public void addListener(Context context, Listener listener) {
        if (listener != null && !mMinuteListeners.contains(listener)) {
            listener.onTimeChanged(updateTime());
            if (mMinuteListeners.size() == 0) {
                Log.i(TAG, "addListener: first one,register here");
                register(context);
            }
            mMinuteListeners.add(listener);
        }
    }

    public void removeListener(Context context, Listener listener) {
        if (listener != null && mMinuteListeners.contains(listener)) {
            mMinuteListeners.remove(listener);
            if (mMinuteListeners.size() == 0) {
                Log.i(TAG, "listener empty,unregister");
                unregister(context);
            } else {
                Log.i(TAG, "remove listener:" + listener);
            }
        }
    }

    private void notifyListeners() {
        for (Listener listener : mMinuteListeners) {
            if (DEBUG) Log.i(TAG, "notifyListeners: " + listener);
            listener.onTimeChanged(updateTime());
        }
    }

    public static Calendar updateTime() {
        return Calendar.getInstance();
    }

    public static final boolean is24HourFormat(Context context) {
        return DateFormat.is24HourFormat(context);
    }

    public interface Listener {
        void onTimeChanged(Calendar time);
    }

}
