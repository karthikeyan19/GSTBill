package com.stevenodecreation.gstbill.util;

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
}
