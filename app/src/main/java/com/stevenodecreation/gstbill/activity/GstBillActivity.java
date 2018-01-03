package com.stevenodecreation.gstbill.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.stevenodecreation.gstbill.BaseActivity;
import com.stevenodecreation.gstbill.R;
import com.stevenodecreation.gstbill.clients.fragment.ClientFragment;

/**
 * Created by lenovo on 22-08-2017.
 */

public class GstBillActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gstbill);

        replace(R.id.fragment_host, ClientFragment.newInstance());
    }
}
