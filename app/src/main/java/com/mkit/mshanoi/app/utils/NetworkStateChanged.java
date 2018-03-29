package com.mkit.mshanoi.app.utils;

/**
 * Created by MinhDN on 28/3/2018.
 */

public class NetworkStateChanged {
    private boolean mIsInternetConnected;

    public NetworkStateChanged(boolean isInternetConnected) {
        mIsInternetConnected = isInternetConnected;
    }

    public boolean isInternetConnected() {
        return mIsInternetConnected;
    }
}
