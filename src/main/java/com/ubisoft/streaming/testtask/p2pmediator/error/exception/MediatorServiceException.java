package com.ubisoft.streaming.testtask.p2pmediator.error.exception;

/**
 * Local exception to use be used by excepion handler.
 */
public class MediatorServiceException extends RuntimeException {
    public MediatorServiceException() {
    }

    public MediatorServiceException(String message) {
        super(message);
    }

    public MediatorServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public MediatorServiceException(Throwable cause) {
        super(cause);
    }

    public MediatorServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
