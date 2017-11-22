package com.stevenodecreation.gstbill.clients.manager;

import com.google.gson.Gson;
import com.stevenodecreation.gstbill.BaseResponse;
import com.stevenodecreation.gstbill.clients.manager.operation.GetClientListOperation;
import com.stevenodecreation.gstbill.clients.manager.operation.UpdateClientDetailsOperation;
import com.stevenodecreation.gstbill.exception.GstBillException;
import com.stevenodecreation.gstbill.manager.WebServiceManager;
import com.stevenodecreation.gstbill.model.Client;

import java.util.List;

/**
 * Created by Lenovo on 12/10/2017.
 */

public class ClientManager extends WebServiceManager {

    public interface OnUpdateClientListener {
        void onUpdateClientSuccess(BaseResponse response);
        void onUpdateClientError(GstBillException exception);
    }

    public void updateClientDetails(Client client, final OnUpdateClientListener listener) {
        UpdateClientDetailsOperation operation = new UpdateClientDetailsOperation(new Gson().toJson(client),
                new UpdateClientDetailsOperation.OnUpdateClientListener() {
                    @Override
                    public void onUpdateClientSuccess(BaseResponse response) {
                        if (listener != null) {
                            listener.onUpdateClientSuccess(response);
                        }
                    }

                    @Override
                    public void onUpdateClientError(GstBillException exception) {
                        if (listener != null) {
                            listener.onUpdateClientError(exception);
                        }
                    }
                });
        operation.addToRequestQueue();
    }

    public void deleteClient(long clientId, final OnUpdateClientListener listener) {
        UpdateClientDetailsOperation operation = new UpdateClientDetailsOperation(clientId,
                new UpdateClientDetailsOperation.OnUpdateClientListener() {
                    @Override
                    public void onUpdateClientSuccess(BaseResponse response) {
                        if (listener != null) {
                            listener.onUpdateClientSuccess(response);
                        }
                    }

                    @Override
                    public void onUpdateClientError(GstBillException exception) {
                        if (listener != null) {
                            listener.onUpdateClientError(exception);
                        }
                    }
                });
        operation.addToRequestQueue();
    }

    public interface OnGetClientListListener {
        void OnGetClientListSuccess(List<Client> response);
        void OnGetClientListError(GstBillException exception);
        void onGetClientListEmpty();
    }

    public void getClientList(int from, final OnGetClientListListener listener) {
        GetClientListOperation operation = new GetClientListOperation(from, new GetClientListOperation.OnGetClientListListener() {
            @Override
            public void OnGetClientListSuccess(List<Client> response) {
                if (listener != null) {
                    if (response.isEmpty())
                        listener.onGetClientListEmpty();
                    else
                        listener.OnGetClientListSuccess(response);
                }
            }

            @Override
            public void OnGetClientListError(GstBillException exception) {
                if (listener != null)
                    listener.OnGetClientListError(exception);
            }
        });
        operation.addToRequestQueue();
    }
}
