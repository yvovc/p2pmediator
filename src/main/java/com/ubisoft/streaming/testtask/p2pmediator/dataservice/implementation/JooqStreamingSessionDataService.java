package com.ubisoft.streaming.testtask.p2pmediator.dataservice.implementation;

import com.ubisoft.streaming.testtask.p2pmediator.auth.Peer;
import com.ubisoft.streaming.testtask.p2pmediator.dataservice.IStreamingSessionDataService;
import com.ubisoft.streaming.testtask.p2pmediator.dto.VideoGame;
import com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session.StreamingSession;
import com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session.StreamingSessionStatus;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.ubisoft.streaming.testtask.p2pmediator.p2pmediatordb.Tables.STREAMING_SESSION;
import static com.ubisoft.streaming.testtask.p2pmediator.p2pmediatordb.Tables.VIDEO_GAME;

public class JooqStreamingSessionDataService implements IStreamingSessionDataService {
    private final DSLContext dslContext;

    public JooqStreamingSessionDataService(final DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public void createStreamingSession(final Peer peer,
                                       final VideoGame videoGame) {
        dslContext.insertInto(STREAMING_SESSION)
                .set(STREAMING_SESSION.HOST_ID, peer.getId())
                .set(STREAMING_SESSION.VIDEO_GAME_ID, videoGame.getId())
                .set(STREAMING_SESSION.STREAMING_SESSION_STATUS, StreamingSessionStatus.NEW)
                .execute();
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
}
