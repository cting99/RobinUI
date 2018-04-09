package com.cting.support.dialpad.fragments;


import android.content.Context;

import com.cting.support.dialpad.base.IBasePresenter;
import com.cting.support.dialpad.base.IBaseUI;

public class DialpadContract {

    public interface IPresenter extends IBasePresenter {
        void dialNumber(Context context, String number);

        void sendMessage(Context context, String number);
    }

    public interface IUI extends IBaseUI {

        void showDialpad(boolean show);

        void deleteInputText();

    }
}
