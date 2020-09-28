package com.ubisoft.streaming.testtask.p2pmediator.dto.view.request;

import java.util.UUID;

public class ViewRequest {
    public final static String VIEW_REQUEST_STATUS_FIELD_NAME = "viewRequestStatus";

    private Integer id;
    private UUID viewerId;
    private Integer streamingSessionId;
    private ViewRequestStatus viewRequestStatus;

    public Integer getId() {
        return id;
    }

    public ViewRequest setId(Integer id) {
        this.id = id;
        return this;
    }

    public UUID getViewerId() {
        return viewerId;
    }

    public ViewRequest setViewerId(UUID viewerId) {
        this.viewerId = viewerId;
        return this;
    }

    public Integer getStreamingSessionId() {
        return streamingSessionId;
    }

    public ViewRequest setStreamingSessionId(Integer streamingSessionId) {
        this.streamingSessionId = streamingSessionId;
        return this;
    }

    public ViewRequestStatus getViewRequestStatus() {
        return viewRequestStatus;
    }

    public ViewRequest setViewRequestStatus(ViewRequestStatus viewRequestStatus) {
        this.viewRequestStatus = viewRequestStatus;
        return this;
    }
}
