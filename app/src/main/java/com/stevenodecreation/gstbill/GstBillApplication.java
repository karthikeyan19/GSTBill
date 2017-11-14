package com.stevenodecreation.gstbill;


import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.stevenodecreation.gstbill.util.LRUBitmapCache;

public class GstBillApplication extends Application{

    private static GstBillApplication mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private LRUBitmapCache mLruBitmapCache;

    //onCreate will call only one time
    @Override

    public void onCreate() {
        super.onCreate();
        mInstance = this;
//        new DBManager(this).connect();
    }

    public static synchronized GstBillApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }
    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            getLruBitmapCache();
            mImageLoader = new ImageLoader(this.mRequestQueue, mLruBitmapCache);
        }
        return this.mImageLoader;
    }

    public LRUBitmapCache getLruBitmapCache() {
        if (mLruBitmapCache == null)
            mLruBitmapCache = new LRUBitmapCache();
        return this.mLruBitmapCache;
    }
}
