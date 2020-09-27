package com.ubisoft.streaming.testtask.p2pmediator.dto.view.request;

import java.util.UUID;

public class ViewRequest {
    private UUID viewerId;
    private Integer streamingSessionId;

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
}
