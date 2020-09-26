package com.ubisoft.streaming.testtask.p2pmediator.controller;

import com.ubisoft.streaming.testtask.p2pmediator.auth.Peer;
import com.ubisoft.streaming.testtask.p2pmediator.dto.Game;
import com.ubisoft.streaming.testtask.p2pmediator.service.StreamingSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/streaming-session")
public class StreamingSessionController {
    private final StreamingSessionService streamingSessionService;

    @Autowired
    public StreamingSessionController(final StreamingSessionService streamingSessionService) {
        this.streamingSessionService = streamingSessionService;
    }

    @PostMapping
    public void createStreamingSession(@AuthenticationPrincipal final Peer peer,
                                       final Game gameToStream) {
        streamingSessionService.createStreamingSession(peer, gameToStream);
    }
}
