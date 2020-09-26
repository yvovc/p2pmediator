package com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum StreamingSessionStatus {
    NEW(1, "new"),
    ACTIVE(2, "active"),
    FINISHED(3, "finished");

    Integer id;
    String value;

    StreamingSessionStatus(final Integer id,
                           final String value) {
        this.id = id;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    final static Map<Integer, StreamingSessionStatus> STREAMING_SESSION_STATUS_MAP =
            Collections.unmodifiableMap(
                    Stream.of(values()).collect(Collectors.toMap(entry -> entry.id, entry -> entry))
            );

    public static StreamingSessionStatus fromId(final Integer id) {
        return STREAMING_SESSION_STATUS_MAP.get(id);
    }
}
