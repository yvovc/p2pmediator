package com.ubisoft.streaming.testtask.p2pmediator.error;

/**
 * Service response error object.
 *
 * @author yvovc
 * @since
 */
public class ServiceResponseError {
    private final String errorMessage;

    public ServiceResponseError(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
