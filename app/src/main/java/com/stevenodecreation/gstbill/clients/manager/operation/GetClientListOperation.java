package com.stevenodecreation.gstbill.clients.manager.operation;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.stevenodecreation.gstbill.BaseResponse;
import com.stevenodecreation.gstbill.constant.UrlConstant;
import com.stevenodecreation.gstbill.exception.GstBillException;
import com.stevenodecreation.gstbill.manager.operation.WebServiceOperation;
import com.stevenodecreation.gstbill.model.Client;

import java.util.List;
import java.util.Locale;

/**
 * Created by lenovo on 22-11-2017.
 */

public class GetClientListOperation extends WebServiceOperation<List<Client>> {

    public interface OnGetClientListListener {
        void OnGetClientListSuccess(List<Client> response);
        void OnGetClientListError(GstBillException exception);
    }

    private OnGetClientListListener mOnGetClientListListener;

    public GetClientListOperation(int from, OnGetClientListListener listener) {
        super(String.format(Locale.getDefault(), UrlConstant.GET_CLIENT_LIST, from), Request.Method.GET,
                new TypeToken() {}.getType(), GetClientListOperation.class.getSimpleName());
        mOnGetClientListListener = listener;
    }

    @Override
    public void onError(GstBillException exception) {
        if (mOnGetClientListListener != null) {
            mOnGetClientListListener.OnGetClientListError(exception);
        }
    }

    @Override
    public void onSuccess(List<Client> response) {
        if (mOnGetClientListListener != null) {
            mOnGetClientListListener.OnGetClientListSuccess(response);
        }
    }
}
