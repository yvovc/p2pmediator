package com.ubisoft.streaming.testtask.p2pmediator.controller;

import com.ubisoft.streaming.testtask.p2pmediator.auth.Peer;
import com.ubisoft.streaming.testtask.p2pmediator.dto.VideoGame;
import com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session.StreamingSession;
import com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session.StreamingSessionEndpoint;
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
    public StreamingSession createStreamingSession(@AuthenticationPrincipal final Peer peer,
                                                   @RequestBody final VideoGame videoGameToStream) {
        return streamingSessionService.createStreamingSession(peer, videoGameToStream);
    }

    @PutMapping("/{streamingSessionId}/status")
    public StreamingSessionStatus updateStreamingSessionStatus(@AuthenticationPrincipal final Peer peer,
                                                               @PathVariable final Integer streamingSessionId,
                                                               @RequestParam final StreamingSessionStatus newStatus) {
        return streamingSessionService.updateStreamingSessionStatus(peer, streamingSessionId, newStatus);
    }

    @GetMapping("/{streamingSessionId}/endpoint")
    public List<StreamingSessionEndpoint> getStreamingSessionEndpoints(@AuthenticationPrincipal final Peer peer,
                                                                       @PathVariable final Integer streamingSessionId,
                                                                       @RequestParam final List<StreamingSessionStatus> statuses) {
        return streamingSessionService.getStreamingSessionEndpoints(peer, streamingSessionId, statuses);
    }

    @PostMapping("/{streamingSessionId}/endpoint")
    public List<StreamingSessionEndpoint> addStreamSessionEndpoints(@AuthenticationPrincipal Peer peer,
                                                                    @PathVariable final Integer streamingSessionId,
                                                                    @RequestBody List<StreamingSessionEndpoint> streamSessionEndpoints) {
        return streamingSessionService.addStreamingSessionEndpoints(peer, streamingSessionId, streamSessionEndpoints);
    }
}
