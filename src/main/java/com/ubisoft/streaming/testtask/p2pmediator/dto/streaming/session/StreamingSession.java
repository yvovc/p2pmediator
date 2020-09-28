package com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session;

import java.util.UUID;

public class StreamingSession {
    public final static String VIDEO_GAME_NAME_FIELD_NAME = "videoGameName";
    public final static String STREAMING_SESSION_STATUS_FIELD_NAME = "streamingSessionStatus";

    private Integer id;
    private UUID hostId;
    private String videoGameName;
    private StreamingSessionStatus streamingSessionStatus;

    public Integer getId() {
        return id;
    }

    public StreamingSession setId(Integer id) {
        this.id = id;
        return this;
    }

    public UUID getHostId() {
        return hostId;
    }

    public StreamingSession setHostId(UUID hostId) {
        this.hostId = hostId;
        return this;
    }

    public String getVideoGameName() {
        return videoGameName;
    }

    public StreamingSession setVideoGameName(String videoGameName) {
        this.videoGameName = videoGameName;
        return this;
    }

    public StreamingSessionStatus getStreamingSessionStatus() {
        return streamingSessionStatus;
    }

    public StreamingSession setStreamingSessionStatus(StreamingSessionStatus streamingSessionStatus) {
        this.streamingSessionStatus = streamingSessionStatus;
        return this;
    }
}
