package com.ubisoft.streaming.testtask.p2pmediator.dataservice.implementation;

import com.ubisoft.streaming.testtask.p2pmediator.dataservice.IStreamingSessionDataService;
import com.ubisoft.streaming.testtask.p2pmediator.auth.Peer;
import com.ubisoft.streaming.testtask.p2pmediator.dto.Game;
import com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session.StreamingSession;
import com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session.StreamingSessionStatus;
import org.jooq.DSLContext;

import java.util.List;

import static com.ubisoft.streaming.testtask.p2pmediator.p2pmediatordb.Tables.STREAMING_SESSION;

public class JooqStreamingSessionDataService implements IStreamingSessionDataService {
    private final DSLContext dslContext;

    public JooqStreamingSessionDataService(final DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public void createStreamingSession(final Peer peer,
                                       final Game game) {
        dslContext.insertInto(STREAMING_SESSION)
                .set(STREAMING_SESSION.HOST_ID, peer.getId())
                .set(STREAMING_SESSION.VIDEO_GAME_ID, game.getId())
                .execute();
    }

    @Override
    public List<StreamingSession> getStreamingSessions(final List<StreamingSessionStatus> statuses) {
        return dslContext.selectFrom(STREAMING_SESSION).fetchInto(StreamingSession.class);
    }
}
