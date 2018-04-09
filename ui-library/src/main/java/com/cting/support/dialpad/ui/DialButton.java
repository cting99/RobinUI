package com.cting.support.dialpad.ui;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cting.support.R;

public class DialButton extends LinearLayout {
    private TextView nameText;
    private ImageView iconImg;

    public DialButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.dial_button_layout, this);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        nameText = findViewById(R.id.dial_text);
        iconImg = findViewById(R.id.dial_icon);
    }

    public void update(DialButtonEntry entry) {
        nameText.setText(entry.name);
        iconImg.setImageResource(entry.iconResId);
        setBackgroundResource(entry.bgResId);
    }


    public class DialButtonEntry{

        @NonNull private String name;
        @DrawableRes private int iconResId;
        @DrawableRes private int bgResId;
    }


}
