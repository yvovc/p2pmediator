package com.ubisoft.streaming.testtask.p2pmediator.dto.view.request;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ViewRequestStatus {
    NEW(1, "new"),
    READY(2, "ready"),
    PROCESSED(3, "processed");

    Integer id;
    String value;

    ViewRequestStatus(final Integer id,
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

    final static Map<Integer, ViewRequestStatus> VIEW_REQUEST_STATUS_MAP =
            Collections.unmodifiableMap(
                    Stream.of(values()).collect(Collectors.toMap(entry -> entry.id, entry -> entry))
            );

    public static ViewRequestStatus fromId(final Integer id) {
        return VIEW_REQUEST_STATUS_MAP.get(id);
    }
}

