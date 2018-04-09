package cting.com.robin.ui.lib.clock;


import android.content.Context;
import android.content.res.Resources;

public interface IClockView {

    int getCenterX();

    int getCenterY();

    int getDiameter();

    Context getContext();

    Resources getResources();
}
