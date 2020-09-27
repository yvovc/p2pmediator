package com.ubisoft.streaming.testtask.p2pmediator.dataservice.implementation.bind;

import com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session.StreamingSessionRole;
import com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session.StreamingSessionStatus;
import org.jooq.impl.AbstractConverter;

public class StreamingSessionPeerRoleConverter extends AbstractConverter<Integer, StreamingSessionRole> {
    public StreamingSessionPeerRoleConverter() {
        super(Integer.class, StreamingSessionRole.class);
    }

    @Override
    public StreamingSessionRole from(Integer id) {
        return StreamingSessionRole.fromId(id);
    }

    @Override
    public Integer to(StreamingSessionRole userObject) {
        if (userObject != null) {
            return userObject.getId();
        }
        return null;
    }
}
