package com.ubisoft.streaming.testtask.p2pmediator.config;

import com.ubisoft.streaming.testtask.p2pmediator.dataservice.IStreamingSessionDataService;
import com.ubisoft.streaming.testtask.p2pmediator.dataservice.IViewRequestDataService;
import com.ubisoft.streaming.testtask.p2pmediator.dataservice.factory.StreamingSessionDataServiceFactory;
import com.ubisoft.streaming.testtask.p2pmediator.dataservice.factory.ViewRequestDataServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataServicesConfiguration {
    @Value("${services.streaming-session.data-service.type}")
    private String streamingSessionDataServiceType;

    @Value("${services.view-request.data-service.type}")
    private String viewRequestDataServiceType;

    private final StreamingSessionDataServiceFactory streamingSessionDataServiceFactory;
    private final ViewRequestDataServiceFactory viewRequestDataServiceFactory;

    @Autowired
    public DataServicesConfiguration(final StreamingSessionDataServiceFactory streamingSessionDataServiceFactory, ViewRequestDataServiceFactory viewRequestDataServiceFactory) {
        this.streamingSessionDataServiceFactory = streamingSessionDataServiceFactory;
        this.viewRequestDataServiceFactory = viewRequestDataServiceFactory;
    }

    @Bean
    public IStreamingSessionDataService streamingSessionDataService() {
        return streamingSessionDataServiceFactory.getStreamingSessionDataService(streamingSessionDataServiceType);
    }

    @Bean
    public IViewRequestDataService viewRequestDataService() {
        return viewRequestDataServiceFactory.getViewRequestDataService(viewRequestDataServiceType);
    }
}
