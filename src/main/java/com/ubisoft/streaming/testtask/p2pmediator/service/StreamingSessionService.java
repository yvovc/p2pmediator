package com.ubisoft.streaming.testtask.p2pmediator.service;

import com.ubisoft.streaming.testtask.p2pmediator.auth.Peer;
import com.ubisoft.streaming.testtask.p2pmediator.dataservice.IStreamingSessionDataService;
import com.ubisoft.streaming.testtask.p2pmediator.dto.VideoGame;
import com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session.StreamingSession;
import com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session.StreamingSessionEndpoint;
import com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session.StreamingSessionRole;
import com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session.StreamingSessionStatus;
import com.ubisoft.streaming.testtask.p2pmediator.error.exception.MediatorServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class StreamingSessionService {

    private final GameService gameService;
    private final IStreamingSessionDataService streamingSessionDataService;
    private final StreamingSessionFieldAccessValidator streamingSessionFieldAccessValidator;

    @Autowired
    public StreamingSessionService(final IStreamingSessionDataService streamingSessionDataService,
                                   final GameService gameService,
                                   final StreamingSessionFieldAccessValidator streamingSessionFieldAccessValidator) {
        this.gameService = gameService;
        this.streamingSessionDataService = streamingSessionDataService;
        this.streamingSessionFieldAccessValidator = streamingSessionFieldAccessValidator;
    }

    public StreamingSession createStreamingSession(final Peer peer,
                                                   final VideoGame videoGameToStream) {
        validateStreamingSessionCreation(peer, videoGameToStream);
        final StreamingSession streamingSession = streamingSessionDataService.createStreamingSession(peer, videoGameToStream);
        streamingSessionDataService.indexPeerStreamingSessionRole(peer, streamingSession.getId(), StreamingSessionRole.STREAMER);
        return streamingSession;
    }

    public List<StreamingSession> getStreamingSessions(final Peer peer,
                                                       final List<StreamingSessionStatus> statuses) {
        return streamingSessionDataService.getStreamingSessions(statuses);
    }

    public StreamingSessionStatus updateStreamingSessionStatus(final Peer peer,
                                                               final Integer streamingSessionId,
                                                               final StreamingSessionStatus newStatus) {
        final StreamingSession streamingSessionToUpdate = streamingSessionDataService.getStreamingSession(streamingSessionId);
        validateStreamingSessionStatusUpdate(peer, streamingSessionToUpdate, newStatus);
        return streamingSessionDataService.updateStreamingSessionStatus(streamingSessionId, newStatus);
    }

    public List<StreamingSessionEndpoint> addStreamingSessionEndpoints(final Peer peer,
                                                                       final Integer streamingSessionId,
                                                                       final List<StreamingSessionEndpoint> streamSessionEndpoints) {
        validateStreamingSessionEndpointsCreation(peer, streamingSessionId);
        final List<StreamingSessionEndpoint> endpoints = streamingSessionDataService.addStreamingSessionEndpoints(
                streamingSessionId, streamSessionEndpoints);
        if (streamingSessionDataService.getStreamingSession(streamingSessionId)
                .getStreamingSessionStatus().equals(StreamingSessionStatus.NEW)) {
            streamingSessionDataService.updateStreamingSessionStatus(streamingSessionId, StreamingSessionStatus.READY);
        }
        return endpoints;
    }

    public List<StreamingSessionEndpoint> getStreamingSessionEndpoints(final Peer peer,
                                                                       final Integer streamingSessionId,
                                                                       final List<StreamingSessionStatus> statuses) {
        validateStreamingSessionEndpointsFetch(peer, streamingSessionId);
        return streamingSessionDataService.getStreamingSessionEndpoints(streamingSessionId, statuses);
    }

    private void validateStreamingSessionCreation(final Peer peer,
                                                  final VideoGame videoGameToStream) {
        validateVideoGameExists(videoGameToStream);
        validatePeerHasntActiveStreamingSessions(peer);
    }

    private void validatePeerHasntActiveStreamingSessions(final Peer peer) {
        final List<StreamingSession> activeSessions = getStreamingSessions(
                peer,
                Arrays.asList(StreamingSessionStatus.NEW, StreamingSessionStatus.ACTIVE));
        if (!activeSessions.isEmpty()) {
            throw new MediatorServiceException("Peer can't stream more then one streaming session");
        }
    }

    private void validateVideoGameExists(final VideoGame videoGameToStream) {
        if (!gameService.exists(videoGameToStream)) {
            throw new MediatorServiceException("Requested video game doesn't exist");
        }
    }

    private void validateStreamingSessionStatusUpdate(final Peer peer,
                                                      final StreamingSession streamingSession,
                                                      final StreamingSessionStatus newStatus) {
        if (streamingSession == null) {
            throw new MediatorServiceException("Requested streaming session doesn't exist");
        }
        final Integer streamingSessionId = streamingSession.getId();
        validateStatusUpdatePermissions(peer.getId(), streamingSessionId, newStatus);
    }

    private void validateStreamingSessionExists(final Integer streamingSessionId) {
        if (!streamingSessionDataService.exists(streamingSessionId)) {
            throw new MediatorServiceException("Requested streaming session doesn't exist");
        }
    }

    private void validateStatusUpdatePermissions(final UUID updatingPeerId,
                                                 final Integer streamingSessionId,
                                                 final StreamingSessionStatus newStatus) {
        final StreamingSessionRole role = streamingSessionDataService.getPeerRole(updatingPeerId, streamingSessionId);
        streamingSessionFieldAccessValidator.validate(
                role,
                StreamingSession.STREAMING_SESSION_STATUS_FIELD_NAME,
                newStatus);
    }

    private void validateStreamingSessionEndpointsFetch(final Peer peer,
                                                        final Integer streamingSessionId) {
        validateStreamingSessionExists(streamingSessionId);
    }

    private void validateStreamingSessionEndpointsCreation(final Peer peer,
                                                           final Integer streamingSessionId) {
        validateStreamingSessionEndpointsFetch(peer, streamingSessionId);
        if (streamingSessionDataService.getPeerRole(peer.getId(), streamingSessionId) != StreamingSessionRole.STREAMER) {
            throw new MediatorServiceException("Only streaming request creator can add streaming endpoints");
        }
        final StreamingSession streamingSession = streamingSessionDataService.getStreamingSession(streamingSessionId);
        if (streamingSession.getStreamingSessionStatus().equals(StreamingSessionStatus.FINISHED)) {
            throw new MediatorServiceException(String.format("Can't add streaming session endpoint to already finished session (%d)",
                    streamingSessionId));
        }
    }
}
