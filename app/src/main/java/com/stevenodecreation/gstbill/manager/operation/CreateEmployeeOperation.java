package com.stevenodecreation.gstbill.manager.operation;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.stevenodecreation.gstbill.BaseResponse;
import com.stevenodecreation.gstbill.constant.UrlConstant;
import com.stevenodecreation.gstbill.exception.GstBillException;

/**
 * Created by lenovo on 06-10-2017.
 */

public class CreateEmployeeOperation extends WebServiceOperation<BaseResponse> {

    public interface CreateEmpListener {
        void onCreateEmpSuccess(BaseResponse response);
        void onCreateEmpError(GstBillException e);
    }

    private CreateEmpListener mCreateEmpListener;

    public CreateEmployeeOperation(String body, CreateEmpListener listener) {
        super(UrlConstant.CREATE_EMP_URI, Request.Method.POST, body, new TypeToken<BaseResponse>() {}.getType(),
                CreateEmployeeOperation.class.getSimpleName());
        mCreateEmpListener = listener;

    }

    @Override
    public void onError(GstBillException exception) {
        if (mCreateEmpListener != null) {
            mCreateEmpListener.onCreateEmpError(exception);
        }
    }

    @Override
    public void onSuccess(BaseResponse response) {
        if (mCreateEmpListener != null) {
            mCreateEmpListener.onCreateEmpSuccess(response);
        }
    }
}
