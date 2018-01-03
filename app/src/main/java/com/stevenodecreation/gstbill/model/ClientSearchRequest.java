package com.stevenodecreation.gstbill.model;

import com.stevenodecreation.gstbill.constant.AppConstant;

/**
 * Created by Lenovo on 03/01/2018.
 */

public class ClientSearchRequest {

    public static final int SEARCH_BY_NAME = 1;
    public static final int SEARCH_BY_MOBILE = SEARCH_BY_NAME + 1;

    public String name;
    public String mobileNo;
    public int searchType;
    public int from;
    public int pageLimit = AppConstant.PAGE_LIMIT;
}
