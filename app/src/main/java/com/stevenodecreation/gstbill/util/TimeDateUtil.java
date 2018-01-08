package com.stevenodecreation.gstbill.util;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Lenovo on 08/01/2018.
 */

public final class TimeDateUtil {

    public static final String DATE_TIME_FORMAT_dd_MMM_yyyy = "dd MMM yyyy";

    public static String getFormattedString(long timeStamp, String type) {
        SimpleDateFormat currentDateFormat = new SimpleDateFormat(type, Locale.getDefault());
        if (TextUtils.isEmpty(String.valueOf(timeStamp))) {
            return String.valueOf(currentDateFormat.format(new Date()));
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timeStamp);
            return String.valueOf(currentDateFormat.format(calendar.getTime()));
        }
    }
}
