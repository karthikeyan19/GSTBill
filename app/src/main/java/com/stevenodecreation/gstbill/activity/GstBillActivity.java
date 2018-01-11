package com.stevenodecreation.gstbill.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.stevenodecreation.gstbill.BaseActivity;
import com.stevenodecreation.gstbill.R;
import com.stevenodecreation.gstbill.clients.fragment.ClientFragment;
import com.stevenodecreation.gstbill.clients.fragment.ClientListFragment;
import com.stevenodecreation.gstbill.model.Product;
import com.stevenodecreation.gstbill.products.fragment.EditProductFragment;
import com.stevenodecreation.gstbill.products.fragment.ProductListFragment;

/**
 * Created by lenovo on 22-08-2017.
 */

public class GstBillActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gstbill);

        replace(R.id.fragment_host, EditProductFragment.newInstance(Product.PRODUCT_UPDATE));
    }
}
