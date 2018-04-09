package com.cting.robin.ui.lib.unread;

import android.content.ContentResolver;
import android.content.Context;
import android.util.AttributeSet;

import com.cting.robin.ui.lib.unread.query.UnreadMessageQueryHandler;
import com.cting.robin.ui.lib.unread.query.BaseUnreadQueryHandler;
import com.cting.robin.ui.lib.unread.utils.UnreadHelper;

// need request runtime & Manifest permission: android.permission.READ_SMS
public class UnreadMsgView extends BaseUnreadView {

    public UnreadMsgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (mIconRes == -1) {
            mIconRes = R.drawable.unread_ic_mms;
        }
    }

    @Override
    protected BaseUnreadQueryHandler getQueryHandler() {
        return new UnreadMessageQueryHandler(getContext().getContentResolver(),this);
    }

    @Override
    protected boolean registerUri(final ContentResolver resolver) {
        resolver.registerContentObserver(UnreadHelper.OBSERVER_UNREAD_SMS, true, mObserver);
        resolver.registerContentObserver(UnreadHelper.OBSERVER_UNREAD_MMS, true, mObserver);
        return true;
    }

}
