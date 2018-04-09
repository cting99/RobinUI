package cting.com.robin.ui.lib.clock.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.ArrayList;

import cting.com.robin.robin.ui.lib.clock.R;
import cting.com.robin.ui.lib.clock.BaseClockView;


public class TestClockFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    public static final String TAG = "cting/test/clock";
    public static final int MENU_SHOW_SECOND = 1;
    ArrayList<BaseClockView> clockViews = new ArrayList<>();
    private Switch mHasSecondSwitch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_clock_fragment, container, false);

        mHasSecondSwitch = view.findViewById(R.id.test_clock_has_second_switch);
        mHasSecondSwitch.setOnCheckedChangeListener(this);
        getAllClockViews(view.findViewById(R.id.container));

        return view;
    }

    private void getAllClockViews(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) view;
            int childCount = vg.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View v = vg.getChildAt(i);
                if (v instanceof BaseClockView) {
                    clockViews.add((BaseClockView) v);
                } else if (v instanceof ViewGroup) {
                    getAllClockViews(v);
                }
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        for (BaseClockView clockView : clockViews) {
            clockView.setHasSecond(isChecked);
        }
    }
/*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.add(0, MENU_SHOW_SECOND, 0, "show second");
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//        item.setOn
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_SHOW_SECOND:
                showSecond = !showSecond;
                for (BaseClockView clockView : clockViews) {
                    clockView.setHasSecond(showSecond);
                }
                return true;
            default:
                return false;
        }
    }*/
}
