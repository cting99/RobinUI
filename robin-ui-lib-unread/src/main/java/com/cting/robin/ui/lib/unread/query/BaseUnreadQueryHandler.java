package com.cting.robin.ui.lib.unread.query;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;

public abstract class BaseUnreadQueryHandler extends AsyncQueryHandler {
    protected IUnreadQuery unreadQuery;

    public BaseUnreadQueryHandler(ContentResolver cr, IUnreadQuery unreadQuery) {
        super(cr);
        this.unreadQuery = unreadQuery;
    }

    public abstract void query();

    public interface IUnreadQuery {
        void onResult(int count);
    }
}
