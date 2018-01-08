package com.stevenodecreation.gstbill.products.manager.opeation;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.stevenodecreation.gstbill.BaseResponse;
import com.stevenodecreation.gstbill.clients.manager.operation.UpdateClientDetailsOperation;
import com.stevenodecreation.gstbill.constant.UrlConstant;
import com.stevenodecreation.gstbill.exception.GstBillException;
import com.stevenodecreation.gstbill.manager.operation.WebServiceOperation;

import java.util.Locale;

/**
 * Created by Lenovo on 08/01/2018.
 */

public class UpdateProductOperation extends WebServiceOperation<BaseResponse> {

    public interface OnUpdateProductListener {
        void onUpdateProductLSuccess(BaseResponse response);
        void onUpdateProductLError(GstBillException exception);
    }

    private OnUpdateProductListener mOnUpdateProductListener;

    public UpdateProductOperation(String body,  OnUpdateProductListener listener) {
        super(UrlConstant.UPDATE_PRODUCT, Request.Method.POST, body, new TypeToken<BaseResponse>() {}.getType(),
                UpdateClientDetailsOperation.class.getSimpleName());
        mOnUpdateProductListener = listener;
    }

    public UpdateProductOperation(long productId, OnUpdateProductListener listener) {
        super(String.format(Locale.getDefault(), UrlConstant.DELETE_PRODUCT, productId), Request.Method.DELETE, new TypeToken<BaseResponse>() {}.getType(),
                UpdateClientDetailsOperation.class.getSimpleName());
        mOnUpdateProductListener = listener;
    }

    @Override
    public void onError(GstBillException exception) {
        if (mOnUpdateProductListener != null) {
            mOnUpdateProductListener.onUpdateProductLError(exception);
        }
    }

    @Override
    public void onSuccess(BaseResponse response) {
        if (mOnUpdateProductListener != null) {
            mOnUpdateProductListener.onUpdateProductLSuccess(response);
        }
    }
}
