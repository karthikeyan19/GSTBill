package com.stevenodecreation.gstbill.products.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.stevenodecreation.gstbill.BaseFragment;
import com.stevenodecreation.gstbill.BaseResponse;
import com.stevenodecreation.gstbill.OnSubmitClickListener;
import com.stevenodecreation.gstbill.R;
import com.stevenodecreation.gstbill.adapter.GenericAutoCompleteAdapter;
import com.stevenodecreation.gstbill.exception.GstBillException;
import com.stevenodecreation.gstbill.model.Product;
import com.stevenodecreation.gstbill.products.adapter.ProductAutoCompleteAdapter;
import com.stevenodecreation.gstbill.products.manager.ProductManager;
import com.stevenodecreation.gstbill.util.DataUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 07-01-2018.
 */

public class EditProductFragment extends BaseFragment {

    private EditText mEditTextQuantity, mEditTextPrice, mEditTextHsnSacCode, mEditTextDiscountValue, mEditTextProdDesc;
    private AppCompatSpinner mSpinnerUnits, mSpinnerTaxRate;
    private RadioGroup mRadioGroupProductType, mRadioGroupDiscount;
    private AutoCompleteTextView mAutoCompleteTextViewProduct;
    private ScrollView mRootLayout;

    private int navigationType;
    private OnSubmitClickListener<Product> mOnSubmitClickListener;
    private Product mProduct;
    private ProductAutoCompleteAdapter mProductAutoCompleteAdapter;

    public static EditProductFragment newInstance(int type) {
        return newInstance(type, null);
    }

    public static EditProductFragment newInstance(int type, Product product) {
        EditProductFragment fragment = new EditProductFragment();
        fragment.navigationType = type;
        fragment.mProduct = product;
        return fragment;
    }

    public void setOnSubmitClickListener(OnSubmitClickListener<Product> listener) {
        mOnSubmitClickListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_product, container, false);

        mEditTextQuantity = view.findViewById(R.id.edittext_quantity);
        mEditTextPrice = view.findViewById(R.id.edittext_price);
        mEditTextHsnSacCode = view.findViewById(R.id.edittext_hsn_sac_code);
        mEditTextDiscountValue = view.findViewById(R.id.edittext_discount_value);
        mEditTextProdDesc = view.findViewById(R.id.edittext_product_description);

        mSpinnerUnits = view.findViewById(R.id.spinner_units);
        mSpinnerTaxRate = view.findViewById(R.id.spinner_tax_rate);

        mRadioGroupProductType = view.findViewById(R.id.radiogroup_product_type);
        mRadioGroupDiscount = view.findViewById(R.id.radiogroup_discount);

