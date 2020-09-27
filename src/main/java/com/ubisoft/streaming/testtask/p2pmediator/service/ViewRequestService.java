package com.ubisoft.streaming.testtask.p2pmediator.service;

import com.ubisoft.streaming.testtask.p2pmediator.auth.Peer;
import com.ubisoft.streaming.testtask.p2pmediator.dataservice.IViewRequestDataService;
import com.ubisoft.streaming.testtask.p2pmediator.dto.view.request.ViewRequest;
import com.ubisoft.streaming.testtask.p2pmediator.dto.view.request.ViewRequestEndpoint;
import com.ubisoft.streaming.testtask.p2pmediator.dto.view.request.ViewRequestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViewRequestService {

    private final IViewRequestDataService viewRequestDataService;

    @Autowired
    public ViewRequestService(final IViewRequestDataService viewRequestDataService) {
        this.viewRequestDataService = viewRequestDataService;
    }

    public void createViewRequest(final Peer peer,
                                  final ViewRequest viewRequest) {
    }

    public void updateViewRequest(final Peer peer, ViewRequest viewRequest) {
    }

    public void addViewRequestEndpoints(List<ViewRequestEndpoint> viewRequestEndpoints) {
    }

    public List<ViewRequestEndpoint> getViewRequestsEndpoints(final Peer peer,
                                                              final List<ViewRequestStatus> viewRequestStatuses) {

    }
}
