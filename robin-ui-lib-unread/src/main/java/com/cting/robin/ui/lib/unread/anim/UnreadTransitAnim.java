package com.cting.robin.ui.lib.unread.anim;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;

import com.cting.robin.ui.lib.unread.IUnreadView;


public class UnreadTransitAnim extends BaseUnreadAnim {
    public UnreadTransitAnim(IUnreadView view) {
        super(view);
    }


    @Override
    public View getTarget() {
        return mView.getIconView();
    }

    @Override
    public void start() {
        float delta = 5/*getTarget().getWidth() / 2*/;
        ObjectAnimator animator = ObjectAnimator.ofFloat(getTarget(), "translationX", 0, -delta, delta, 0)
                .setDuration(300);
//        animator.setRepeatCount(2);
        animator.setInterpolator(new BounceInterpolator());
        animator.addListener(mView.getAnimatorListener());
        animator.start();
    }
}
