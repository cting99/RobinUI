package com.cting.robin.ui.lib.unread;


import android.animation.Animator;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.view.View;

import com.cting.robin.ui.lib.unread.anim.IUnreadAnim;

public interface IUnreadView {
    View getContainer();

    View getIconView();

    View getCountView();

    void updateCountViewVisibility();

    void setCount(int count);

    void setCountBgColor(@ColorInt int color);

    void setUnreadAnim(IUnreadAnim anim);

    int getCount();

    void setAlwaysShowZero(boolean always);

    boolean alwaysShowZero();

    void setIcon(@DrawableRes int iconResId);

    Animator.AnimatorListener getAnimatorListener();
}
