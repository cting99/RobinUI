package com.cting.robin.ui.lib.unread;


import android.animation.Animator;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.ContentObserver;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cting.robin.ui.lib.unread.anim.IUnreadAnim;
import com.cting.robin.ui.lib.unread.anim.UnreadScaleAnim;
import com.cting.robin.ui.lib.unread.anim.UnreadShakeAnim;
import com.cting.robin.ui.lib.unread.anim.UnreadTransitAnim;
import com.cting.robin.ui.lib.unread.query.BaseUnreadQueryHandler;
import com.cting.robin.ui.lib.unread.query.BaseUnreadQueryHandler.IUnreadQuery;

public abstract class BaseUnreadView extends FrameLayout implements IUnreadQuery, IUnreadView {
    private static final boolean DEBUG = false;
    protected String TAG = "cting/unread/";
    public static final int ANIM_TYPE_SCALE = 1;
    public static final int ANIM_TYPE_SHAKE = 2;
    public static final int ANIM_TYPE_TRANSIT = 3;

    private static final String EXCEED_TEXT = "99+";
    private static final int MAX_UNREAD_COUNT = 99;

    protected ImageView mIconView;
    protected TextView mCountView;
    protected TextView mLabelView;

    protected int mIconRes = -1;
    protected int mTextColor = -1;
    protected int mUnreadTextBg = -1;
    protected int mTextSize = -1;
    protected int mLayoutId = R.layout.unread_tile;
    private String mLabel;
    private boolean mAlwaysShowZero = true;

    protected int mCount = 0;
    private boolean mIsRegistered = false;
    private Animator.AnimatorListener mListener = null;
    private IUnreadAnim mUnreadAnim = null;

    protected boolean mIsInitQuery = false;
    protected BaseUnreadQueryHandler mUnreadQueryHandler;
    protected ContentObserver mObserver = new ContentObserver(getHandler()) {
        @Override
        public void onChange(boolean selfChange) {
            Log.w(TAG, "onChange: ");
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            Log.w(TAG, "onChange: " + uri);
            mIsInitQuery = false;
            startQuery();
        }
    };

    private void startQuery() {
        if (mUnreadQueryHandler != null) {
            mUnreadQueryHandler.query();
        }
    }

