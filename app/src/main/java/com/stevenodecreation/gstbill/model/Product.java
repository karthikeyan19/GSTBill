package com.stevenodecreation.gstbill.model;

/**
 * Created by lenovo on 07-01-2018.
 */

public class Product {

    public static final int PRODUCT_SELECT = 0;
    public static final int PRODUCT_UPDATE = PRODUCT_SELECT + 1;
//    public static final int PRODUCT_UPDATE = PRODUCT_CREATE + 1;

    public long productId;
    public int prodcutType;
    public String productName;
    public int quantity;
    public double unitPrice;
    public int unit;
    public String hsnSacCode;
    public int discountType;
    public float discountValue;
    public int taxRate;
    public String productDescription;
}
