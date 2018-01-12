package com.stevenodecreation.gstbill.util;

import android.text.TextUtils;

/**
 * Created by lenovo on 07-01-2018.
 */

public class DataUtil {

    public static double getDouble(String doubleValue) {
        double val = 0D;
        try {
            val = Double.parseDouble(doubleValue);
            return val;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getInteger(String intValue) {
        int val = 0;
        try {
            val = Integer.parseInt(intValue);
            return val;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static float getFloat(String floatValue) {
        float val = 0f;
        try {
            val = Float.parseFloat(floatValue);
            return val;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String generateUrlParamsValue(String paramValue) {
        if (TextUtils.isEmpty(paramValue))
            return "";

        String[] words = paramValue.split(" ");
        return words.length > 1 ? TextUtils.join("+", words) : words[0];
    }
}
