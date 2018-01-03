package com.stevenodecreation.gstbill.constant;

import android.Manifest;

/**
 * Created by lenovo on 29-09-2017.
 */

public final class AppConstant {

    private AppConstant () {}

    public static final String READ_CONTACTS_PERMISSION = Manifest.permission.READ_CONTACTS;
    public static final int READ_CONTACTS_REQUEST_CODE = 1;
    public static final int PHONE_NO_LIST_SIZE = 2;
    public static final String REGEX_PATTERN_ALPHA_NUM = "^[a-zA-Z0-9]*$";
    public static final char UNICODE_CHAR_Z = '\u005A';
    public static final int PAGE_LIMIT = 10;
}
