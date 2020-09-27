package com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum StreamingSessionRole {
    STREAMER(1, "streamer"),
    VIEWER(2, "viewer");

    int id;
    String value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    StreamingSessionRole(int id, String value) {
        this.id = id;
        this.value = value;
    }

    final static Map<Integer, StreamingSessionRole> STREAMING_SESSION_ROLE_MAP =
            Collections.unmodifiableMap(
                    Stream.of(values()).collect(Collectors.toMap(entry -> entry.id, entry -> entry))
            );

    public static StreamingSessionRole fromId(final Integer id) {
        return STREAMING_SESSION_ROLE_MAP.get(id);
    }
}
