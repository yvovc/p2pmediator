package com.ubisoft.streaming.testtask.p2pmediator.dataservice;

import com.ubisoft.streaming.testtask.p2pmediator.auth.Peer;
import com.ubisoft.streaming.testtask.p2pmediator.dto.VideoGame;
import com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session.StreamingSession;
import com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session.StreamingSessionStatus;
import com.ubisoft.streaming.testtask.p2pmediator.dto.view.request.ViewRequest;

import java.util.List;

public interface IViewRequestDataService {
    void createViewRequest(final Peer peer,
                           final ViewRequest viewRequest);

    List<StreamingSession> updateViewRequest(final List<StreamingSessionStatus> statuses);
}
