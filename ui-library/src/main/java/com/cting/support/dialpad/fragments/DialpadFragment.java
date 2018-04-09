package com.cting.support.dialpad.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.cting.support.R;
import com.cting.support.dialpad.ui.DialButton;
import com.cting.support.dialpad.ui.DialKeyPad;
import com.cting.support.dialpad.utils.DialUtils;


public class DialpadFragment extends Fragment
        implements DialpadContract.IUI,
        TextWatcher,
        View.OnClickListener,
        View.OnLongClickListener {

    private EditText mInputEdit;
    private ImageButton mDeleteInputBtn;
    private DialKeyPad mKeyPad;
    private ImageButton mShowDialpadBtn;
    private ImageButton mSendMsgBtn;
    private DialButton mDialBtn1, mDialBtn2;

    private DialpadPresenter mPresenter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new DialpadPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialpad_layout, container, false);

        mInputEdit = view.findViewById(R.id.input_edit);
        mDeleteInputBtn = view.findViewById(R.id.delete_btn);
        mKeyPad = view.findViewById(R.id.dialpad);
        mShowDialpadBtn = view.findViewById(R.id.show_dialpad_btn);
        mSendMsgBtn = view.findViewById(R.id.send_msg_btn);
        mDialBtn1 = view.findViewById(R.id.dial_btn);
        mDialBtn2 = view.findViewById(R.id.dial_btn_dual);

        mInputEdit.addTextChangedListener(this);
        mDeleteInputBtn.setOnClickListener(this);
        mDeleteInputBtn.setOnLongClickListener(this);
        mKeyPad.setEditable(mInputEdit);
        mShowDialpadBtn.setOnClickListener(this);
        mSendMsgBtn.setOnClickListener(this);

        showInputRow(isInputEmpty());
        return view;
    }


    @Override
    public void showDialpad(boolean show) {
        mKeyPad.setVisibility(show ? View.VISIBLE : View.GONE);
        showInputRow(show);
    }

    public void setInputText(String number) {
        mInputEdit.setText(number);
    }

    @Override
    public void deleteInputText() {
        mInputEdit.getText().clear();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        showInputRow(isInputEmpty());
    }

    private void showInputRow(boolean show) {
        int visibility = show ? View.GONE : View.VISIBLE;
        mInputEdit.setVisibility(visibility);
        mDeleteInputBtn.setVisibility(visibility);
    }

    private String getInputNumber() {
        return mInputEdit.getText().toString();
    }

    private boolean isInputEmpty() {
        return mInputEdit.length() == 0;
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (isInputEmpty()) {
            mInputEdit.setCursorVisible(false);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.delete_btn) {
            pressKey(KeyEvent.KEYCODE_DEL);
        } else if (id == R.id.show_dialpad_btn) {
            toggleDialpad();
        } else if (id == R.id.send_msg_btn) {
            mPresenter.sendMessage(getContext(), getInputNumber());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        int id = v.getId();
        if (id == R.id.delete_btn) {
            deleteInputText();
            return true;
        }
        return false;
    }

    private void pressKey(int keyCode) {
        DialUtils.pressKey(mInputEdit, keyCode);
    }

    private void toggleDialpad() {
        showDialpad(!isDialpadOpen());
    }

    private boolean isDialpadOpen() {
        return mKeyPad.getVisibility() == View.VISIBLE;
    }
}
