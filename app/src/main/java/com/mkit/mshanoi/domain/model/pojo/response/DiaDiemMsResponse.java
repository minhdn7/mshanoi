package com.mkit.mshanoi.domain.model.pojo.response;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by PC on 2/19/2018.
 */

public class DiaDiemMsResponse {
    @SerializedName("id")
    @Getter @Setter
    private String id;

    @SerializedName("name")
    @Getter @Setter
    private String name;

    @SerializedName("linkTruyen")
    @Getter @Setter
    private String linkTruyen;

    @SerializedName("linkAnh")
    @Getter @Setter
    private String linkAnh;

    @SerializedName("điểm")
    @Getter @Setter
    private String point;

    @SerializedName("long")
    @Getter @Setter
    private String longTitule;

    @SerializedName("lat")
    @Getter @Setter
    private String latTitule;

    @SerializedName("comment")
    @Getter @Setter
    private String comment;
}
