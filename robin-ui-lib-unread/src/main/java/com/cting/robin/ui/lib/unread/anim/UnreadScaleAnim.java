package com.cting.robin.ui.lib.unread.anim;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;

import com.cting.robin.ui.lib.unread.IUnreadView;


public class UnreadScaleAnim extends BaseUnreadAnim {

    public UnreadScaleAnim(IUnreadView view) {
        super(view);
    }

    @Override
    protected void appear() {
        animAppearDisappear(0f, 1f);
    }

    @Override
    protected void attention() {
        animAppearDisappear(1f, 0f, 1f);
    }

    @Override
    protected void disappear() {
        animAppearDisappear(1f, 0f);
    }

    private void animAppearDisappear(float... values) {
        PropertyValuesHolder xScaleHolder = PropertyValuesHolder.ofFloat("scaleX", values);
        PropertyValuesHolder yScaleHolder = PropertyValuesHolder.ofFloat("scaleY", values);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(getTarget(), xScaleHolder, yScaleHolder).setDuration(300);
//        animator.setInterpolator(new AccelerateInterpolator());
        animator.addListener(mView.getAnimatorListener());//must needed
        animator.start();
    }
}
