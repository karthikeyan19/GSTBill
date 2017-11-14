package com.stevenodecreation.gstbill.clients.fragment;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.gson.reflect.TypeToken;
import com.stevenodecreation.gstbill.BaseActivity;
import com.stevenodecreation.gstbill.BaseFragment;
import com.stevenodecreation.gstbill.OnSubmitClickListener;
import com.stevenodecreation.gstbill.R;
import com.stevenodecreation.gstbill.clients.adapter.PlaceOfSupplyAdapter;
import com.stevenodecreation.gstbill.model.Address;
import com.stevenodecreation.gstbill.model.Client;
import com.stevenodecreation.gstbill.model.PlaceOfSupply;
import com.stevenodecreation.gstbill.util.FileUtil;

import java.util.List;

/**
 * Created by Lenovo on 12/10/2017.
 */
public class AddressFragment extends BaseFragment {

    private TextInputEditText mEditTextAddressLine1, mEditTextAddressLine2, mEditTextCity, mEditTextPincode;
    private Spinner mSpinnerState;
    protected LinearLayout mLinearLayoutSwitch;
    protected Button mButtonAddress;

    protected long id;
    protected boolean isNewAddress;
    protected Address mAddress;

    private OnSubmitClickListener mOnSubmitClickListener;

    public void setOnSubmitClickListener(OnSubmitClickListener listener) {
        mOnSubmitClickListener = listener;
    }

   /* public static AddressFragment newInstance() {
        return new AddressFragment();
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_address, container, false);

        mButtonAddress = view.findViewById(R.id.button_address);
        mEditTextAddressLine1 = view.findViewById(R.id.edittext_addressline1);
        mEditTextAddressLine2 = view.findViewById(R.id.edittext_addressline2);
        mEditTextCity = view.findViewById(R.id.edittext_city);
        mEditTextPincode = view.findViewById(R.id.edittext_pincode);
        mLinearLayoutSwitch = view.findViewById(R.id.layout_switch);

        mSpinnerState = view.findViewById(R.id.spinner_state);

        final PlaceOfSupplyAdapter mPosAdapter = new PlaceOfSupplyAdapter();
        mSpinnerState.setAdapter(mPosAdapter);

        List<PlaceOfSupply> posList = FileUtil.getFromAssetsFolder("pos", new TypeToken<List<PlaceOfSupply>>() {}.getType());
        mPosAdapter.setData(posList);

        mSpinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mAddress.state = mPosAdapter.getItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mButtonAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid()) {
                    mAddress.addressLine1 = mEditTextAddressLine1.getText().toString();
                    mAddress.addressLine2 = mEditTextAddressLine2.getText().toString();
                    mAddress.city = mEditTextCity.getText().toString();
                    mAddress.pinCode = mEditTextPincode.getText().toString();

                    if (mOnSubmitClickListener != null) {
                        mOnSubmitClickListener.onSubmitClicked(mAddress);
                    }
                    pop();
                }
            }
        });
        setAddressDetails();
        return view;
    }

    protected void setAddressDetails() {
        mEditTextAddressLine1.setText(mAddress.addressLine1);
        mEditTextAddressLine2.setText(mAddress.addressLine2);
        mEditTextCity.setText(mAddress.city);
        mEditTextPincode.setText(mAddress.pinCode);
        if (mAddress.state != null)
            mSpinnerState.setSelection(mAddress.state.stateCode);
    }

    private boolean isValid() {
        boolean isValid = true;
        if (TextUtils.isEmpty(mEditTextAddressLine1.getText().toString())) {
            isValid = false;
        } else if (TextUtils.isEmpty(mEditTextAddressLine2.getText().toString())) {
            isValid = false;
        } else if (TextUtils.isEmpty(mEditTextCity.getText().toString())) {
            isValid = false;
        } else if (TextUtils.isEmpty(mEditTextPincode.getText().toString())) {
            isValid = false;
        } else if (mSpinnerState.getSelectedItemPosition() <= 0) {
            isValid = false;
        }
        return isValid;
    }
}