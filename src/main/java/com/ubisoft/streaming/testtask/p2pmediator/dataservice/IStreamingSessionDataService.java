package com.ubisoft.streaming.testtask.p2pmediator.dataservice;

import com.ubisoft.streaming.testtask.p2pmediator.auth.Peer;
import com.ubisoft.streaming.testtask.p2pmediator.dto.VideoGame;
import com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session.StreamingSession;
import com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session.StreamingSessionEndpoint;
import com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session.StreamingSessionRole;
import com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session.StreamingSessionStatus;

import java.util.List;
import java.util.UUID;

public interface IStreamingSessionDataService {
    StreamingSession createStreamingSession(final Peer peer,
                                            final VideoGame videoGameToStream);

    List<StreamingSession> getStreamingSessions(final List<StreamingSessionStatus> statuses);

    StreamingSessionRole getPeerRole(final UUID peerId,
                                     final Integer streamingSessionId);

    boolean exists(final Integer streamingSessionId);

    StreamingSession getStreamingSession(final Integer streamingSessionId);

    StreamingSessionStatus updateStreamingSessionStatus(final Integer streamingSessionId,
                                                        final StreamingSessionStatus newStatus);

    List<StreamingSessionEndpoint> getStreamingSessionEndpoints(final Integer streamingSessionId);

    List<StreamingSessionEndpoint> addStreamingSessionEndpoints(final Integer streamingSessionId,
                                                                final List<StreamingSessionEndpoint> streamSessionEndpoints);

    StreamingSessionRole indexPeerStreamingSessionRole(final Peer peer,
                                                       final Integer streamingSessionId,
                                                       final StreamingSessionRole role);
}
