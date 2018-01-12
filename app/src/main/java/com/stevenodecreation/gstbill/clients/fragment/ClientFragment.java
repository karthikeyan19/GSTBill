package com.stevenodecreation.gstbill.clients.fragment;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.stevenodecreation.gstbill.BaseResponse;
import com.stevenodecreation.gstbill.OnSubmitClickListener;
import com.stevenodecreation.gstbill.R;
import com.stevenodecreation.gstbill.RunTimePermissionFragment;
import com.stevenodecreation.gstbill.adapter.GenericAutoCompleteAdapter;
import com.stevenodecreation.gstbill.clients.adapter.MyContactsAdapter;
import com.stevenodecreation.gstbill.clients.adapter.PlaceOfSupplyAdapter;
import com.stevenodecreation.gstbill.clients.manager.ClientManager;
import com.stevenodecreation.gstbill.constant.AppConstant;
import com.stevenodecreation.gstbill.exception.GstBillException;
import com.stevenodecreation.gstbill.model.Address;
import com.stevenodecreation.gstbill.model.Client;
import com.stevenodecreation.gstbill.model.MyContacts;
import com.stevenodecreation.gstbill.model.PlaceOfSupply;
import com.stevenodecreation.gstbill.util.FileUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 22-08-2017.
 */

public class ClientFragment extends RunTimePermissionFragment implements ClientManager.OnUpdateClientListener {

    private TextInputEditText mEditTextEmail, mEditTextPhone1, mEditTextPhone2, mEditTextBusinessName, mEditTextGSTIN, mEditTextPaymentDays;
    private Spinner mSpinnerPOS, mSpinnerCustomerType, mSpinnerPaymentTerms;
    private TextView mTextViewBillingAddress, mTextViewShippingAddress;

    // // TODO: 07-10-2017 android autocompletetextview with suggestions from a web service
    // // TODO: 12/10/2017 progress dialog for contacts, web servcie operation
    private AutoCompleteTextView mAutoCompleteContacts;
    private GenericAutoCompleteAdapter mContactsAdapter;
    private PlaceOfSupplyAdapter mPosAdapter;

    private Client mClient;

    public static ClientFragment newInstance() {
        return newInstance(null);
    }

