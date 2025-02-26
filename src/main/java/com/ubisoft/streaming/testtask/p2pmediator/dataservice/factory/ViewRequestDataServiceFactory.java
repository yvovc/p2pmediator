package com.ubisoft.streaming.testtask.p2pmediator.dataservice.factory;

import com.google.common.collect.ImmutableMap;
import com.ubisoft.streaming.testtask.p2pmediator.dataservice.IViewRequestDataService;
import com.ubisoft.streaming.testtask.p2pmediator.dataservice.implementation.JooqViewRequestDataService;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Factory component that produces corresponded {@link IViewRequestDataService} implementation according
 * to some value provided by env config.
 *
 * @author yvovc
 * @since 2020/26/09
 */
@Component
public class ViewRequestDataServiceFactory {
    private static Map<String, IViewRequestDataService> VIEW_REQUEST_TYPE_DATA_SERVICE_MAP;

    private final DSLContext dslContext;

    @PostConstruct
    public void init() {
        VIEW_REQUEST_TYPE_DATA_SERVICE_MAP = ImmutableMap.<String, IViewRequestDataService>builder()
                .put(MediatorDataSourceType.JOOQ_DSL.getValue(), new JooqViewRequestDataService(dslContext))
                .build();
    }

    @Autowired
    public ViewRequestDataServiceFactory(final DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public IViewRequestDataService getViewRequestDataService(final String dataServiceType) {
        return VIEW_REQUEST_TYPE_DATA_SERVICE_MAP.getOrDefault(
                dataServiceType, new JooqViewRequestDataService(dslContext));
    }
}
