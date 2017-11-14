package com.stevenodecreation.gstbill.clients.manager;

import com.google.gson.Gson;
import com.stevenodecreation.gstbill.BaseResponse;
import com.stevenodecreation.gstbill.clients.manager.operation.UpdateClientDetailsOperation;
import com.stevenodecreation.gstbill.exception.GstBillException;
import com.stevenodecreation.gstbill.manager.WebServiceManager;
import com.stevenodecreation.gstbill.model.Client;

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
}
