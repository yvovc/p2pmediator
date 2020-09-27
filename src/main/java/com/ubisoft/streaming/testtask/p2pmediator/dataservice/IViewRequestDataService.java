package com.ubisoft.streaming.testtask.p2pmediator.dataservice;

import com.ubisoft.streaming.testtask.p2pmediator.dto.view.request.ViewRequest;
import com.ubisoft.streaming.testtask.p2pmediator.dto.view.request.ViewRequestEndpoint;
import com.ubisoft.streaming.testtask.p2pmediator.dto.view.request.ViewRequestStatus;

import java.util.List;

public interface IViewRequestDataService {
    ViewRequest createViewRequest(final ViewRequest viewRequest);

    ViewRequestStatus updateViewRequestStatus(final Integer viewRequestId,
                                              final ViewRequestStatus newStatus);

    ViewRequest getViewRequest(final Integer viewRequestId);

    List<ViewRequestEndpoint> getViewRequestEndpoints(final Integer viewRequestId,
                                                      final List<ViewRequestStatus> viewRequestStatuses);

    List<ViewRequestEndpoint> getViewRequestsEndpoints(final Integer streamingSessionId,
                                                       final List<ViewRequestStatus> viewRequestStatuses);

    List<ViewRequestEndpoint> addViewRequestEndpoints(final Integer viewRequestId,
                                                      final List<ViewRequestEndpoint> viewRequestEndpoints);
}
