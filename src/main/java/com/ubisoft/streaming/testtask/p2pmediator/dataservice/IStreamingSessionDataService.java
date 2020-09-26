package com.ubisoft.streaming.testtask.p2pmediator.dataservice;

import com.ubisoft.streaming.testtask.p2pmediator.auth.Peer;
import com.ubisoft.streaming.testtask.p2pmediator.dto.Game;
import com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session.StreamingSession;
import com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session.StreamingSessionStatus;

import java.util.List;

public interface IStreamingSessionDataService {
    void createStreamingSession(final Peer peer,
                                final Game gameToStream);

    List<StreamingSession> getStreamingSessions(final List<StreamingSessionStatus> statuses);
}
