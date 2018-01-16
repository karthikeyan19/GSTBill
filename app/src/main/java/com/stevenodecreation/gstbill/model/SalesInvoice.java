package com.stevenodecreation.gstbill.model;

import java.util.List;

/**
 * Created by Lenovo on 12/01/2018.
 */

public class SalesInvoice {

    public Client client;
    public List<Product> productList;
    public PaymentDetails paymentDetails;
    public long invoiceNo;
    public long invoiceDate;


}
