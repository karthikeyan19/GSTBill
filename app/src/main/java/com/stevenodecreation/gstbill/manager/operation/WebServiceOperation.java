package com.stevenodecreation.gstbill.manager.operation;


import android.content.res.AssetManager;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.stevenodecreation.gstbill.BaseResponse;
import com.stevenodecreation.gstbill.GstBillApplication;
import com.stevenodecreation.gstbill.constant.UrlConstant;
import com.stevenodecreation.gstbill.exception.GstBillException;
import com.stevenodecreation.gstbill.util.ConnectionUtil;
import com.stevenodecreation.gstbill.util.GBillLoggerUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public abstract class WebServiceOperation<T> implements Response.Listener, Response.ErrorListener {

    public static final String TAG = WebServiceOperation.class.getSimpleName();

    protected final int TIME_OUT_CONNECTION = 20000;//

    protected String mTag;
    protected Class mClazz;
    protected Type mClazzType;
    protected WebServiceRequest mWebServiceRequest;
    private Map<String, String> mHeader = new HashMap<>();

    //Constructors for POST
    protected WebServiceOperation(String uri, int method, String body, Type type, String tag) {
        this(uri, method, body, null, type, tag);
    }

    //Constructors for GET
    protected WebServiceOperation(String uri, int method, Type type, String tag) {
        this(uri, method, null, null, type, tag);
    }

    protected WebServiceOperation(String uri, int method, String body, Class clazz, Type type, String tag) {
        mTag = tag;
        mClazzType = type;
        setRequest(uri, method, body);
    }

    protected void setRequest(String uri, int method, String body) {
        /*mWebServiceRequest =
                new WebServiceRequest(Config.LOCAL ? UrlConstant.BASE_LOCAL_URL + uri
                        : UrlConstant.BASE_SERVER_URL + uri, method, mHeader, body, this, this);*/
        mWebServiceRequest = new WebServiceRequest(UrlConstant.BASE_DEV_URL + uri, method, mHeader, body, this, this);
        RetryPolicy policy = new DefaultRetryPolicy(TIME_OUT_CONNECTION, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        mWebServiceRequest.setRetryPolicy(policy);
    }

    /**
     * This method will check network connection before adding the request to queue for
     * network operation.
     */
    public void addToRequestQueue() {
        if (ConnectionUtil.isOnline(GstBillApplication.getInstance().getApplicationContext())){
            GBillLoggerUtil.debug(this, "Network Online");
            mWebServiceRequest.setTag(TextUtils.isEmpty(mTag) ? WebServiceOperation.TAG : mTag);
            GstBillApplication.getInstance().getRequestQueue().add(mWebServiceRequest);
        } else {
            onError(new GstBillException("Network is not available"));
            GBillLoggerUtil.debug(this, "Network offline");
        }
    }

    protected <T> T getFromAssetsFolder(String filename, Type type) {
        return getFromAssetsFolder(filename, null, type);
    }

    protected <T> T getFromAssetsFolder(String filename, Class<T> clazz) {
        return getFromAssetsFolder(filename, clazz, null);
    }

    private <T> T getFromAssetsFolder(String filename, Class<T> clazz, Type type) {
        AssetManager manager = GstBillApplication.getInstance().getAssets();
        BufferedReader reader = null;
        StringBuilder response = new StringBuilder();
        T object = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(manager.open(filename)));

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                //process line
                response.append(mLine);
            }
            if (type != null) {
                object = new Gson().fromJson(response.toString(), type);
            } else {
                object = new Gson().fromJson(response.toString(), clazz);
            }
        } catch (IOException | JsonSyntaxException e) {
            //log the exception
            GBillLoggerUtil.debug(this, e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        return object;
    }

    /**
     * This method will clear the request from queue based on tag.
     */
    public void removeFromRequestQueue() {
        GstBillApplication.getInstance().getRequestQueue().cancelAll(TextUtils.isEmpty(mTag) ? WebServiceOperation.TAG : mTag);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        GBillLoggerUtil.debug(this, "Network error:=" + error != null ? "error" : error.getMessage());
        onError(new GstBillException(error.networkResponse != null ? error.networkResponse.statusCode : 401));
    }

    /**
     * Abstract method for handling network or parsing error
     */
    public abstract void onError(GstBillException exception);

    /**
     * This method will parse the json data based on class
     */
    @Override
    public void onResponse(Object response) {
        T object = null;

        try {
            if (mClazzType != null) {
                object = new Gson().fromJson(((String) response), mClazzType);
            }
            if (object != null) {
                int statusCode = HttpURLConnection.HTTP_OK;
                String statusMessage = "";
                if (object instanceof BaseResponse) {
                    BaseResponse baseResponse = (BaseResponse) object;
                    statusCode = baseResponse.statusCode;
                    statusMessage = baseResponse.message;
                }
                if (statusCode == HttpURLConnection.HTTP_OK || statusCode == HttpURLConnection.HTTP_CREATED) {
                    onSuccess(object);
                } else {
                    onError(new GstBillException(statusCode, statusMessage));
                    new GBillLoggerUtil().error("ss","ee1");
                }
            } else {
                onError(new GstBillException(401));
            }
        } catch (JsonSyntaxException e) {
            GBillLoggerUtil.debug(this, "Network error:=" + e.getMessage());
            GBillLoggerUtil.error("ss","eew");
            onError(new GstBillException(401));
        }
    }

    /**
     * Abstract method for handling success response
     */
    public abstract void onSuccess(T response);
}
