package com.mkit.mshanoi.ui.event;

import com.mkit.mshanoi.domain.model.pojo.response.DiaDiemMsResponse;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by MinhDN on 14/3/2018.
 */

public class DiaDiemMsEvent {
    @Getter @Setter
    private DiaDiemMsResponse diaDiemMsResponse;

    public DiaDiemMsEvent(DiaDiemMsResponse diaDiemMsResponse) {
        this.diaDiemMsResponse = diaDiemMsResponse;
    }
}
