package com.stevenodecreation.gstbill.clients;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.stevenodecreation.gstbill.BaseFragment;
import com.stevenodecreation.gstbill.BaseResponse;
import com.stevenodecreation.gstbill.R;
import com.stevenodecreation.gstbill.exception.GstBillException;
import com.stevenodecreation.gstbill.manager.EmployeeManager;
import com.stevenodecreation.gstbill.model.Employee;

/**
 * Created by lenovo on 06-10-2017.
 */

public class CreateEmpFragment extends BaseFragment {

    public static CreateEmpFragment newInstance() {
        return new CreateEmpFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_emp, container, false);

        final EditText editTextName = view.findViewById(R.id.edittext_name);
        final EditText editTextAge = view.findViewById(R.id.edittext_age);
        Button buttonSendServer = view.findViewById(R.id.btn_send_server);

        buttonSendServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Employee emp = new Employee();
                emp.name = editTextName.getText().toString();
                emp.age = Integer.parseInt(editTextAge.getText().toString());

                EmployeeManager manager = new EmployeeManager();
                manager.createEmployee(emp, new EmployeeManager.CreateEmpListener() {
                    @Override
                    public void onCreateEmpSuccess(BaseResponse response) {
                        Toast.makeText(getActivity(), response.message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCreateEmpError(GstBillException e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return view;
    }
}
