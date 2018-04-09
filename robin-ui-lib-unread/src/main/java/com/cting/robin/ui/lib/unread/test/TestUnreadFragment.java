package com.cting.robin.ui.lib.unread.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cting.robin.ui.lib.unread.R;


public class TestUnreadFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "cting/test/unread";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_unread_fragment, container, false);
        Button readBtn = view.findViewById(R.id.unread_test_read_btn);
        Button receiveBtn = view.findViewById(R.id.unread_test_receive_btn);
        readBtn.setOnClickListener(this);
        receiveBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.unread_test_read_btn) {
            TestUnreadView.sendRead(getContext());
        } else if (i == R.id.unread_test_receive_btn) {
            TestUnreadView.sendNewReceive(getContext());
        }
    }
}
