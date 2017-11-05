package com.example.ceh.cryptoconverter.utils;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by CEH on 10/31/2017.
 */

public class SingletonRequest extends Application {
    //Getting tag it will be used for keeping track of request
    public static final String TAG= "hhh";

    //Creating class object
    private static SingletonRequest mInstance;

    private RequestQueue mRequestQueue;


    //class instance will be initialized on app launch
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    //Public static method to get the instance of this class
    public static synchronized SingletonRequest getInstance() {
        return mInstance;
    }

    //This method would return the request queue
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }


    //This method would add the request to the queue for execution
    public <T> void addToRequestQueue(Request<T> req) {
        //Setting a tag to the request
        req.setTag(TAG);

        //calling this method to get the request queue and adding the request the the queue
        getRequestQueue().add(req);
    }

    //method to cancel the pending requests
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
