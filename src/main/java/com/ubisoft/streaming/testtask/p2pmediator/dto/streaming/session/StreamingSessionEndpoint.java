package com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session;

public class StreamingSessionEndpoint {
    private Integer streamingSessionId;
    private String host;
    private int port;

    public Integer getStreamingSessionId() {
        return streamingSessionId;
    }

    public void setStreamingSessionId(Integer streamingSessionId) {
        this.streamingSessionId = streamingSessionId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
