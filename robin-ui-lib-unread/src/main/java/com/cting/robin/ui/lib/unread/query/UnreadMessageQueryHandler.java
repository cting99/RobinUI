package com.cting.robin.ui.lib.unread.query;

import android.content.ContentResolver;
import android.database.Cursor;
import android.util.Log;

import com.cting.robin.ui.lib.unread.utils.UnreadHelper;

// need request runtime & Manifest permission: android.permission.READ_SMS
public class UnreadMessageQueryHandler extends BaseUnreadQueryHandler {
    private static final int TOKEN_UNREAD_MMS_COUNT = 1;
    private static final int TOKEN_UNREAD_SMS_COUNT = 2;
    int mmsCount = -1;
    int smsCount = -1;

    public UnreadMessageQueryHandler(ContentResolver cr, IUnreadQuery unreadQuery) {
        super(cr, unreadQuery);
    }

    @Override
    public void query() {
        startQuery(TOKEN_UNREAD_MMS_COUNT,
                null,
                UnreadHelper.URI_UNREAD_MMS,
                UnreadHelper.PROJECTION_UNREAD_MMS,
                UnreadHelper.SELECTION_UNREAD_MMS,
                null,
                null);
        startQuery(TOKEN_UNREAD_SMS_COUNT,
                null,
                UnreadHelper.URI_UNREAD_SMS,
                UnreadHelper.PROJECTION_UNREAD_SMS,
                UnreadHelper.SELECTION_UNREAD_SMS,
                null,
                null);
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        if (TOKEN_UNREAD_MMS_COUNT == token) {
            if (cursor != null && cursor.getCount() == 1 && cursor.moveToNext()) {
                mmsCount = cursor.getInt(0);
                cursor.close();
                Log.i("cting/unread/mms", "onQueryComplete: " + mmsCount);
            }
        } else if (TOKEN_UNREAD_SMS_COUNT == token) {
            if (cursor != null && cursor.getCount() == 1 && cursor.moveToNext()) {
                smsCount = cursor.getInt(0);
                cursor.close();
                Log.i("cting/unread/sms", "onQueryComplete: " + smsCount);
            }
        }
        if (mmsCount != -1 && smsCount != -1) {
            unreadQuery.onResult(mmsCount + smsCount);
        }
    }
}
