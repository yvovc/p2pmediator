package com.ubisoft.streaming.testtask.p2pmediator.service;

import com.google.common.collect.ImmutableMap;
import com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session.StreamingSession;
import com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session.StreamingSessionRole;
import com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session.StreamingSessionStatus;
import com.ubisoft.streaming.testtask.p2pmediator.error.exception.MediatorServiceException;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class StreamingSessionFieldAccessValidator {
    /**
     * Map that shows which fields are eligible to be updated by some Role and to which values update is possible.
     * E.g: Streamer can update streaming session status field to FINISHED state.
     */
    final Map<String, List<Pair<StreamingSessionRole, List<Object>>>> STREAMING_SESSION_UPDATE_PERMISSIONS_BY_ROLE =
            ImmutableMap.<String, List<Pair<StreamingSessionRole, List<Object>>>>builder()
                    .put(
                            StreamingSession.STREAMING_SESSION_STATUS_FIELD_NAME,
                            Collections.singletonList(Pair.of(StreamingSessionRole.STREAMER,
                                    Collections.singletonList(StreamingSessionStatus.FINISHED)))
                    ).build();

    public void validate(final StreamingSessionRole role,
                         final String fieldName,
                         final Object newValue) {
        final boolean permitted = STREAMING_SESSION_UPDATE_PERMISSIONS_BY_ROLE.get(fieldName).stream()
                .anyMatch(pair -> pair.getKey().equals(role) && pair.getValue().contains(newValue));
        if (!permitted) {
            throw new MediatorServiceException(
                    String.format("Role %s can't update field '%s' to '%s' value", role, fieldName,
                            newValue));
        }
    }
}
