package com.ubisoft.streaming.testtask.p2pmediator.dto.view.request;

import com.fasterxml.jackson.annotation.JsonCreator;

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

    final static Map<Integer, ViewRequestStatus> ID_VIEW_REQUEST_STATUS_MAP =
            Collections.unmodifiableMap(
                    Stream.of(values()).collect(Collectors.toMap(entry -> entry.id, entry -> entry))
            );

    final static Map<String, ViewRequestStatus> NAME_VIEW_REQUEST_STATUS_MAP =
            Collections.unmodifiableMap(
                    Stream.of(values()).collect(Collectors.toMap(Enum::name, entry -> entry))
            );

    public static ViewRequestStatus fromId(final Integer id) {
        return ID_VIEW_REQUEST_STATUS_MAP.get(id);
    }

    @JsonCreator
    public static ViewRequestStatus fromText(String text) {
        return NAME_VIEW_REQUEST_STATUS_MAP.get(text);
    }

    @Override
    public String toString() {
        return this.name();
    }
}

