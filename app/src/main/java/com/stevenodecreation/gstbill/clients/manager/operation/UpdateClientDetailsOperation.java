package com.stevenodecreation.gstbill.clients.manager.operation;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.stevenodecreation.gstbill.BaseResponse;
import com.stevenodecreation.gstbill.constant.UrlConstant;
import com.stevenodecreation.gstbill.exception.GstBillException;
import com.stevenodecreation.gstbill.manager.operation.WebServiceOperation;

import java.lang.reflect.Type;
import java.util.Locale;

/**
 * Created by Lenovo on 12/10/2017.
 */

public class UpdateClientDetailsOperation extends WebServiceOperation<BaseResponse> {

    public interface OnUpdateClientListener {
        void onUpdateClientSuccess(BaseResponse response);
        void onUpdateClientError(GstBillException exception);
    }

    private OnUpdateClientListener mOnUpdateClientListener;

    public UpdateClientDetailsOperation(String body, OnUpdateClientListener listener) {
        super(UrlConstant.UPDATE_CLIENT, Request.Method.POST, body, new TypeToken<BaseResponse>() {}.getType(),
                UpdateClientDetailsOperation.class.getSimpleName());
        mOnUpdateClientListener = listener;
    }

    public UpdateClientDetailsOperation(long clientId, OnUpdateClientListener listener) {
        super(String.format(Locale.getDefault(), UrlConstant.DELETE_CLIENT, clientId), Request.Method.DELETE, new TypeToken<BaseResponse>() {}.getType(),
                UpdateClientDetailsOperation.class.getSimpleName());
        mOnUpdateClientListener = listener;
    }

    @Override
    public void onError(GstBillException exception) {
        if (mOnUpdateClientListener != null) {
            mOnUpdateClientListener.onUpdateClientError(exception);
        }
    }

    @Override
    public void onSuccess(BaseResponse response) {
        if (mOnUpdateClientListener != null) {
            mOnUpdateClientListener.onUpdateClientSuccess(response);
        }
    }
}
