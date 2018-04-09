package com.cting.robin.ui.lib.unread.utils;

import android.net.Uri;
import android.provider.CallLog;
import android.provider.Telephony;

public class UnreadHelper {

    // missed call
    public static final Uri OBSERVER_MISSED_CALL = Uri.parse("content://call_log/calls");//CallLog.Calls.CONTENT_URI;
    public static final Uri URI_MISSED_CALL = CallLog.Calls.CONTENT_URI;
    public static final String SELECTION_MISSED_CALL = CallLog.Calls.TYPE + " = " + CallLog.Calls.MISSED_TYPE + " AND " + CallLog.Calls.NEW + " = 1";
    public static final String[] PROJECTION_MISSED_CALL = {CallLog.Calls._ID};

    // mms
    public static final Uri OBSERVER_UNREAD_MMS = Telephony.Mms.CONTENT_URI;
    public static final Uri URI_UNREAD_MMS = Telephony.Mms.CONTENT_URI;
    public static final String SELECTION_UNREAD_MMS = "read = 0";//"msg_box = 1 AND read = 0 AND ( m_type =130 OR m_type = 132 ) AND thread_id > 0";
    public static final String[] PROJECTION_UNREAD_MMS = {"count(" + Telephony.Mms._ID + ")"};

    // sms
    public static final Uri OBSERVER_UNREAD_SMS = Uri.parse("content://sms/inbox-insert");//Telephony.Sms.CONTENT_URI;
    public static final Uri URI_UNREAD_SMS = Telephony.Sms.CONTENT_URI;
    public static final String SELECTION_UNREAD_SMS = "type =1 AND read = 0";
    public static final String[] PROJECTION_UNREAD_SMS = {"count(" + Telephony.Sms._ID + ")"};

}