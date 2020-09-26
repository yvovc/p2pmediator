package com.ubisoft.streaming.testtask.p2pmediator.dataservice.factory;

import com.google.common.collect.ImmutableMap;
import com.ubisoft.streaming.testtask.p2pmediator.dataservice.IStreamingSessionDataService;
import com.ubisoft.streaming.testtask.p2pmediator.dataservice.implementation.JooqStreamingSessionDataService;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
public class StreamingSessionDataServiceFactory {
    private static Map<String, IStreamingSessionDataService> STREAMING_SESSION_TYPE_DATA_SERVICE_MAP;

    private final DSLContext dslContext;

    @PostConstruct
    public void init() {
        STREAMING_SESSION_TYPE_DATA_SERVICE_MAP = ImmutableMap.<String, IStreamingSessionDataService>builder()
                .put("JooqDSL", new JooqStreamingSessionDataService(dslContext)).build();
    }

    @Autowired
    public StreamingSessionDataServiceFactory(final DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public IStreamingSessionDataService getStreamingSessionDataService(final String dataServiceType) {
        return STREAMING_SESSION_TYPE_DATA_SERVICE_MAP.getOrDefault(
                dataServiceType, new JooqStreamingSessionDataService(dslContext));
    }
}
