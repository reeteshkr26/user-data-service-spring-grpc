package com.techno.userdataservice.mapper;

import com.techno.user.data.api.v1.Gender;
import com.techno.user.data.api.v1.GetUserResponse;
import com.techno.user.data.api.v1.UserRegistrationRequest;
import com.techno.user.data.api.v1.UserRegistrationResponse;
import com.techno.userdataservice.model.UserEntity;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity toUserEntity(UserRegistrationRequest request){
        UserEntity user = new UserEntity();
        user.setId(RandomStringUtils.random(8,true, false));
        user.setPassword(request.getPassword());
        user.setUsername(request.getUsername());
        user.setName(request.getName());
        user.setAddress(request.getAddress());
        user.setEmail(request.getEmail());
        user.setMobile(request.getMobile());
        user.setAge(request.getAge());
        user.setGender(request.getGender().name());
        return user;
    }

    public UserRegistrationResponse toRegistrationResponseProto(UserEntity user){
        return UserRegistrationResponse
                .newBuilder()
                .setId(user.getId())
                .setName(user.getName())
                .setUsername(user.getUsername())
                .build();
    }

    public GetUserResponse toGetUserResponseProto(UserEntity user){
        Gender gender;
        if(user.getGender()=="MALE"){
          gender = Gender.MALE;
        } else if (user.getGender()=="FEMALE") {
            gender = Gender.FEMALE;
        }
        else {
            gender = Gender.OTHER;
        }
        return GetUserResponse.newBuilder()
                .setId(user.getId())
                .setName(user.getName())
                .setAddress(user.getAddress())
                .setUsername(user.getUsername())
                .setMobile(user.getMobile())
                .setGender(gender)
                .build();
    }
}
