package com.ubisoft.streaming.testtask.p2pmediator.dataservice;

import com.ubisoft.streaming.testtask.p2pmediator.auth.Peer;
import com.ubisoft.streaming.testtask.p2pmediator.dto.Game;

public interface IStreamingSessionDataService {
    void createStreamingSession(final Peer peer,
                                final Game gameToStream);
}
