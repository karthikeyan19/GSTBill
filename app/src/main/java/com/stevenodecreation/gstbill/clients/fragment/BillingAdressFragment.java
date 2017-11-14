package com.stevenodecreation.gstbill.clients.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stevenodecreation.gstbill.BaseActivity;
import com.stevenodecreation.gstbill.R;
import com.stevenodecreation.gstbill.model.Address;
import com.stevenodecreation.gstbill.model.Client;

/**
 * Created by lenovo on 15-10-2017.
 */

public class BillingAdressFragment extends AddressFragment {

    public static BillingAdressFragment newInstance(Client client) {
        BillingAdressFragment fragment = new BillingAdressFragment();
        fragment.id = client.clientId;
        fragment.mAddress = client.billingAddress != null ? client.billingAddress : new Address();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        mLinearLayoutSwitch.setVisibility(View.GONE);
        ((BaseActivity) getActivity()).setTitle(R.string.lbl_billing_address);
        mButtonAddress.setText(id <= 0 ? R.string.title_add_billing_address : R.string.title_edit_billing_address);
        return view;
    }
}
