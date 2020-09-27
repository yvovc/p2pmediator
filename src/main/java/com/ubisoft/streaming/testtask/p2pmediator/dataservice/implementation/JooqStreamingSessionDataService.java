package com.ubisoft.streaming.testtask.p2pmediator.dataservice.implementation;

import com.ubisoft.streaming.testtask.p2pmediator.auth.Peer;
import com.ubisoft.streaming.testtask.p2pmediator.dataservice.IStreamingSessionDataService;
import com.ubisoft.streaming.testtask.p2pmediator.dto.VideoGame;
import com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session.StreamingSession;
import com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session.StreamingSessionEndpoint;
import com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session.StreamingSessionRole;
import com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session.StreamingSessionStatus;
import com.ubisoft.streaming.testtask.p2pmediator.error.exception.MediatorServiceException;
import com.ubisoft.streaming.testtask.p2pmediator.p2pmediatordb.tables.records.StreamingSessionEndpointRecord;
import com.ubisoft.streaming.testtask.p2pmediator.p2pmediatordb.tables.records.StreamingSessionPeerRoleIndexRecord;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.jooq.UpdateQuery;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ubisoft.streaming.testtask.p2pmediator.p2pmediatordb.Tables.*;

public class JooqStreamingSessionDataService implements IStreamingSessionDataService {
    private final DSLContext dslContext;

