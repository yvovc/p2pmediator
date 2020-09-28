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

/**
 * Controller that exposes streaming sessions related endpoints.
 *
 * @author yvovc
 * @since 2020/26/09
 */
@RestController
@RequestMapping("/streaming-session")
public class StreamingSessionController {
    private final StreamingSessionService streamingSessionService;

    @Autowired
    public StreamingSessionController(final StreamingSessionService streamingSessionService) {
        this.streamingSessionService = streamingSessionService;
    }

    /**
     * Gets streaming sessions with specified statuses.
     *
     * @param peer     active peer
     * @param statuses statuses of streaming sessions to fetch
     * @return streaming sessions with specified statuses
     */
    @GetMapping
    public List<StreamingSession> getStreamingSessions(@AuthenticationPrincipal final Peer peer,
                                                       @RequestParam(required = false) final List<StreamingSessionStatus> statuses) {
        return streamingSessionService.getStreamingSessions(peer, statuses);
    }

    /**
     * Creates streaming session of the specified video game.
     *
     * @param peer              active peer
     * @param videoGameToStream game to stream
     * @return created {@link StreamingSession}
     */
    @PostMapping
    public StreamingSession createStreamingSession(@AuthenticationPrincipal final Peer peer,
                                                   @RequestBody final VideoGame videoGameToStream) {
        return streamingSessionService.createStreamingSession(peer, videoGameToStream);
    }

    /**
     * Updates streaming session's status.
     *
     * @param peer               active peer
     * @param streamingSessionId streaming session ID
     * @param newStatus          new status
     * @return updated streaming session
     */
    @PutMapping("/{streamingSessionId}/status")
    public StreamingSessionStatus updateStreamingSessionStatus(@AuthenticationPrincipal final Peer peer,
                                                               @PathVariable final Integer streamingSessionId,
                                                               @RequestParam final StreamingSessionStatus newStatus) {
        return streamingSessionService.updateStreamingSessionStatus(peer, streamingSessionId, newStatus);
    }

    /**
     * Gets streaming session's endpoints.
     *
     * @param peer               active peer
     * @param streamingSessionId streaming session ID
     * @return list of {@link StreamingSessionEndpoint}s
     */
    @GetMapping("/{streamingSessionId}/endpoint")
    public List<StreamingSessionEndpoint> getStreamingSessionEndpoints(@AuthenticationPrincipal final Peer peer,
                                                                       @PathVariable final Integer streamingSessionId) {
        return streamingSessionService.getStreamingSessionEndpoints(peer, streamingSessionId);
    }

    /**
     * Adds
     *
     * @param peer                      active peer
     * @param streamingSessionId        streaming session ID
     * @param streamingSessionEndpoints streaming session endpoints to add
     * @return added {@link StreamingSessionEndpoint}s
     */
    @PostMapping("/{streamingSessionId}/endpoint")
    public List<StreamingSessionEndpoint> addStreamSessionEndpoints(@AuthenticationPrincipal Peer peer,
                                                                    @PathVariable final Integer streamingSessionId,
                                                                    @RequestBody List<StreamingSessionEndpoint> streamingSessionEndpoints) {
        return streamingSessionService.addStreamingSessionEndpoints(peer, streamingSessionId, streamingSessionEndpoints);
    }
}
