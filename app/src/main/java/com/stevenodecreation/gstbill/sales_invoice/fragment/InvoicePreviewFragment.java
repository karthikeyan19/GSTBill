package com.stevenodecreation.gstbill.sales_invoice.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stevenodecreation.gstbill.BaseFragment;
import com.stevenodecreation.gstbill.R;

/**
 * Created by lenovo on 16-01-2018.
 */

public class InvoicePreviewFragment extends BaseFragment {

    public static InvoicePreviewFragment newInstance() {
        return new InvoicePreviewFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invoice_preview, container, false);
        return view;
    }
}
