package com.stevenodecreation.gstbill.manager;


import java.util.HashMap;
import java.util.Map;

public class WebServiceManager {

    private Map<String, String> headers = new HashMap<>();

    protected Map<String, String> getHeaders() {
        return headers;
    }
}