    public BaseUnreadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TAG += getClass().getSimpleName();

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BaseUnreadView, 0, 0);
        mAlwaysShowZero = ta.getBoolean(R.styleable.BaseUnreadView_unread_alwaysShowZero, mAlwaysShowZero);
        mIconRes = ta.getResourceId(R.styleable.BaseUnreadView_unread_icon, -1);
        mTextColor = ta.getColor(R.styleable.BaseUnreadView_unread_count_textColor, -1);
        mUnreadTextBg = ta.getResourceId(R.styleable.BaseUnreadView_unread_count_textBackground, -1);
        mTextSize = ta.getDimensionPixelOffset(R.styleable.BaseUnreadView_unread_count_textSize, -1);
        mLayoutId = ta.getResourceId(R.styleable.BaseUnreadView_unread_layout, mLayoutId);
        mLabel = ta.getString(R.styleable.BaseUnreadView_unread_label);
        int animType = ta.getInt(R.styleable.BaseUnreadView_unread_anim_type, -1);
        ta.recycle();
        if (DEBUG) Log.i(TAG,
                "animType=" + animType +
                ",mIconRes=" + mIconRes +
                ",mTextColor=" + mTextColor +
                ",mTextSize=" + mTextSize +
                ",mLayoutId=" + mLayoutId
        );
        inflate(context, mLayoutId, this);
        switch (animType) {
            case ANIM_TYPE_SCALE:
                setUnreadAnim(new UnreadScaleAnim(this));
                break;
            case ANIM_TYPE_SHAKE:
                setUnreadAnim(new UnreadShakeAnim(this));
                break;
            case ANIM_TYPE_TRANSIT:
                setUnreadAnim(new UnreadTransitAnim(this));
                break;
            default:
                setUnreadAnim(null);
//                setUnreadAnim(new UnreadScaleAnim(this));
                break;
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mIconView = findViewById(R.id.unread_icon_img);
        mCountView = findViewById(R.id.unread_count_text);
        mLabelView = findViewById(R.id.unread_label_text);

        mCountView.getPaint().setAntiAlias(true);
        if (mIconRes > 0) {
            mIconView.setImageResource(mIconRes);
        }
        if (mTextColor > 0) {
            mCountView.setTextColor(mTextColor);
        }
        if (mTextSize > 0) {
            mCountView.setTextSize(mTextSize);
        }
        if (mUnreadTextBg > 0) {
            mCountView.setBackgroundResource(mUnreadTextBg);
        }

        if (mLabelView != null) {
            if (mLabel != null) {
                mLabelView.setVisibility(VISIBLE);
                mLabelView.setText(mLabel);
            } else {
                mLabelView.setVisibility(GONE);
            }
        }
        setCount(0);//clear
        mUnreadQueryHandler = getQueryHandler();
    }

    protected abstract BaseUnreadQueryHandler getQueryHandler();

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mIsRegistered = registerUri(getContext().getContentResolver());
        if (!mIsInitQuery) {
            mIsInitQuery = true;
        }
        startQuery();
    }

    @Override
    public void onVisibilityAggregated(boolean isVisible) {
        super.onVisibilityAggregated(isVisible);
        if (isVisible) {
            startQuery();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mIsRegistered) {
            unregisterUri(getContext().getContentResolver());
        }
    }

    protected abstract boolean registerUri(ContentResolver resolver);

    protected void unregisterUri(ContentResolver resolver) {
        resolver.unregisterContentObserver(mObserver);
    }

    @Override
    public void onResult(int count) {
        setCount(count, !mIsInitQuery);
    }

    @Override
    public View getContainer() {
        return this;
    }

    @Override
    public View getIconView() {
        return mIconView;
    }

    @Override
    public View getCountView() {
        return mCountView;
    }

    @Override
    public void updateCountViewVisibility() {
        mCountView.setVisibility(mCount > 0 || mAlwaysShowZero ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setCountBgColor(@ColorInt int color) {
        mCountView.setBackgroundColor(color);
    }

    @Override
    public void setCount(int count) {
        setCount(count, false);
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public void setAlwaysShowZero(boolean always) {
        mAlwaysShowZero = always;
    }

    @Override
    public boolean alwaysShowZero() {
        return mAlwaysShowZero;
    }

    @Override
    public void setIcon(@DrawableRes int iconResId) {
        mIconRes = iconResId;
        mIconView.setImageResource(mIconRes);
    }

    protected void setCount(int count, boolean animate) {
        if (mIsInitQuery) {
            animate = false;
            mIsInitQuery = false;
        }
        if (DEBUG)
            Log.i(TAG, "setCount:" + count + ",animate=" + animate + ",mUnreadAnim=" + mUnreadAnim);
        mCount = count;
        if (mCountView != null) {
            String finalText;
            if (mCount <= MAX_UNREAD_COUNT) {
                finalText = String.valueOf(mCount);
            } else {
                finalText = EXCEED_TEXT;
            }
            mCountView.setText(finalText);
        } else {
            Log.e(TAG, "mCountView null");
        }
        if (animate && mUnreadAnim != null) {
            mUnreadAnim.start();
        } else {
            updateCountViewVisibility();
        }
    }

    @Override
    public void setUnreadAnim(IUnreadAnim anim) {
        mUnreadAnim = anim;
    }

    @Override
    public Animator.AnimatorListener getAnimatorListener() {
        if (mListener == null) {
            mListener = new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    updateCountViewVisibility();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    onAnimationEnd(animation);
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            };
        }
        return mListener;
    }
}