    public static ClientFragment newInstance(Client client) {
        ClientFragment fragment = new ClientFragment();
        fragment.mClient = client != null ? client : new Client();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (hasPermission(new String[]{AppConstant.READ_CONTACTS_PERMISSION})) {
            showProgressDialog(getString(R.string.lbl_read_contacts));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    readContacts();
                }
            }).start();
        } else {
            requestAppPermissions(new String[]{AppConstant.READ_CONTACTS_PERMISSION}, AppConstant.READ_CONTACTS_REQUEST_CODE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_clients, container, false);

        mEditTextEmail = view.findViewById(R.id.edittext_email);
        mEditTextPhone1 = view.findViewById(R.id.edittext_phone1);
        mEditTextPhone2 = view.findViewById(R.id.edittext_phone2);
        mEditTextBusinessName = view.findViewById(R.id.edittext_business_name);
        mEditTextGSTIN = view.findViewById(R.id.edittext_gstin);
        mEditTextPaymentDays = view.findViewById(R.id.editTextCustomDaysForPayment);

        mAutoCompleteContacts = view.findViewById(R.id.auto_comp_textview_contacts);
        mAutoCompleteContacts.setThreshold(1);

        mTextViewBillingAddress = view.findViewById(R.id.textview_billing_address);
        mTextViewShippingAddress = view.findViewById(R.id.textview_shipping_address);

        mSpinnerPOS = view.findViewById(R.id.spinner_place_of_supply);
        mSpinnerCustomerType = view.findViewById(R.id.spinner_gst_treatment);
        mSpinnerPaymentTerms = view.findViewById(R.id.spinner_payment_terms);

        mSpinnerCustomerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 3) {
                    mEditTextBusinessName.setVisibility(View.GONE);
                    mEditTextGSTIN.setVisibility(View.GONE);
                } else {
                    mEditTextBusinessName.setVisibility(View.VISIBLE);
                    mEditTextGSTIN.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mSpinnerPaymentTerms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                mEditTextPaymentDays.setVisibility(position == 4 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (mPosAdapter == null)
            mPosAdapter = new PlaceOfSupplyAdapter();
        mSpinnerPOS.setAdapter(mPosAdapter);

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<PlaceOfSupply> posList = FileUtil.getFromAssetsFolder("pos", new TypeToken<List<PlaceOfSupply>>() {}.getType());
        mPosAdapter.setData(posList);
        mSpinnerCustomerType.setSelection(mClient.gstTreatment);
        mAutoCompleteContacts.setText(mClient.name != null ? mClient.name : "");
        mAutoCompleteContacts.dismissDropDown();
        mEditTextEmail.setText(mClient.emailId);

        if (mClient.phoneNoList != null) {
            for (int i = 0; i < mClient.phoneNoList.size(); i++) {
                if (i == 0)
                    mEditTextPhone1.setText(mClient.phoneNoList.get(i));
                else if (i == 1)
                    mEditTextPhone2.setText(mClient.phoneNoList.get(i));
            }
        }

        mSpinnerPOS.setSelection(mClient.placeOfSupply != null ? mClient.placeOfSupply.stateCode : 0);
        mSpinnerPaymentTerms.setSelection(mClient.paymentTerms);
        mEditTextBusinessName.setText(mClient.nameOfBusiness != null ? mClient.nameOfBusiness : "");
        mEditTextPaymentDays.setText(mClient.customDaysForPayment > 0 ? String.valueOf(mClient.customDaysForPayment) : "");

        mTextViewBillingAddress.setText(mClient.billingAddress != null ? createAddressString("Billing Address:", mClient.billingAddress) :
                getString(R.string.title_add_billing_address));
        mTextViewShippingAddress.setText(mClient.shippingAddress != null ? createAddressString("Shipping Address:", mClient.shippingAddress) :
                getString(R.string.title_add_shipping_address));

        mSpinnerPOS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mClient.placeOfSupply = mPosAdapter.getItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mTextViewBillingAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceAddressFragment(Address.ADDRESS_TYPE_BILLING);
            }
        });

        mTextViewShippingAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceAddressFragment(Address.ADDRESS_TYPE_SHIPPING);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dismissProgressDialog();
            mContactsAdapter = new MyContactsAdapter(getActivity(), (List<MyContacts>) msg.obj);
            mAutoCompleteContacts.setAdapter(mContactsAdapter);

            mAutoCompleteContacts.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable.toString().length() > 1)
                        mContactsAdapter.getFilter().filter(editable.toString());
                }
            });

            mContactsAdapter.setOnItemClickListener(new GenericAutoCompleteAdapter.OnItemClickListener<MyContacts>() {
                @Override
                public void onItemClicked(MyContacts item) {
                    mAutoCompleteContacts.setText(item.displayName);
                    mAutoCompleteContacts.dismissDropDown();

                    if (item.phoneNoList != null) {
                        if (item.phoneNoList.size() > 1)
                            showPhoneNoDialog(item);
                        else {
                            mEditTextPhone1.setText(item.phoneNoList.get(0));
                            hasMultipleEmail(item.emailIdList);
                        }
                    } else {
                        mEditTextPhone1.setText("");
                        hasMultipleEmail(item.emailIdList);
                    }
                }
            });
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_client, menu);
        if (mClient.clientId <= 0) {
            menu.findItem(R.id.menu_delete_client).setVisible(false);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_add_client) {
            if (isValid()) {
                sendDataToServer();
            }
        } else
            deleteClient(mClient.clientId);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void readContacts() {
        Cursor cur = null;
        final List<MyContacts> myContactsList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
        ContentResolver cr = getActivity().getContentResolver();
        try {
            cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);
            if (cur != null) {
                while (cur.moveToNext()) {
                    MyContacts contacts = new MyContacts();
                    contacts.id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                    contacts.displayName = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    System.out.println("log name = " + contacts.displayName);

                    int hasPhoneNumber = Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                    if (hasPhoneNumber > 0) {
                        //This is to read multiple phone numbers associated with the same contact
                        Cursor phoneCursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{contacts.id}, null);
                        System.out.println("log p.cursor count = " + phoneCursor.getCount());
                        while (phoneCursor.moveToNext()) {
                            if (contacts.phoneNoList == null)
                                contacts.phoneNoList = new ArrayList<>();

                            String phoneNum = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            String PhoneNoSpaceRemoved = phoneNum.replaceAll("\\s", "");
                            if (!contacts.phoneNoList.contains(PhoneNoSpaceRemoved))
                                contacts.phoneNoList.add(PhoneNoSpaceRemoved);
                        }
                        phoneCursor.close();
                    }

                    // To get Email Addresses
                    Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{contacts.id}, null);
                    System.out.println("log e.cursor count = " + emailCur.getCount());
                    while (emailCur.moveToNext()) {
                        if (contacts.emailIdList == null)
                            contacts.emailIdList = new ArrayList<>();

                        String emailId = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        if (!contacts.emailIdList.contains(emailId))
                            contacts.emailIdList.add(emailId);
                    }
                    emailCur.close();

                    if (contacts.phoneNoList != null || contacts.emailIdList != null) {
                        if (!nameList.contains(contacts.displayName)) {
                            nameList.add(contacts.displayName);
                            myContactsList.add(contacts);
                        }
                    }
                }
                Message msg = new Message();
                msg.obj = myContactsList;
                handler.sendMessage(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cur != null)
                cur.close();
        }

    }

    private void goToNextScreen() {
        mClient.gstTreatment = mSpinnerCustomerType.getSelectedItemPosition();
        mClient.name = mAutoCompleteContacts.getText().toString();
        mClient.emailId = mEditTextEmail.getText().toString();

        if (mClient.phoneNoList == null)
            mClient.phoneNoList = new ArrayList<>(AppConstant.PHONE_NO_LIST_SIZE);
        mClient.phoneNoList.clear();
        mClient.phoneNoList.add(mEditTextPhone1.getText().toString());
        if (!TextUtils.isEmpty(mEditTextPhone2.getText().toString()))
            mClient.phoneNoList.add(mEditTextPhone2.getText().toString());

        mClient.nameOfBusiness = mEditTextBusinessName.getText().toString();
        mClient.paymentTerms = mSpinnerPaymentTerms.getSelectedItemPosition();

        if (!TextUtils.isEmpty(mEditTextPaymentDays.getText().toString()))
            mClient.customDaysForPayment = Integer.parseInt(mEditTextPaymentDays.getText().toString());
    }

    private void sendDataToServer() {
        ClientManager manager = new ClientManager();
        manager.updateClientDetails(mClient, this);
    }

    private void deleteClient(long clientId) {
        ClientManager manager = new ClientManager();
        manager.deleteClient(clientId, this);
    }

    @Override
    public void onUpdateClientSuccess(BaseResponse response) {
        if (isAdded()) {
            Toast.makeText(getActivity(), response.message, Toast.LENGTH_SHORT).show();
            popAllFragmentUpTo(1);
        }
    }

    @Override
    public void onUpdateClientError(GstBillException exception) {
        if (isAdded()) {
            Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPermissionsGranted(int requestCode) {
        if (requestCode == AppConstant.READ_CONTACTS_REQUEST_CODE) {
            showProgressDialog("Reading Contacts...");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    readContacts();
                }
            }).start();
        }
    }

    private void showPhoneNoDialog(final MyContacts item) {
        String[] phoneNoArray = new String[item.phoneNoList.size()];
        phoneNoArray = item.phoneNoList.toArray(phoneNoArray);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Choose any one Number")
                .setCancelable(false)
                .setSingleChoiceItems(phoneNoArray, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        mEditTextPhone1.setText(item.phoneNoList.get(which));
                        dialogInterface.dismiss();

                        hasMultipleEmail(item.emailIdList);
                    }
                });
    }

    private void showEmailDialog(final List<String> emailIdList) {
        String[] emailArray = new String[emailIdList.size()];
        emailArray = emailIdList.toArray(emailArray);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Choose any one Email Id")
                .setCancelable(false)
                .setSingleChoiceItems(emailArray, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        mEditTextEmail.setText(emailIdList.get(which));
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }

    private void hasMultipleEmail(List<String> emailIdList) {
        if (emailIdList != null) {
            if (emailIdList.size() > 1)
                showEmailDialog(emailIdList);
            else
                mEditTextEmail.setText(emailIdList.get(0));
        } else
            mEditTextEmail.setText("");
    }

    private void replaceAddressFragment(final int type) {
        goToNextScreen();
        AddressFragment fragment = type == Address.ADDRESS_TYPE_BILLING ? BillingAdressFragment.newInstance(mClient) :
                ShippingAddressFragment.newInstance(mClient);
        fragment.setOnSubmitClickListener(new OnSubmitClickListener<Address>() {
            @Override
            public void onSubmitClicked(Address address) {
                if (type == Address.ADDRESS_TYPE_BILLING)
                    mClient.billingAddress = address;
                else
                    mClient.shippingAddress = address;
            }
        });
        replace(R.id.fragment_host, fragment);
    }

    private boolean isValid() {
        boolean isValid = true;
        if (mSpinnerCustomerType.getSelectedItemPosition() <= 0) {
            isValid = false;
        } else if (TextUtils.isEmpty(mAutoCompleteContacts.getText().toString())) {
            isValid = false;
        } else if (TextUtils.isEmpty(mEditTextEmail.getText().toString())) {
            isValid = false;
        } else if (TextUtils.isEmpty(mEditTextPhone1.getText().toString())) {
            isValid = false;
        } /*else if (!TextUtils.isEmpty(mEditTextGSTIN.getText().toString())) {
            if (!isGstinValid(mEditTextGSTIN.getText().toString()))
                isValid = false;
        } */else if (mSpinnerPOS.getSelectedItemPosition() <= 0) {
            isValid = false;
        } else if (mSpinnerPaymentTerms.getSelectedItemPosition() <= 0) {
            isValid = false;
        } else if (mClient.billingAddress == null || mClient.shippingAddress == null) {
            isValid = false;
        }
        return isValid;
    }

    private boolean isGstinValid(String gstin) {
        if (gstin.matches(AppConstant.REGEX_PATTERN_ALPHA_NUM))
            return false;
        boolean isStateCodeValid = false;

        try {
            int stateCode = Integer.parseInt(gstin.substring(0, 2));
            if (stateCode > 0 && stateCode < 37)
                isStateCodeValid = true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return isStateCodeValid && (gstin.charAt(gstin.length() - 2) == AppConstant.UNICODE_CHAR_Z);
    }

    private SpannableStringBuilder createAddressString(String header, Address address) {
        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append(header).append("\n").append(address.toString());
        sb.setSpan(new StyleSpan(Typeface.BOLD), 0, (sb.toString().length() - address.toString().length()), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return sb;
    }
}
