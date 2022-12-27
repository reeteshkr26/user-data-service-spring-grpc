package com.techno.userdataservice.handlers;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.lognet.springboot.grpc.recovery.GRpcExceptionHandler;
import org.lognet.springboot.grpc.recovery.GRpcExceptionScope;
import org.lognet.springboot.grpc.recovery.GRpcServiceAdvice;

@GRpcServiceAdvice
public class GlobalExceptionHandler {

    @GRpcExceptionHandler
    public Status handleUserNotFound(StatusRuntimeException exception, GRpcExceptionScope scope){
        return exception.getStatus().withDescription(exception.getMessage()).withCause(exception);
    }
}
