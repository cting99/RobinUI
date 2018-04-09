package com.cting.robin.ui.miniwindow.clock;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cting.robin.ui.miniwindow.R;
import com.cting.robin.ui.miniwindow.ui.ClockSwitcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class MiniClockFragment extends Fragment {

    public static final String TAG = "cting/mini/clock";
    Unbinder unbinder;
    @BindView(R.id.mini_window_multi_view_switcher)
    View mViewSwitcher;
    @BindView(R.id.mini_window_clock_switch_btn)
    Button mSwitchBtn;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mini_window_fragment_clock, container, false);
        unbinder = ButterKnife.bind(this, view);
        mSwitchBtn.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.mini_window_clock_switch_btn)
    public void onViewClicked() {
//        mViewSwitcher.showNext();
    }
}