        mAutoCompleteTextViewProduct = view.findViewById(R.id.auto_comp_textview_product);
        mRootLayout = view.findViewById(R.id.root_layout);
        mProductAutoCompleteAdapter = new ProductAutoCompleteAdapter(getActivity(), new ArrayList<Product>());
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        mAutoCompleteTextViewProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 2) {
                    getProductList(editable.toString());
                }
            }
        });

        mProductAutoCompleteAdapter.setOnItemClickListener(new GenericAutoCompleteAdapter.OnItemClickListener<Product>() {
            @Override
            public void onItemClicked(Product item) {
                if (navigationType == Product.PRODUCT_UPDATE) {
                    showSnackBar(view, "Product already added");
                } else {
                    mProduct = item;
                    setProductDetails();
                }
            }
        });

        setProductDetails();
        super.onViewCreated(view, savedInstanceState);
    }

    private void setProductDetails() {
        if (mProduct == null)
            return;

        mRadioGroupProductType.check(mProduct.prodcutType);
        mAutoCompleteTextViewProduct.setText(mProduct.productName);
        mAutoCompleteTextViewProduct.dismissDropDown();
        mEditTextQuantity.setText(String.valueOf(mProduct.quantity));
        mEditTextPrice.setText(String.valueOf(mProduct.unitPrice));
        mEditTextHsnSacCode.setText(mProduct.hsnSacCode);
        mRadioGroupDiscount.check(mProduct.discountType == 0 ? R.id.radiobutton_discount_rupee : R.id.radiobutton_discount_perc);
        mEditTextDiscountValue.setText(String.valueOf(mProduct.discountValue));
        mEditTextProdDesc.setText(mProduct.productDescription != null ? mProduct.productDescription : "");
        mSpinnerUnits.setSelection(mProduct.unit);
        mSpinnerTaxRate.setSelection(mProduct.taxRate);
    }

    private void getProductList(final String query) {
        ProductManager manager = new ProductManager();
        manager.getProductList(DataUtil.generateUrlParamsValue(query), new ProductManager.OnGetProductListListener() {
            @Override
            public void OnGetProductListSuccess(List<Product> response) {
                mProductAutoCompleteAdapter = new ProductAutoCompleteAdapter(getActivity(), response);
                mAutoCompleteTextViewProduct.setThreshold(3);
                mAutoCompleteTextViewProduct.setAdapter(mProductAutoCompleteAdapter);
                mProductAutoCompleteAdapter.getFilter().filter(query);

            }

            @Override
            public void OnGetProductListError(GstBillException exception) {

            }

            @Override
            public void onGetProductListEmpty() {
                if (navigationType == Product.PRODUCT_SELECT)
                    clearProductDetails();
            }
        });
    }

    private void updateProduct() {
        mProduct = new Product();
        mProduct.prodcutType = mRadioGroupProductType.getCheckedRadioButtonId();
        mProduct.productName = mAutoCompleteTextViewProduct.getText().toString();
        mProduct.quantity = DataUtil.getInteger(mEditTextQuantity.getText().toString());
        mProduct.unitPrice = DataUtil.getDouble(mEditTextPrice.getText().toString());
        mProduct.unit = mSpinnerUnits.getSelectedItemPosition();
        mProduct.hsnSacCode = mEditTextHsnSacCode.getText().toString();
        mProduct.discountType = mRadioGroupDiscount.getCheckedRadioButtonId();
        mProduct.discountValue = DataUtil.getFloat(mEditTextDiscountValue.getText().toString());
        mProduct.taxRate = mSpinnerTaxRate.getSelectedItemPosition();
        mProduct.productDescription = mEditTextProdDesc.getText().toString();
        mProduct.lastUpdateTimestamp = System.currentTimeMillis();

        ProductManager manager = new ProductManager();
        manager.updateProduct(mProduct, new ProductManager.OnUpdateProductListener() {
            @Override
            public void onUpdateProductLSuccess(BaseResponse response) {
                showSnackBar(mRootLayout, response.message);
            }

            @Override
            public void onUpdateProductLError(GstBillException exception) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_product, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_save) {
            if (isValid()) {
                if (navigationType == Product.PRODUCT_UPDATE) {
                    updateProduct();
                } else if (mOnSubmitClickListener != null) {
                    mOnSubmitClickListener.onSubmitClicked(mProduct);
                }
            }
        }
        return true;
    }

    private boolean isValid() {
        boolean isValid = true;
        if (mSpinnerUnits.getSelectedItemPosition() <= 0) {
            isValid = false;
        } else if (TextUtils.isEmpty(mAutoCompleteTextViewProduct.getText().toString())) {
            isValid = false;
        } else if (TextUtils.isEmpty(mEditTextQuantity.getText().toString())) {
            isValid = false;
        } else if (TextUtils.isEmpty(mEditTextPrice.getText().toString()) && DataUtil.getDouble(mEditTextPrice.getText().toString()) <= 0) {
            isValid = false;
        } else if (mSpinnerTaxRate.getSelectedItemPosition() <= 0) {
            isValid = false;
        } else if (TextUtils.isEmpty(mEditTextHsnSacCode.getText().toString())) {
            isValid = false;
        }
        return isValid;
    }

    private void clearProductDetails() {
        mEditTextQuantity.setText("");
        mEditTextPrice.setText("");
        mEditTextHsnSacCode.setText("");
        mRadioGroupDiscount.check(R.id.radiobutton_discount_rupee);
        mEditTextDiscountValue.setText("");
        mEditTextProdDesc.setText("");
        mSpinnerUnits.setSelection(0);
        mSpinnerTaxRate.setSelection(0);
    }
}
