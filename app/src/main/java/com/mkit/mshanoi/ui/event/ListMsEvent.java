package com.mkit.mshanoi.ui.event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mkit.mshanoi.domain.model.pojo.response.DiaDiemMsResponse;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by PC on 3/8/2018.
 */

public class ListMsEvent {
    @Getter @Setter
    @Expose
    private List<DiaDiemMsResponse> listMs;

    public ListMsEvent(List<DiaDiemMsResponse> listMs) {
        this.listMs = listMs;
    }
}
