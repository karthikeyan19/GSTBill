package com.stevenodecreation.gstbill.products.manager.opeation;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.stevenodecreation.gstbill.constant.UrlConstant;
import com.stevenodecreation.gstbill.exception.GstBillException;
import com.stevenodecreation.gstbill.manager.operation.WebServiceOperation;
import com.stevenodecreation.gstbill.model.Client;
import com.stevenodecreation.gstbill.model.Product;

import java.util.List;

/**
 * Created by Lenovo on 08/01/2018.
 */

public class GetProductListOperation extends WebServiceOperation<List<Product>> {

    public interface OnGetProductListListener {
        void onGetProductListSuccess(List<Product> response);
        void onGetProductListError(GstBillException exception);
    }

    private OnGetProductListListener mOnGetProductListListener;

    public GetProductListOperation(String query, OnGetProductListListener listener) {
        super(String.format(UrlConstant.GET_PRODUCT_LIST, query), Request.Method.GET,
                new TypeToken<List<Product>>() {}.getType(), GetProductListOperation.class.getSimpleName());
        mOnGetProductListListener = listener;
    }
    @Override
    public void onError(GstBillException exception) {
        if (mOnGetProductListListener != null) {
            mOnGetProductListListener.onGetProductListError(exception);
        }
    }

    @Override
    public void onSuccess(List<Product> response) {
        if (mOnGetProductListListener != null) {
            mOnGetProductListListener.onGetProductListSuccess(response);
        }
    }
}
