package com.ubisoft.streaming.testtask.p2pmediator.dataservice;

import com.ubisoft.streaming.testtask.p2pmediator.auth.Peer;
import com.ubisoft.streaming.testtask.p2pmediator.dto.VideoGame;
import com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session.StreamingSession;
import com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session.StreamingSessionStatus;

import java.util.List;

public interface IStreamingSessionDataService {
    void createStreamingSession(final Peer peer,
                                final VideoGame videoGameToStream);

    List<StreamingSession> getStreamingSessions(final List<StreamingSessionStatus> statuses);
}
