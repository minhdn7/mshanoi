package com.mkit.mshanoi.domain.model.pojo.request;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by MinhDN on 28/12/2017.
 */

public class HeaderAPI {
    @SerializedName("key")
    @Getter @Setter
    private String key;

    @SerializedName("value")
    @Getter @Setter
    private String value;
}
