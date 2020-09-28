package com.ubisoft.streaming.testtask.p2pmediator.dto.view.request;

public class ViewRequestEndpoint {
    private Integer viewRequestId;
    private String host;
    private int port;


    public Integer getViewRequestId() {
        return viewRequestId;
    }

    public void setViewRequestId(Integer viewRequestId) {
        this.viewRequestId = viewRequestId;
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
