package com.cting.robin.ui.lib.unread.test;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.util.Log;

import com.cting.robin.ui.lib.unread.BaseUnreadView;
import com.cting.robin.ui.lib.unread.anim.UnreadShakeAnim;
import com.cting.robin.ui.lib.unread.query.BaseUnreadQueryHandler;


public class TestUnreadView extends BaseUnreadView {
    public static final String UNREAD_ACTION_READ = "cting.com.robin.ui.lib.unread.ACTION_READ";
    public static final String UNREAD_ACTION_UNREAD = "cting.com.robin.ui.lib.unread.ACTION_UNREAD";
    private BroadcastReceiver mReceiver;

    public TestUnreadView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected BaseUnreadQueryHandler getQueryHandler() {
        return null;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        mIsInitQuery = false;
    }

    private void read() {
        if (mCount > 0) {
            setCount(--mCount,true);
        }
    }

    private void newReceive() {
        setCount(++mCount,true);
    }

    @Override
    protected boolean registerUri(ContentResolver resolver) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UNREAD_ACTION_READ);
        intentFilter.addAction(UNREAD_ACTION_UNREAD);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
//                Log.i(TAG, "onReceive: " + action);
                if (UNREAD_ACTION_READ.equals(action)) {
                    read();
                } else if(UNREAD_ACTION_UNREAD.equals(action)){
                    newReceive();
                }
            }
        };
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver, intentFilter);
        return true;
    }

    @Override
    protected void unregisterUri(ContentResolver resolver) {
        super.unregisterUri(resolver);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mReceiver);
    }

    public static final void sendRead(Context context) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(UNREAD_ACTION_READ));
    }

    public static final void sendNewReceive(Context context) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(UNREAD_ACTION_UNREAD));
    }
}
