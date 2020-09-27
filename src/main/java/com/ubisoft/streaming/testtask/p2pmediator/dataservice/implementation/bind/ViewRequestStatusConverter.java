package com.ubisoft.streaming.testtask.p2pmediator.dataservice.implementation.bind;

import com.ubisoft.streaming.testtask.p2pmediator.dto.view.request.ViewRequestStatus;
import org.jooq.impl.AbstractConverter;

public class ViewRequestStatusConverter extends AbstractConverter<Integer, ViewRequestStatus> {
    public ViewRequestStatusConverter() {
        super(Integer.class, ViewRequestStatus.class);
    }

    @Override
    public ViewRequestStatus from(Integer id) {
        return ViewRequestStatus.fromId(id);
    }

    @Override
    public Integer to(ViewRequestStatus userObject) {
        if (userObject != null) {
            return userObject.getId();
        }
        return null;
    }
}
