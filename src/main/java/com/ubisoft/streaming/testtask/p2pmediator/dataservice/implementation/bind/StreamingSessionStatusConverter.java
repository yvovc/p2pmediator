package com.ubisoft.streaming.testtask.p2pmediator.dataservice.implementation.bind;

import com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session.StreamingSessionStatus;
import org.jooq.impl.AbstractConverter;

public class StreamingSessionStatusConverter extends AbstractConverter<Integer, StreamingSessionStatus> {
    public StreamingSessionStatusConverter() {
        super(Integer.class, StreamingSessionStatus.class);
    }

    @Override
    public StreamingSessionStatus from(Integer id) {
        return StreamingSessionStatus.fromId(id);
    }

    @Override
    public Integer to(StreamingSessionStatus userObject) {
        if (userObject != null) {
            return userObject.getId();
        }
        return null;
    }
}
