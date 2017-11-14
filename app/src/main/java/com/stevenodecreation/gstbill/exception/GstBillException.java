package com.stevenodecreation.gstbill.exception;


public class GstBillException extends Exception {

    private int statusCode;
    private String message;

    public GstBillException(int statusCode) {
        this(statusCode, "Something wrong!!!.Please try after sometime.");
    }

    public GstBillException(String message) {
        this(-1, message);
    }

    public GstBillException(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message != null ? message : this.message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
