package com.ubisoft.streaming.testtask.p2pmediator.service;

import com.google.common.collect.ImmutableList;
import com.ubisoft.streaming.testtask.p2pmediator.auth.Peer;
import com.ubisoft.streaming.testtask.p2pmediator.dataservice.IStreamingSessionDataService;
import com.ubisoft.streaming.testtask.p2pmediator.dataservice.IViewRequestDataService;
import com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session.StreamingSession;
import com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session.StreamingSessionRole;
import com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session.StreamingSessionStatus;
import com.ubisoft.streaming.testtask.p2pmediator.dto.view.request.ViewRequest;
import com.ubisoft.streaming.testtask.p2pmediator.dto.view.request.ViewRequestEndpoint;
import com.ubisoft.streaming.testtask.p2pmediator.dto.view.request.ViewRequestStatus;
import com.ubisoft.streaming.testtask.p2pmediator.error.exception.MediatorServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer class performing operations related to view requests.
 *
 * @author yvovc
 * @since 2020/27/09
 */
@Service
public class ViewRequestService {
    private static final List<StreamingSessionStatus> ILLEGAL_TO_VIEW_STREAMING_SESSION =
            ImmutableList.of(StreamingSessionStatus.NEW, StreamingSessionStatus.FINISHED);

    /**
     * DAL component performing operations related to view requests.
     */
    private final IViewRequestDataService viewRequestDataService;
    private final IStreamingSessionDataService streamingSessionDataService;
    private final ViewRequestFieldAccessValidator viewRequestFieldAccessValidator;

    @Autowired
    public ViewRequestService(final IViewRequestDataService viewRequestDataService,
                              final IStreamingSessionDataService streamingSessionDataService, ViewRequestFieldAccessValidator viewRequestFiledAccessValidator) {
        this.viewRequestDataService = viewRequestDataService;
        this.streamingSessionDataService = streamingSessionDataService;
        this.viewRequestFieldAccessValidator = viewRequestFiledAccessValidator;
    }

    /**
     * Creates view requests related to the specified by ID streaming session.
     *
     * @param peer active peer
     * @return created {@link ViewRequest}
     */
    public ViewRequest createViewRequest(final Peer peer,
                                         final ViewRequest viewRequest) { //TODO:Validate IP addr
        viewRequest.setViewRequestStatus(ViewRequestStatus.NEW);
        validateViewRequestCreation(peer, viewRequest);
        streamingSessionDataService.indexPeerStreamingSessionRole(peer,
                viewRequest.getStreamingSessionId(), StreamingSessionRole.VIEWER);
        return viewRequestDataService.createViewRequest(viewRequest);
    }

    /**
     * Updates view request's status.
     *
     * @param peer              active peer
     * @param viewRequestStatus new view request status
     * @return new {@link ViewRequestStatus}
     */
    public ViewRequestStatus updateViewRequestStatus(final Peer peer,
                                                     final Integer viewRequestId,
                                                     final ViewRequestStatus viewRequestStatus) {
        validateViewRequestStatusUpdate(peer, viewRequestId, viewRequestStatus);
        return viewRequestDataService.updateViewRequestStatus(viewRequestId, viewRequestStatus);
    }

    /**
     * Adds viewer endpoints to the specified view request.
     *
     * @param peer                 active peer
     * @param viewRequestId        view request ID
     * @param viewRequestEndpoints endpoints to use to process view request
     * @return created {@link ViewRequestEndpoint}s
     */
    public List<ViewRequestEndpoint> addViewRequestEndpoints(final Peer peer,
                                                             final Integer viewRequestId,
                                                             final List<ViewRequestEndpoint> viewRequestEndpoints) {
        validateViewRequestEndpointsCreation(peer, viewRequestId);
        return viewRequestDataService.addViewRequestEndpoints(viewRequestId, viewRequestEndpoints);
    }

    /**
     * Gets all the endpoints of the view requests that are in the specified status.
     *
     * @param peer                active peer
     * @param viewRequestStatuses view request statuses to fetch endpoints of
     * @return fetched {@link ViewRequestEndpoint}s
     */
    public List<ViewRequestEndpoint> getViewRequestsEndpoints(final Peer peer,
                                                              final Integer streamingSessionId,
                                                              final List<ViewRequestStatus> viewRequestStatuses) {
        return viewRequestDataService.getViewRequestsEndpoints(streamingSessionId, viewRequestStatuses);
    }

    private void validateViewRequestStatusUpdate(final Peer peer,
                                                 final Integer viewRequestId,
                                                 final ViewRequestStatus viewRequestStatus) {
        final ViewRequest viewRequest = getViewRequestIfExists(viewRequestId);
        final StreamingSessionRole role =
                streamingSessionDataService.getPeerRole(peer.getId(), viewRequest.getStreamingSessionId());
        viewRequestFieldAccessValidator.validate(
                viewRequestId,
                role,
                ViewRequest.VIEW_REQUEST_STATUS_FIELD_NAME,
                viewRequestStatus);
    }

    private void validateViewRequestCreation(final Peer peer,
                                             final ViewRequest viewRequest) {
        //validate streaming session exists and in correct status
        final StreamingSession streamingSession =
                streamingSessionDataService.getStreamingSession(viewRequest.getStreamingSessionId());
        if (streamingSession == null) {
            throw new MediatorServiceException("Requested streaming session doesn't exist");
        }
        if (!streamingSessionStatusEligibleToBeViewed(streamingSession.getStreamingSessionStatus())) {
            throw new MediatorServiceException("Requested streaming session can't be viewed because of it's invalid status");
        }
    }

    private static boolean streamingSessionStatusEligibleToBeViewed(final StreamingSessionStatus streamingSessionStatus) {
        return !ILLEGAL_TO_VIEW_STREAMING_SESSION.contains(streamingSessionStatus);
    }

    private void validateViewRequestEndpointsCreation(final Peer peer,
                                                      final Integer viewRequestId) {
        final ViewRequest viewRequest = getViewRequestIfExists(viewRequestId);
        if (streamingSessionDataService.getPeerRole(peer.getId(), viewRequestId) != StreamingSessionRole.VIEWER) {
            throw new MediatorServiceException("Only view request creator can add viewer endpoints");
        }
        if (viewRequest.getViewRequestStatus() == ViewRequestStatus.PROCESSED) {
            throw new MediatorServiceException("Can't add view request endpoints as it's already processed");
        }
    }

    private ViewRequest getViewRequestIfExists(final Integer viewRequestId) {
        final ViewRequest viewRequest = viewRequestDataService.getViewRequest(viewRequestId);
        if (viewRequest == null) {
            throw new MediatorServiceException("Requested view request doesn't exist");
        }
        return viewRequest;
    }
}
