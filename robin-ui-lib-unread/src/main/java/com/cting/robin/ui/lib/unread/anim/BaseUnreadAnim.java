package com.cting.robin.ui.lib.unread.anim;

import android.util.Log;
import android.view.View;

import com.cting.robin.ui.lib.unread.IUnreadView;


public abstract class BaseUnreadAnim implements IUnreadAnim {
    protected String TAG = "cting/unread/anim/";
    protected IUnreadView mView;

    public BaseUnreadAnim(IUnreadView view) {
        TAG += getClass().getSimpleName();
        this.mView = view;
    }

    public View getTarget() {
        return mView.getCountView();
    }

    @Override
    public void start() {
        boolean viewAlreadyShow = isVisible(getTarget());
        boolean hasUnread = mView.getCount() > 0;
        boolean viewAlwaysShow = mView.alwaysShowZero();
//        Log.i(TAG, "start: viewAlreadyShow:" + viewAlreadyShow + ",viewAlwaysShow=" + viewAlwaysShow + ",hasUnread=" + hasUnread);
        if (hasUnread) {
            if (viewAlreadyShow) {
                attention();
            } else {
                mView.updateCountViewVisibility();
                appear();
            }
        } else {
            if (viewAlwaysShow) {
                mView.updateCountViewVisibility();
            } else {
                disappear();
            }
        }
    }

    protected void appear() {
        Log.i(TAG, "appear: ");
        setShow(getTarget(), true);
    }

    protected void disappear() {
        Log.i(TAG, "disappear: ");
        setShow(getTarget(), false);
    }

    protected void attention() {
        Log.i(TAG, "attention: ");
        appear();
    }

    protected boolean isVisible(View view) {
        return view != null && view.getVisibility() == View.VISIBLE;
    }

    protected void setShow(View view, boolean show) {
        if (view != null) {
            boolean isVisible = isVisible(view);
            if (show) {
                if (!isVisible) {
                    view.setVisibility(View.VISIBLE);
                }
            } else {
                if (isVisible) {
                    view.setVisibility(View.GONE);
                }
            }
        }
    }
}
