package com.techno.userdataservice.interceptors;

import com.techno.userdataservice.exception.UserAlreadyExist;
import com.techno.userdataservice.exception.UserNotFoundException;
import io.grpc.*;
import org.lognet.springboot.grpc.GRpcGlobalInterceptor;

@GRpcGlobalInterceptor
public class GlobalExceptionInterceptor implements ServerInterceptor {
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata,
                                                                 ServerCallHandler<ReqT, RespT> serverCallHandler) {
        ServerCall.Listener<ReqT> listener = serverCallHandler.startCall(serverCall, metadata);
        return new ExceptionHandlingServerCallListener<>(listener, serverCall, metadata);
    }

    private class ExceptionHandlingServerCallListener<ReqT, RespT>
            extends ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT> {
        private ServerCall<ReqT, RespT> serverCall;
        private Metadata metadata;

        ExceptionHandlingServerCallListener(ServerCall.Listener<ReqT> listener, ServerCall<ReqT, RespT> serverCall,
                                            Metadata metadata) {
            super(listener);
            this.serverCall = serverCall;
            this.metadata = metadata;
        }

        @Override
        public void onHalfClose() {
            try {
                super.onHalfClose();
            } catch (StatusRuntimeException ex) {
                handleException(ex, serverCall, metadata);
            } catch (Exception ex){
                handleException(ex, serverCall, metadata);
            }
        }

        @Override
        public void onReady() {
            try {
                super.onReady();
            } catch (StatusRuntimeException ex) {
                handleException(ex, serverCall, metadata);
            }
            catch (Exception ex){
                handleException(ex, serverCall, metadata);
            }
        }

        private void handleException(StatusRuntimeException exception, ServerCall<ReqT, RespT> serverCall, Metadata metadata) {
            serverCall.close(exception.getStatus().withDescription(exception.getMessage()),metadata);
        }
        private void handleException(Exception exception, ServerCall<ReqT, RespT> serverCall, Metadata metadata) {
            if(exception instanceof IllegalArgumentException){
                serverCall.close(Status.INVALID_ARGUMENT.withDescription(exception.getMessage()),metadata);
            } else if (exception instanceof IllegalStateException) {
                serverCall.close(Status.FAILED_PRECONDITION.withDescription(exception.getMessage()),metadata);
            } else if (exception instanceof UserNotFoundException) {
                serverCall.close(Status.NOT_FOUND.withDescription(exception.getMessage()),metadata);
            } else if (exception instanceof UserAlreadyExist) {
                serverCall.close(Status.ALREADY_EXISTS.withDescription(exception.getMessage()), metadata);
            } else {
                serverCall.close(Status.UNKNOWN.withDescription(exception.getMessage()),metadata);
            }

        }
    }
}
