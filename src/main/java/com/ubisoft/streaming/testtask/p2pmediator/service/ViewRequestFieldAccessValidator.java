package com.ubisoft.streaming.testtask.p2pmediator.service;

import com.google.common.collect.ImmutableMap;
import com.ubisoft.streaming.testtask.p2pmediator.dataservice.IViewRequestDataService;
import com.ubisoft.streaming.testtask.p2pmediator.dto.streaming.session.StreamingSessionRole;
import com.ubisoft.streaming.testtask.p2pmediator.dto.view.request.ViewRequest;
import com.ubisoft.streaming.testtask.p2pmediator.dto.view.request.ViewRequestStatus;
import com.ubisoft.streaming.testtask.p2pmediator.error.exception.MediatorServiceException;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class ViewRequestFieldAccessValidator {
    /**
     * Map that stores available view request statuses transitions for STREAMER role.
     */
    final Map<ViewRequestStatus, List<ViewRequestStatus>> STREAMER_FROM_TO_STATUSES_RULES =
            ImmutableMap.<ViewRequestStatus, List<ViewRequestStatus>>builder()
                    .put(ViewRequestStatus.READY, Collections.singletonList(ViewRequestStatus.PROCESSED))
                    .build();

    /**
     * Map that stores available view request statuses transitions for STREAMER role.
     */
    final Map<ViewRequestStatus, List<ViewRequestStatus>> VIEWER_FROM_TO_STATUSES_RULES =
            ImmutableMap.<ViewRequestStatus, List<ViewRequestStatus>>builder()
                    .put(ViewRequestStatus.NEW, Collections.singletonList(ViewRequestStatus.READY))
                    .build();

    private final Map<String, List<Pair<StreamingSessionRole, ViewRequestFieldAccessValidatorMethod>>>
            STREAMING_SESSION_UPDATE_PERMISSIONS_BY_ROLE =
            ImmutableMap.<String, List<Pair<StreamingSessionRole, ViewRequestFieldAccessValidatorMethod>>>builder()
                    .put(
                            ViewRequest.VIEW_REQUEST_STATUS_FIELD_NAME,
                            Arrays.asList(
                                    Pair.of(StreamingSessionRole.STREAMER, this::validateStreamerStatusUpdate),
                                    Pair.of(StreamingSessionRole.VIEWER, this::validateViewerStatusUpdate)
                            )
                    ).build();


    private final IViewRequestDataService viewRequestDataService;

    @Autowired
    public ViewRequestFieldAccessValidator(final IViewRequestDataService viewRequestDataService) {
        this.viewRequestDataService = viewRequestDataService;
    }

    public void validate(final Integer viewRequestId,
                         final StreamingSessionRole role,
                         final String fieldName,
                         final Object newValue) {
        STREAMING_SESSION_UPDATE_PERMISSIONS_BY_ROLE.get(fieldName).stream()
                .filter(validator -> validator.getKey() == role)
                .map(Pair::getValue)
                .findFirst()
                .ifPresent(validator -> {
                    if (!validator.validateFieldAccess(viewRequestId, newValue)) {
                        throw new RuntimeException(String.format("User with role %s cannot assign field %s with value %s",
                                role, fieldName, newValue));
                    }
                });
    }

    private interface ViewRequestFieldAccessValidatorMethod {
        boolean validateFieldAccess(final Integer viewRequestId,
                                    final Object valueToValidate);
    }

    private boolean validateStreamerStatusUpdate(final Integer viewRequestId,
                                                 final Object newStatus) {
        if (!(newStatus instanceof ViewRequestStatus)) {
            return false;
        }
        final ViewRequestStatus currentStatus = viewRequestDataService.getViewRequest(viewRequestId).getViewRequestStatus();
        final List<ViewRequestStatus> availableStatuses =
                STREAMER_FROM_TO_STATUSES_RULES.getOrDefault(
                        currentStatus,
                        Collections.emptyList());
        return availableStatuses.contains(newStatus);
    }

    private boolean validateViewerStatusUpdate(final Integer viewRequestId,
                                               final Object newStatus) {
        if (!(newStatus instanceof ViewRequestStatus)) {
            return false;
        }
        final ViewRequest viewRequest = viewRequestDataService.getViewRequest(viewRequestId);
        final ViewRequestStatus currentStatus = viewRequest.getViewRequestStatus();
        final List<ViewRequestStatus> availableStatuses =
                VIEWER_FROM_TO_STATUSES_RULES.getOrDefault(
                        currentStatus,
                        Collections.emptyList());

        final boolean permitted = availableStatuses.contains(newStatus);
        if (permitted && newStatus == ViewRequestStatus.READY) {
            final boolean viewRequestEndpointsSet = !viewRequestDataService.getViewRequestEndpoints(viewRequestId,
                    Collections.singletonList(viewRequest.getViewRequestStatus())).isEmpty();
            if (!viewRequestEndpointsSet) {
                throw new MediatorServiceException(
                        String.format(
                                "View request %s hasn't any configured endpoints and illegal to become READY",
                                viewRequest.getId()));
            }
        }
        return permitted;
    }
}
