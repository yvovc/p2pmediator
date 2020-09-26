package com.ubisoft.streaming.testtask.p2pmediator.config;

import com.ubisoft.streaming.testtask.p2pmediator.dataservice.IStreamingSessionDataService;
import com.ubisoft.streaming.testtask.p2pmediator.dataservice.factory.StreamingSessionDataServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataServicesConfiguration {
    @Value("${services.streaming-session.data-service.type}")
    private String streamingSessionDataServiceType;

    private final StreamingSessionDataServiceFactory streamingSessionDataServiceFactory;

    @Autowired
    public DataServicesConfiguration(final StreamingSessionDataServiceFactory streamingSessionDataServiceFactory) {
        this.streamingSessionDataServiceFactory = streamingSessionDataServiceFactory;
    }

    @Bean
    public IStreamingSessionDataService streamingSessionDataService() {
        return streamingSessionDataServiceFactory.getStreamingSessionDataService(streamingSessionDataServiceType);
    }
}
