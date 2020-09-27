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

@RestController
@RequestMapping("/view-request")
public class ViewRequestController {

    private final ViewRequestService viewRequestService;

    @Autowired
    public ViewRequestController(final ViewRequestService viewRequestService) {
        this.viewRequestService = viewRequestService;
    }

    @PostMapping
    public void createViewRequest(@AuthenticationPrincipal Peer peer,
                                  @RequestParam Integer streamingSessionId) {
        final ViewRequest viewRequest = new ViewRequest()
                .setStreamingSessionId(streamingSessionId)
                .setViewerId(peer.getId());
        viewRequestService.createViewRequest(peer, viewRequest);
    }

    @PutMapping
    public void updateViewRequest(@AuthenticationPrincipal Peer peer,
                                  @RequestBody ViewRequest viewRequest) {
        viewRequestService.updateViewRequest(peer, viewRequest);
    }

    @PostMapping("/{viewRequestId}/endpoints")
    public void addViewRequestEndpoints(@AuthenticationPrincipal Peer peer,
                                        @RequestBody List<ViewRequestEndpoint> viewRequestEndpoints) {
        viewRequestService.addViewRequestEndpoints(viewRequestEndpoints);
    }

    @GetMapping("/endpoint")
    public List<ViewRequestEndpoint> getViewRequestsEndpoints(@AuthenticationPrincipal final Peer peer,
                                                              @RequestParam final List<ViewRequestStatus> viewRequestStatuses) {
        return viewRequestService.getViewRequestsEndpoints(peer, viewRequestStatuses);
    }

}
