package com.stevenodecreation.gstbill.manager;

import com.google.gson.Gson;
import com.stevenodecreation.gstbill.BaseResponse;
import com.stevenodecreation.gstbill.exception.GstBillException;
import com.stevenodecreation.gstbill.manager.operation.CreateEmployeeOperation;
import com.stevenodecreation.gstbill.manager.operation.WebServiceOperation;
import com.stevenodecreation.gstbill.model.Employee;

/**
 * Created by lenovo on 06-10-2017.
 */

public class EmployeeManager extends WebServiceManager {

    public interface CreateEmpListener {
        void onCreateEmpSuccess(BaseResponse response);
        void onCreateEmpError(GstBillException e);
    }

    public void createEmployee(Employee emp, final CreateEmpListener listener) {
        WebServiceOperation operation = new CreateEmployeeOperation(new Gson().toJson(emp),
                new CreateEmployeeOperation.CreateEmpListener() {
            @Override
            public void onCreateEmpSuccess(BaseResponse response) {
                if (listener != null) {
                    listener.onCreateEmpSuccess(response);
                }
            }

            @Override
            public void onCreateEmpError(GstBillException e) {
                if (listener != null) {
                    listener.onCreateEmpError(e);
                }
            }
        });
        operation.addToRequestQueue();
    }
}
