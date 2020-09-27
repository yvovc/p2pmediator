package com.ubisoft.streaming.testtask.p2pmediator.service;

import com.google.common.collect.ImmutableMap;
import com.ubisoft.streaming.testtask.p2pmediator.auth.Peer;
import com.ubisoft.streaming.testtask.p2pmediator.dataservice.IStreamingSessionDataService;
import com.ubisoft.streaming.testtask.p2pmediator.dto.VideoGame;
import com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session.StreamingSession;
import com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session.StreamingSessionEndpoint;
import com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session.StreamingSessionRole;
import com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session.StreamingSessionStatus;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StreamingSessionService {

    /**
     * Map that shows which fields are eligible to be updated by some Role and to which values update is possible.
     * E.g: Streamer can update streaming session status field to FINISHED state.
     */
    final Map<String, List<Pair<StreamingSessionRole, List<Object>>>> STREAMING_SESSION_UPDATE_PERMISSIONS_BY_ROLE =
            ImmutableMap.<String, List<Pair<StreamingSessionRole, List<Object>>>>builder()
                    .put(
                            StreamingSession.STREAMING_SESSION_STATUS_FIELD_NAME,
                            Collections.singletonList(Pair.of(StreamingSessionRole.STREAMER,
                                    Collections.singletonList(StreamingSessionStatus.FINISHED)))
                    ).build();


    private final GameService gameService;
    private final IStreamingSessionDataService streamingSessionDataService;

    @Autowired
    public StreamingSessionService(final IStreamingSessionDataService streamingSessionDataService,
                                   final GameService gameService) {
        this.gameService = gameService;
        this.streamingSessionDataService = streamingSessionDataService;
    }

    public StreamingSession createStreamingSession(final Peer peer,
                                                   final VideoGame videoGameToStream) {
        validateStreamingSessionCreation(peer, videoGameToStream);
        return streamingSessionDataService.createStreamingSession(peer, videoGameToStream);
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
            throw new RuntimeException("Peer can't stream more then one streaming session");
        }
    }

    private void validateVideoGameExists(final VideoGame videoGameToStream) {
        if (!gameService.exists(videoGameToStream)) {
            throw new RuntimeException("Requested video game doesn't exist");
        }
    }

    private void validateStreamingSessionStatusUpdate(final Peer peer,
                                                      final StreamingSession streamingSession,
                                                      final StreamingSessionStatus newStatus) {
        final int streamingSessionId = streamingSession.getId();
        validateStreamingSessionExists(streamingSessionId);
        validateStatusUpdatePermissions(peer.getId(), streamingSessionId, newStatus);
    }

    private void validateStreamingSessionExists(final Integer streamingSessionId) {
        if (!streamingSessionDataService.exists(streamingSessionId)) {
            throw new RuntimeException("Requested streaming session doesn't exist");
        }
    }

    private void validateStatusUpdatePermissions(final UUID updatingPeerId,
                                                 final Integer streamingSessionId,
                                                 final StreamingSessionStatus newStatus) {
        final StreamingSessionRole role = streamingSessionDataService.getPeerRole(updatingPeerId, streamingSessionId);
        validateRoleCanUpdateStreamingSessionField(role, StreamingSession.STREAMING_SESSION_STATUS_FIELD_NAME, newStatus);
    }

    private void validateRoleCanUpdateStreamingSessionField(final StreamingSessionRole role,
                                                            final String streamingSessionStatusFieldName,
                                                            final Object newValue) {
        final boolean permitted = STREAMING_SESSION_UPDATE_PERMISSIONS_BY_ROLE.get(streamingSessionStatusFieldName).stream()
                .anyMatch(pair -> pair.getKey().equals(role) && pair.getValue().contains(newValue));
        if (!permitted) {
            throw new RuntimeException(
                    String.format("Role %s can't update field '%s' to '%s' value", role, streamingSessionStatusFieldName,
                            newValue));
        }
    }

    private void validateStreamingSessionEndpointsFetch(final Peer peer,
                                                        final Integer streamingSessionId) {
        validateStreamingSessionExists(streamingSessionId);
        streamingSessionDataService.getPeerRole(peer.getId(), streamingSessionId);
    }

    private void validateStreamingSessionEndpointsCreation(final Peer peer,
                                                           final Integer streamingSessionId) {
        validateStreamingSessionEndpointsFetch(peer, streamingSessionId);
        final StreamingSession streamingSession = streamingSessionDataService.getStreamingSession(streamingSessionId);
        if (streamingSession.getStreamingSessionStatus().equals(StreamingSessionStatus.FINISHED)) {
            throw new RuntimeException(String.format("Can't add streaming session endpoint to already finished session (%d)",
                    streamingSessionId));
        }
    }
}
