package com.mkit.mshanoi.domain.model.pojo.response;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by MinhDN on 28/12/2017.
 */
public class APIError {
    @SerializedName("status")
    @Getter @Setter
    private int status;

    @SerializedName("message")
    @Getter @Setter
    private String message;

    public APIError(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
