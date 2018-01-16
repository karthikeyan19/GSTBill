package com.stevenodecreation.gstbill.sales_invoice.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.stevenodecreation.gstbill.BaseFragment;
import com.stevenodecreation.gstbill.R;
import com.stevenodecreation.gstbill.sales_invoice.fragment.CustomerDetailsFragment;
import com.stevenodecreation.gstbill.sales_invoice.fragment.InvoicePreviewFragment;
import com.stevenodecreation.gstbill.sales_invoice.fragment.ItemsListFragment;
import com.stevenodecreation.gstbill.sales_invoice.fragment.PaymentFragment;

/**
 * Created by lenovo on 16-01-2018.
 */

public class SalesInvoiceViewPagerAdapter extends FragmentStatePagerAdapter {

    private static final byte TAB_CUSTOMER = 0;
    private static final byte TAB_ITEMS = TAB_CUSTOMER + 1;
    private static final byte TAB_PAYMENT = TAB_ITEMS + 1;
    private static final byte TAB_INVOICE_PERVIEW = TAB_PAYMENT + 1;

    private static String[] TAB_TITLE;

    public SalesInvoiceViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        TAB_TITLE = context.getResources().getStringArray(R.array.array_sales_invoice_title);
    }

    @Override
    public BaseFragment getItem(int position) {
        switch (position) {
            case TAB_CUSTOMER:
                return CustomerDetailsFragment.newInstance();
            case TAB_ITEMS:
                return ItemsListFragment.newInstance();
            case TAB_PAYMENT:
                return PaymentFragment.newInstance();
            default:
                return InvoicePreviewFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return TAB_TITLE.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLE[position];
    }
}
