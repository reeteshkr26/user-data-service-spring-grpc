package com.techno.userdataservice.grpc;

import com.techno.user.data.api.v1.*;
import com.techno.userdataservice.service.UserService;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GRpcService
public class UserServiceApi extends UserServiceGrpc.UserServiceImplBase {

    @Autowired
    private UserService userService;

    @Override
    public void registerUser(UserRegistrationRequest request, StreamObserver<UserRegistrationResponse> responseObserver) {
        responseObserver.onNext(userService.createUser(request));
        responseObserver.onCompleted();
    }

    @Override
    public void getUser(GetUserRequest request, StreamObserver<GetUserResponse> responseObserver) {
        responseObserver.onNext(userService.getUser(request));
        responseObserver.onCompleted();
    }
}
