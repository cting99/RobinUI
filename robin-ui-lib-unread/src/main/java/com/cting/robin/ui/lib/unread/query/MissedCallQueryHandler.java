package com.cting.robin.ui.lib.unread.query;

import android.content.ContentResolver;
import android.database.Cursor;
import android.util.Log;

import com.cting.robin.ui.lib.unread.utils.UnreadHelper;

// need request runtime & Manifest permission: Manifest.permission.READ_CALL_LOG
public class MissedCallQueryHandler extends BaseUnreadQueryHandler {
    public static final int TOKEN_MISSED_CALL_COUNT = 0;

    public static final String TAG = "cting/unread/call";

    public MissedCallQueryHandler(ContentResolver cr, IUnreadQuery unreadQuery) {
        super(cr, unreadQuery);
    }

    @Override
    public void query() {
        startQuery(TOKEN_MISSED_CALL_COUNT,
                null,
                UnreadHelper.URI_MISSED_CALL,
                UnreadHelper.PROJECTION_MISSED_CALL,
                UnreadHelper.SELECTION_MISSED_CALL,
                null,
                null);
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        if (token == TOKEN_MISSED_CALL_COUNT) {
            if (cursor != null) {
                int count = cursor.getCount();
                Log.i(TAG, "onQueryComplete: " + count);
                unreadQuery.onResult(count);
                cursor.close();
            } else {
                Log.w(TAG, "onQueryComplete: cursor null");
            }
        }
    }
}
