package com.cting.robin.ui.lib.unread;
import android.content.ContentResolver;
import android.content.Context;
import android.util.AttributeSet;

import com.cting.robin.ui.lib.unread.query.MissedCallQueryHandler;
import com.cting.robin.ui.lib.unread.query.BaseUnreadQueryHandler;
import com.cting.robin.ui.lib.unread.utils.UnreadHelper;

// need request runtime & Manifest permission: Manifest.permission.READ_CALL_LOG
public class MissedCallView extends BaseUnreadView {

    public MissedCallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (mIconRes == -1) {
            mIconRes = R.drawable.unread_ic_call;
        }
    }

    @Override
    protected BaseUnreadQueryHandler getQueryHandler() {
        return new MissedCallQueryHandler(getContext().getContentResolver(),this);
    }

    @Override
    protected boolean registerUri(final ContentResolver resolver) {
        resolver.registerContentObserver(UnreadHelper.OBSERVER_MISSED_CALL, false, mObserver);
        return true;
    }

}