    public JooqStreamingSessionDataService(final DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public StreamingSession createStreamingSession(final Peer peer,
                                                   final VideoGame videoGame) {
        final int streamingSessionId = dslContext.insertInto(STREAMING_SESSION)
                .set(STREAMING_SESSION.HOST_ID, peer.getId())
                .set(STREAMING_SESSION.VIDEO_GAME_ID, videoGame.getId())
                .set(STREAMING_SESSION.STREAMING_SESSION_STATUS, StreamingSessionStatus.NEW)
                .returning(STREAMING_SESSION.ID).fetchOne().getId();
        return new StreamingSession()
                .setId(streamingSessionId)
                .setHostId(peer.getId())
                .setVideoGameName(videoGame.getName())
                .setStreamingSessionStatus(StreamingSessionStatus.NEW);
    }

    @Override
    public List<StreamingSession> getStreamingSessions(final List<StreamingSessionStatus> statuses) {
        final SelectQuery<?> selectQuery = dslContext.select(
                STREAMING_SESSION.ID,
                STREAMING_SESSION.HOST_ID,
                STREAMING_SESSION.STREAMING_SESSION_STATUS,
                VIDEO_GAME.NAME.as(StreamingSession.VIDEO_GAME_NAME_FIELD_NAME)
        )
                .from(STREAMING_SESSION)
                .join(VIDEO_GAME).on(STREAMING_SESSION.VIDEO_GAME_ID.eq(VIDEO_GAME.ID)).getQuery();
        if (!CollectionUtils.isEmpty(statuses)) {
            selectQuery.addConditions(STREAMING_SESSION.STREAMING_SESSION_STATUS.in(statuses));
        }
        return selectQuery.fetchInto(StreamingSession.class);
    }

    @Override
    public StreamingSessionRole getPeerRole(final UUID peerId,
                                            final Integer streamingSessionId) {
        StreamingSessionPeerRoleIndexRecord record = dslContext.selectFrom(STREAMING_SESSION_PEER_ROLE_INDEX)
                .where(STREAMING_SESSION_PEER_ROLE_INDEX.PEER_ID.eq(peerId)
                        .and(STREAMING_SESSION_PEER_ROLE_INDEX.STREAMING_SESSION_ID.eq(streamingSessionId)))
                .fetchOne();
        if (record != null) {
            return record.getPeerRole();
        }
        throw new MediatorServiceException(
                String.format("Peer %s isn't a member of streaming session '%d'", peerId, streamingSessionId));
    }

    @Override
    public boolean exists(final Integer streamingSessionId) {
        return dslContext.selectCount().from(STREAMING_SESSION).fetchOne().component1() != 0;
    }

    @Override
    public StreamingSession getStreamingSession(final Integer streamingSessionId) {
        final SelectQuery<?> selectQuery = getSelectStreamingSessionQuery();
        selectQuery.addConditions(STREAMING_SESSION.ID.eq(streamingSessionId));
        return selectQuery.fetchOneInto(StreamingSession.class);
    }

    @Override
    public StreamingSessionStatus updateStreamingSessionStatus(final Integer streamingSessionId,
                                                               final StreamingSessionStatus newStatus) {
        final UpdateQuery<?> query = dslContext.updateQuery(STREAMING_SESSION);
        query.addValue(STREAMING_SESSION.STREAMING_SESSION_STATUS, newStatus);
        query.addConditions(STREAMING_SESSION.ID.eq(streamingSessionId));
        if (query.execute() != 0) {
            return newStatus;
        }
        throw new MediatorServiceException("Streaming session (id: '%d') status wasn't updated");
    }

    @Override
    public List<StreamingSessionEndpoint> getStreamingSessionEndpoints(final Integer streamingSessionId,
                                                                       final List<StreamingSessionStatus> statuses) {
        return dslContext.select(
                STREAMING_SESSION_ENDPOINT.STREAMING_SESSION_ID,
                STREAMING_SESSION_ENDPOINT.HOST,
                STREAMING_SESSION_ENDPOINT.PORT
        )
                .from(STREAMING_SESSION)
                .join(STREAMING_SESSION_ENDPOINT)
                .on(STREAMING_SESSION.ID.eq(STREAMING_SESSION_ENDPOINT.STREAMING_SESSION_ID))
                .where(STREAMING_SESSION.STREAMING_SESSION_STATUS.in(statuses))
                .fetchInto(StreamingSessionEndpoint.class);
    }

    @Override
    public List<StreamingSessionEndpoint> addStreamingSessionEndpoints(final Integer streamingSessionId,
                                                                       final List<StreamingSessionEndpoint> streamSessionEndpoints) {
        final List<StreamingSessionEndpointRecord> recordsToInsert = streamSessionEndpoints.stream()
                .map(endpoint -> new StreamingSessionEndpointRecord()
                        .setStreamingSessionId(streamingSessionId)
                        .setHost(endpoint.getHost())
                        .setPort(endpoint.getPort()))
                .collect(Collectors.toList());
        dslContext.batchInsert(recordsToInsert).execute();
        return streamSessionEndpoints;
    }

    @Override
    public StreamingSessionRole indexPeerStreamingSessionRole(final Peer peer,
                                                              final Integer streamingSessionId,
                                                              final StreamingSessionRole role) {
        return dslContext.insertInto(STREAMING_SESSION_PEER_ROLE_INDEX)
                .set(STREAMING_SESSION_PEER_ROLE_INDEX.PEER_ID, peer.getId())
                .set(STREAMING_SESSION_PEER_ROLE_INDEX.STREAMING_SESSION_ID, streamingSessionId)
                .set(STREAMING_SESSION_PEER_ROLE_INDEX.PEER_ROLE, role)
                .returning(STREAMING_SESSION_PEER_ROLE_INDEX.PEER_ROLE)
                .fetchOne().getPeerRole();

    }


    private SelectQuery<?> getSelectStreamingSessionQuery() {
        return dslContext.select(
                STREAMING_SESSION.ID,
                STREAMING_SESSION.HOST_ID,
                STREAMING_SESSION.STREAMING_SESSION_STATUS,
                VIDEO_GAME.NAME.as(StreamingSession.VIDEO_GAME_NAME_FIELD_NAME)
        )
                .from(STREAMING_SESSION)
                .join(VIDEO_GAME).on(STREAMING_SESSION.VIDEO_GAME_ID.eq(VIDEO_GAME.ID)).getQuery();
    }
}
