package com.mkit.mshanoi.domain.model.service;

/**
 * Created by MinhDN on 28/12/2017.
 */

public interface IFinishedListener {
    void onSuccess(Object object);
    void onError(Object object);
}
