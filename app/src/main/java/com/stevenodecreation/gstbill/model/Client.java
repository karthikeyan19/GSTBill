package com.stevenodecreation.gstbill.model;

import java.util.List;

/**
 * Created by lenovo on 11-10-2017.
 */

public class Client {

    public long clientId;
    public int gstTreatment;
    public String name;
    public String emailId;
    public List<String> phoneNoList;

    public String nameOfBusiness;
    public String GstinNo;
    public PlaceOfSupply placeOfSupply;
    public int paymentTerms;
    public int customDaysForPayment;

    public Address billingAddress, shippingAddress;
}
