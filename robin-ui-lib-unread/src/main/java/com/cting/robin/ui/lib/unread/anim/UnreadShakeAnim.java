package com.cting.robin.ui.lib.unread.anim;


import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.cting.robin.ui.lib.unread.IUnreadView;

public class UnreadShakeAnim extends BaseUnreadAnim {
    public UnreadShakeAnim(IUnreadView view) {
        super(view);
    }

    @Override
    public View getTarget() {
        return mView.getIconView();
    }

    @Override
    public void start() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(getTarget(), "rotation", 0, -20, 20, 0, -20, 20, 0)
                .setDuration(300);
        animator.setInterpolator(new LinearInterpolator());
        animator.addListener(mView.getAnimatorListener());
        animator.start();
    }
}
