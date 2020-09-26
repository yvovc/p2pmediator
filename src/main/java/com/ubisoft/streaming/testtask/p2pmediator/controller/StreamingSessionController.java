package com.ubisoft.streaming.testtask.p2pmediator.controller;

import com.ubisoft.streaming.testtask.p2pmediator.auth.Peer;
import com.ubisoft.streaming.testtask.p2pmediator.dto.Game;
import com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session.StreamingSession;
import com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session.StreamingSessionStatus;
import com.ubisoft.streaming.testtask.p2pmediator.service.StreamingSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/streaming-session")
public class StreamingSessionController {
    private final StreamingSessionService streamingSessionService;

    @Autowired
    public StreamingSessionController(final StreamingSessionService streamingSessionService) {
        this.streamingSessionService = streamingSessionService;
    }

    @GetMapping
    public List<StreamingSession> getStreamingSessions(@AuthenticationPrincipal final Peer peer,
                                                       @RequestParam(required = false) final List<StreamingSessionStatus> statuses) {
        return streamingSessionService.getStreamingSessions(peer, statuses);
    }

    @PostMapping
    public void createStreamingSession(@AuthenticationPrincipal final Peer peer,
                                       @RequestBody final Game gameToStream) {
        streamingSessionService.createStreamingSession(peer, gameToStream);
    }
}
