package com.stevenodecreation.gstbill.clients.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.stevenodecreation.gstbill.BaseActivity;
import com.stevenodecreation.gstbill.R;
import com.stevenodecreation.gstbill.model.Address;
import com.stevenodecreation.gstbill.model.Client;

/**
 * Created by lenovo on 12-11-2017.
 */

public class ShippingAddressFragment extends AddressFragment {

    private Switch mSwitchAddress;
    private Address mBillingAddress;

    public static ShippingAddressFragment newInstance(Client client) {
        ShippingAddressFragment fragment = new ShippingAddressFragment();
        fragment.id = client.clientId;
        fragment.mBillingAddress = client.billingAddress;
        fragment.mAddress = client.shippingAddress != null ? client.shippingAddress : new Address();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        ((BaseActivity)getActivity()).setTitle(R.string.lbl_shipping_address);
        mButtonAddress.setText(id <= 0 ? R.string.title_add_shipping_address : R.string.title_edit_shipping_address);

        mLinearLayoutSwitch.setVisibility(View.VISIBLE);
        mSwitchAddress = view.findViewById(R.id.switch_address);
        mSwitchAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && mBillingAddress != null) {
                    mAddress = mBillingAddress;
                    isNewAddress = false;
                    setAddressDetails();
                }
            }
        });

        return view;
    }
}
