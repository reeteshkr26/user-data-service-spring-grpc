package com.techno.userdataservice.service;

import com.techno.user.data.api.v1.GetUserRequest;
import com.techno.user.data.api.v1.GetUserResponse;
import com.techno.user.data.api.v1.UserRegistrationRequest;
import com.techno.user.data.api.v1.UserRegistrationResponse;
import com.techno.userdataservice.exception.StatusRuntimeExceptionBuilder;
import com.techno.userdataservice.mapper.UserMapper;
import com.techno.userdataservice.model.UserEntity;
import com.techno.userdataservice.repository.UserRepository;
import io.grpc.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService {

    private static final  Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper mapper;

    public UserRegistrationResponse createUser(UserRegistrationRequest request) {
        if(userRepository.findByUsername(request.getUsername()).isPresent()){
            LOGGER.info("user already exist by this username = {}", request.getUsername());
           throw StatusRuntimeExceptionBuilder.build(
                    Status.Code.ALREADY_EXISTS.value(),
                    "user-service",
                    "username = "+request.getUsername()+" already exist"
            );
            //throw new UserAlreadyExist("username = "+request.getUsername()+" already exist");
        }
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            LOGGER.info("user already exist by this email = {}", request.getEmail());
            throw StatusRuntimeExceptionBuilder.build(
             Status.Code.ALREADY_EXISTS.value(),
             "user-service",
             "email = "+request.getEmail()+" already exist"
             );
            //throw new UserAlreadyExist("email = "+request.getEmail()+" already exist");
        }
        UserEntity user = mapper.toUserEntity(request);
        userRepository.saveAndFlush(user);
        return mapper.toRegistrationResponseProto(user);
    }

    public GetUserResponse getUser(GetUserRequest request){
        Optional<UserEntity> user = userRepository.findByUsername(request.getUsername());
        if(user.isEmpty()){
            LOGGER.info("user not found by this username {}", request.getUsername());
            throw StatusRuntimeExceptionBuilder.build(
             Status.Code.NOT_FOUND.value(),
             "user-service",
             "username = "+request.getUsername()+" not found"
             );
            //throw new UserNotFoundException("username = "+request.getUsername()+" not found");
        }
        return mapper.toGetUserResponseProto(user.get());
    }
}
