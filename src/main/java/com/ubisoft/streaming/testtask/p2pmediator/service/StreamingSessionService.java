package com.ubisoft.streaming.testtask.p2pmediator.service;

import com.ubisoft.streaming.testtask.p2pmediator.dataservice.IStreamingSessionDataService;
import com.ubisoft.streaming.testtask.p2pmediator.auth.Peer;
import com.ubisoft.streaming.testtask.p2pmediator.dto.VideoGame;
import com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session.StreamingSession;
import com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session.StreamingSessionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StreamingSessionService {
    private final GameService gameService;
    private final IStreamingSessionDataService streamingSessionDataService;

    @Autowired
    public StreamingSessionService(final IStreamingSessionDataService streamingSessionDataService,
                                   final GameService gameService) {
        this.gameService = gameService;
        this.streamingSessionDataService = streamingSessionDataService;
    }

    public void createStreamingSession(final Peer peer,
                                       final VideoGame videoGameToStream) {
        validateStreamingSessionCreation(peer, videoGameToStream);
        streamingSessionDataService.createStreamingSession(peer, videoGameToStream);
    }

    public List<StreamingSession> getStreamingSessions(final Peer peer,
                                                       final List<StreamingSessionStatus> statuses) {
        return streamingSessionDataService.getStreamingSessions(statuses);
    }

    private void validateGameExists(final VideoGame videoGameToStream) {

    }

    private void validateStreamingSessionCreation(final Peer peer,
                                                  final VideoGame videoGameToStream) {
        gameService.exists(videoGameToStream);
    }
}
