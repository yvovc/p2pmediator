package com.ubisoft.streaming.testtask.p2pmediator.dataservice.implementation;

import com.ubisoft.streaming.testtask.p2pmediator.dataservice.IStreamingSessionDataService;
import com.ubisoft.streaming.testtask.p2pmediator.auth.Peer;
import com.ubisoft.streaming.testtask.p2pmediator.dto.Game;
import org.jooq.DSLContext;

public class JooqStreamingSessionDataService implements IStreamingSessionDataService {
    private final DSLContext dslContext;

    public JooqStreamingSessionDataService(final DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public void createStreamingSession(final Peer peer,
                                       final Game game) {
        dslContext.insertInto(STREAMING_SESSION)
                .set(STREAMING_SESSION.PEER_ID, peer.getId())
                .set(STREAMING_SESSION.GAME_ID, game.getId())
                .execute();
    }
}
