package com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session;

import java.util.UUID;

public class StreamingSession {
    public final static String VIDEO_GAME_NAME_FIELD_NAME = "videoGameName";

    private Integer id;
    private UUID hostId;
    private String videoGameName;
    private StreamingSessionStatus streamingSessionStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UUID getHostId() {
        return hostId;
    }

    public void setHostId(UUID hostId) {
        this.hostId = hostId;
    }

    public String getVideoGameName() {
        return videoGameName;
    }

    public void setVideoGameName(String videoGameName) {
        this.videoGameName = videoGameName;
    }

    public StreamingSessionStatus getStreamingSessionStatus() {
        return streamingSessionStatus;
    }

    public void setStreamingSessionStatus(StreamingSessionStatus streamingSessionStatus) {
        this.streamingSessionStatus = streamingSessionStatus;
    }
}
