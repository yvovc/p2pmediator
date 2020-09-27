package com.ubisoft.streaming.testtask.p2pmediator.dataservice.implementation;

import com.ubisoft.streaming.testtask.p2pmediator.dataservice.IViewRequestDataService;
import com.ubisoft.streaming.testtask.p2pmediator.dto.view.request.ViewRequest;
import com.ubisoft.streaming.testtask.p2pmediator.dto.view.request.ViewRequestEndpoint;
import com.ubisoft.streaming.testtask.p2pmediator.dto.view.request.ViewRequestStatus;
import com.ubisoft.streaming.testtask.p2pmediator.p2pmediatordb.tables.records.ViewRequestEndpointRecord;
import com.ubisoft.streaming.testtask.p2pmediator.p2pmediatordb.tables.records.ViewRequestRecord;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.jooq.UpdateQuery;

import java.util.List;
import java.util.stream.Collectors;

import static com.ubisoft.streaming.testtask.p2pmediator.p2pmediatordb.Tables.VIEW_REQUEST;
import static com.ubisoft.streaming.testtask.p2pmediator.p2pmediatordb.Tables.VIEW_REQUEST_ENDPOINT;

public class JooqViewRequestDataService implements IViewRequestDataService {
    private final DSLContext dslContext;

    public JooqViewRequestDataService(final DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public ViewRequest createViewRequest(ViewRequest viewRequest) {
        final Integer id = dslContext.insertInto(VIEW_REQUEST)
                .set(
                        new ViewRequestRecord()
                                .setStreamingSessionId(viewRequest.getStreamingSessionId())
                                .setViewerId(viewRequest.getViewerId())
                                .setViewRequestStatus(viewRequest.getViewRequestStatus())
                )
                .returning(VIEW_REQUEST.ID)
                .fetchOne().getId();
        return viewRequest.setId(id);
    }

    @Override
    public ViewRequestStatus updateViewRequestStatus(final Integer viewRequestId,
                                                     final ViewRequestStatus newStatus) {
        final UpdateQuery<?> query = dslContext.updateQuery(VIEW_REQUEST);
        query.addValue(VIEW_REQUEST.VIEW_REQUEST_STATUS, newStatus);
        query.addConditions(VIEW_REQUEST.ID.eq(viewRequestId));
        query.execute();
        return newStatus;
    }

    @Override
    public ViewRequest getViewRequest(final Integer viewRequestId) {
        return dslContext.selectFrom(VIEW_REQUEST)
                .where(VIEW_REQUEST.ID.eq(viewRequestId))
                .fetchOneInto(ViewRequest.class);
    }

    @Override
    public List<ViewRequestEndpoint> getViewRequestsEndpoints(final Integer streamingSessionId,
                                                              final List<ViewRequestStatus> viewRequestStatuses) {
        SelectQuery<?> query = getViewRequestSelectQuery();
        query.addConditions(VIEW_REQUEST.STREAMING_SESSION_ID.eq(streamingSessionId)
                .and(VIEW_REQUEST.VIEW_REQUEST_STATUS.in(viewRequestStatuses)));
        return query.fetchInto(ViewRequestEndpoint.class);
    }

    public List<ViewRequestEndpoint> getViewRequestEndpoints(final Integer viewRequestId,
                                                             final List<ViewRequestStatus> viewRequestStatuses) {
        SelectQuery<?> query = getViewRequestSelectQuery();
        query.addConditions(VIEW_REQUEST.ID.eq(viewRequestId), VIEW_REQUEST.VIEW_REQUEST_STATUS.in(viewRequestStatuses));
        return query.fetchInto(ViewRequestEndpoint.class);
    }

    @Override
    public List<ViewRequestEndpoint> addViewRequestEndpoints(final Integer viewRequestId,
                                                             final List<ViewRequestEndpoint> viewRequestEndpoints) {
        final List<ViewRequestEndpointRecord> recordsToInsert = viewRequestEndpoints.stream()
                .map(endpoint -> new ViewRequestEndpointRecord()
                        .setViewRequestId(viewRequestId)
                        .setHost(endpoint.getHost())
                        .setPort(endpoint.getPort()))
                .collect(Collectors.toList());
        dslContext.batchInsert(recordsToInsert).execute();
        return viewRequestEndpoints;
    }

    private SelectQuery<?> getViewRequestSelectQuery() {
        return dslContext.select(
                VIEW_REQUEST_ENDPOINT.VIEW_REQUEST_ID,
                VIEW_REQUEST_ENDPOINT.HOST,
                VIEW_REQUEST_ENDPOINT.PORT)
                .from(VIEW_REQUEST)
                .join(VIEW_REQUEST_ENDPOINT).on(VIEW_REQUEST.ID.eq(VIEW_REQUEST_ENDPOINT.VIEW_REQUEST_ID)).getQuery();
    }
}
