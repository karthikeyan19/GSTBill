package com.stevenodecreation.gstbill.util;

import android.util.Log;

import com.stevenodecreation.gstbill.BuildConfig;
import com.stevenodecreation.gstbill.Config;

/**
 * Created by Karthikeyan on 04-08-2017.
 */

public class GBillLoggerUtil {

    private static boolean mLogEnabled = Config.DEBUG;

    public static void debug(Object tag, String msg) {
        if (mLogEnabled) {
            //log tag becomes a package name
            Log.d(tag.toString(), msg);
        }
    }

    public static void error(Object tag, String msg) {
        if (mLogEnabled) {
            Log.e(tag.toString(), msg);
        }
    }

    public static void println(String msg) {
        if (mLogEnabled) {
            System.out.println(msg);
        }
    }
}
