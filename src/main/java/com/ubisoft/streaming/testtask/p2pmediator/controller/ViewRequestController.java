package com.ubisoft.streaming.testtask.p2pmediator.controller;

import com.ubisoft.streaming.testtask.p2pmediator.auth.Peer;
import com.ubisoft.streaming.testtask.p2pmediator.dto.view.request.ViewRequest;
import com.ubisoft.streaming.testtask.p2pmediator.dto.view.request.ViewRequestEndpoint;
import com.ubisoft.streaming.testtask.p2pmediator.dto.view.request.ViewRequestStatus;
import com.ubisoft.streaming.testtask.p2pmediator.service.ViewRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller exposing view requests related API to peers.
 *
 * @author yvovc
 * @since 2020/27/09
 */
@RestController
@RequestMapping("/view-request")
public class ViewRequestController {

    /**
     * View requests service layer class.
     */
    private final ViewRequestService viewRequestService;

    @Autowired
    public ViewRequestController(final ViewRequestService viewRequestService) {
        this.viewRequestService = viewRequestService;
    }

    /**
     * Creates view requests related to the specified by ID streaming session.
     *
     * @param peer               active peer
     * @param streamingSessionId streaming session ID
     * @return created {@link ViewRequest}
     */
    @PostMapping
    public ViewRequest createViewRequest(@AuthenticationPrincipal Peer peer,
                                         @RequestParam Integer streamingSessionId) {
        final ViewRequest viewRequest = new ViewRequest()
                .setStreamingSessionId(streamingSessionId)
                .setViewerId(peer.getId());
        return viewRequestService.createViewRequest(peer, viewRequest);
    }

    /**
     * Updates view request's status.
     *
     * @param peer              active peer
     * @param viewRequestStatus new view request status
     * @return new {@link ViewRequestStatus}
     */
    @PutMapping("/{viewRequestId}/status")
    public ViewRequestStatus updateViewRequestStatus(@AuthenticationPrincipal Peer peer,
                                                     @PathVariable final Integer viewRequestId,
                                                     @RequestParam ViewRequestStatus viewRequestStatus) {
        return viewRequestService.updateViewRequestStatus(peer, viewRequestId, viewRequestStatus);
    }

    /**
     * Adds viewer endpoints to the specified view request.
     *
     * @param peer                 active peer
     * @param viewRequestId        view request ID
     * @param viewRequestEndpoints endpoints to use to process view request
     * @return created {@link ViewRequestEndpoint}s
     */
    @PostMapping("/{viewRequestId}/endpoint")
    public List<ViewRequestEndpoint> addViewRequestEndpoints(@AuthenticationPrincipal Peer peer,
                                                             @PathVariable final Integer viewRequestId,
                                                             @RequestBody List<ViewRequestEndpoint> viewRequestEndpoints) {
        return viewRequestService.addViewRequestEndpoints(peer, viewRequestId, viewRequestEndpoints);
    }

    /**
     * Gets all the endpoints of the view requests that are in the specified status.
     *
     * @param peer                active peer
     * @param viewRequestStatuses view request statuses to fetch endpoints of
     * @return fetched {@link ViewRequestEndpoint}s
     */
    @GetMapping("/endpoint")
    public List<ViewRequestEndpoint> getViewRequestsEndpoints(@AuthenticationPrincipal final Peer peer,
                                                              @RequestParam final Integer streamingSessionId,
                                                              @RequestParam final List<ViewRequestStatus> viewRequestStatuses) {
        return viewRequestService.getViewRequestsEndpoints(peer, streamingSessionId, viewRequestStatuses);
    }

}
