package com.ubisoft.streaming.testtask.p2pmediator.error;

import com.ubisoft.streaming.testtask.p2pmediator.error.exception.MediatorServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author yvovc
 */
@RestControllerAdvice
public class RestExceptionsHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MediatorServiceException.class)
    public ServiceResponseError handleMediatorServiceException(final MediatorServiceException mediatorServiceException) {
        return new ServiceResponseError(mediatorServiceException.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public ServiceResponseError handleRuntimeException(final RuntimeException runtimeException) {
        return new ServiceResponseError(runtimeException.getMessage());
    }
}