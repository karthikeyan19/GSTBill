package com.stevenodecreation.gstbill.model;

/**
 * Created by Lenovo on 11/10/2017.
 */

public class Address {

    public static final int ADDRESS_TYPE_BILLING = 0;
    public static final int ADDRESS_TYPE_SHIPPING = ADDRESS_TYPE_BILLING + 1;

    public String addressLine1;
    public String addressLine2;
    public String city;
    public PlaceOfSupply state;
    public String pinCode;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(addressLine1).append("\n").append(addressLine2).append("\n")
                .append(city).append(", ").append(state.stateName).append("\n")
                .append(pinCode);
        return builder.toString();
    }
}
