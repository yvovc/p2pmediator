package com.ubisoft.streaming.testtask.p2pmediator.dataservice.factory;

import com.ubisoft.streaming.testtask.p2pmediator.dataservice.IStreamingSessionDataService;
import com.ubisoft.streaming.testtask.p2pmediator.dataservice.implementation.StreamingSessionDataService;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class StreamingSessionDataServiceFactory {
    private static Map<String, IStreamingSessionDataService> STREAMING_SESSION_TYPE_DATA_SERVICE_MAP;

    private final DSLContext dslContext;

    @Autowired
    public StreamingSessionDataServiceFactory(final DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public IStreamingSessionDataService getStreamingSessionDataService(final String dataServiceType) {
        return STREAMING_SESSION_TYPE_DATA_SERVICE_MAP.getOrDefault(
                dataServiceType, new StreamingSessionDataService(dslContext));
    }
}
