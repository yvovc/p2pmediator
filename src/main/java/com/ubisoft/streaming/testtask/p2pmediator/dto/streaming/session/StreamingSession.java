package com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session;

import java.util.UUID;

public class StreamingSession {
    private Integer id;
    private UUID hostId;
    private Integer gameId;
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

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public StreamingSessionStatus getStreamingSessionStatus() {
        return streamingSessionStatus;
    }

    public void setStreamingSessionStatus(StreamingSessionStatus streamingSessionStatus) {
        this.streamingSessionStatus = streamingSessionStatus;
    }
}
