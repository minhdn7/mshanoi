package com.mkit.mshanoi.domain.model.pojo.response;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by MinhDN on 2/2/2018.
 */

public class CommonResponse {
    @Getter @Setter
    @SerializedName("responseCode")
    private int responseCode;

    @Getter @Setter
    @SerializedName("responseMessage")
    private String responseMessage;
}
