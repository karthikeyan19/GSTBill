package com.stevenodecreation.gstbill.products.manager;

import com.google.gson.Gson;
import com.stevenodecreation.gstbill.BaseResponse;
import com.stevenodecreation.gstbill.exception.GstBillException;
import com.stevenodecreation.gstbill.manager.WebServiceManager;
import com.stevenodecreation.gstbill.model.Client;
import com.stevenodecreation.gstbill.model.Product;
import com.stevenodecreation.gstbill.products.manager.opeation.GetProductListOperation;
import com.stevenodecreation.gstbill.products.manager.opeation.UpdateProductOperation;

import java.util.List;

/**
 * Created by Lenovo on 08/01/2018.
 */

// TODO why we should not club / merge all POST operation which returns only base response
public class ProductManager extends WebServiceManager {

    public interface OnUpdateProductListener {
        void onUpdateProductLSuccess(BaseResponse response);
        void onUpdateProductLError(GstBillException exception);
    }

    public void updateProduct(Product item, final OnUpdateProductListener listener) {
        UpdateProductOperation operation = new UpdateProductOperation(new Gson().toJson(item),
                new UpdateProductOperation.OnUpdateProductListener() {
            @Override
            public void onUpdateProductLSuccess(BaseResponse response) {
                if (listener != null)
                    listener.onUpdateProductLSuccess(response);
            }

            @Override
            public void onUpdateProductLError(GstBillException exception) {
                if (listener != null)
                    listener.onUpdateProductLError(exception);
            }
        });
        operation.addToRequestQueue();
    }

    public interface OnGetProductListListener {
        void OnGetProductListSuccess(List<Product> response);
        void OnGetProductListError(GstBillException exception);
        void onGetProductListEmpty();
    }

    public void getProductList(String query, final OnGetProductListListener listener) {
        GetProductListOperation operation = new GetProductListOperation(query,
                new GetProductListOperation.OnGetProductListListener() {
                    @Override
                    public void onGetProductListSuccess(List<Product> response) {
                        if (listener != null) {
                            if (response.isEmpty())
                                listener.onGetProductListEmpty();
                            else listener.OnGetProductListSuccess(response);
                        }
                    }

                    @Override
                    public void onGetProductListError(GstBillException exception) {
                        if (listener != null)
                            listener.OnGetProductListError(exception);
                    }
                });
        operation.addToRequestQueue();
    }
}
